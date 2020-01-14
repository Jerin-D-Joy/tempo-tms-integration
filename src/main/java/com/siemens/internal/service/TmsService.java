package com.siemens.internal.service;

import com.siemens.internal.config.JiraConfig;
import com.siemens.internal.entities.TmsData;
import com.siemens.internal.exceptions.PartialDataSaveException;
import com.siemens.internal.models.TempoRequest;
import com.siemens.internal.models.TmsInput;
import com.siemens.internal.models.WorklogDetails;
import com.siemens.internal.models.WorklogResponse;
import com.siemens.internal.repositories.TmsDataRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.siemens.internal.common.Constants.CODE_DEVELOP;
import static com.siemens.internal.common.Constants.OFFSHORE;
import static com.siemens.internal.common.Constants.SIXTY;

@Service
@Slf4j
public class TmsService {

    @Autowired
    private TempoService tempoService;

    @Autowired
    private TmsDataRepository tmsDataRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<WorklogDetails> getTempoFormat(TempoRequest tempoRequest) {
        WorklogResponse[] worklogResponses = tempoService.getUserDataFromTempo(
                tempoRequest.getFrom(), tempoRequest.getTo(), tempoRequest.getWorker());
        List<WorklogDetails> worklogDetailsList = extractWorklogDetailsFromResponse(worklogResponses);
        return worklogDetailsList;
    }

    public List<WorklogDetails> extractWorklogDetailsFromResponse(WorklogResponse[] worklogResponseList) {
        List<WorklogDetails> worklogDetailsList = new ArrayList<>();
        if(worklogResponseList!=null && worklogResponseList.length > 0) {
            for (WorklogResponse worklogResponse : worklogResponseList) {
                worklogDetailsList.add(convertToWorklogDetails(worklogResponse));
            }
        }
        return worklogDetailsList;
    }

    public WorklogDetails convertToWorklogDetails(WorklogResponse worklogResponse) {
        int issueId = worklogResponse.getIssue().getId();
        JiraConfig.ProjectDetails issueDetails = tempoService.getIssueDetails(issueId);
        WorklogDetails worklogDetails = new WorklogDetails(
                (float) worklogResponse.getTimeSpentSeconds()/(SIXTY * SIXTY),
                worklogResponse.getStarted(),
                worklogResponse.getIssue().getKey(),
                worklogResponse.getIssue().getProjectId(),
                issueDetails.getProjectName());
        return worklogDetails;
    }

    @Transactional
    public List<TmsInput> getDataFromTempoAndParseToTmsInput(TempoRequest tempoRequest) throws Exception {
        WorklogResponse[] worklogResponses = tempoService.getUserDataFromTempo(
                tempoRequest.getFrom(),
                tempoRequest.getTo(),
                tempoRequest.getWorker());
        List<TmsInput> tmsInputList = convertToTmsInput(worklogResponses);
        //deletePreviousTmsEntries(tempoRequest.getWorker(), tempoRequest.getFrom(), tempoRequest.getTo());
        makePreviousTmsEntriesInactive(tempoRequest.getWorker(), tempoRequest.getFrom(), tempoRequest.getTo());
        List<TmsInput> tmsErrorList = new ArrayList<>();
        if(!CollectionUtils.isEmpty(tmsInputList)) {
            tmsErrorList = saveTmsData(tmsInputList);
        }
        if (tmsErrorList.size() > 0) {
            throw new PartialDataSaveException(tmsErrorList);
        }
        return tmsErrorList;
    }

    public List<TmsInput> saveTmsData(List<TmsInput> tmsInputList) {
        List<TmsInput> tmsOutputList = new ArrayList<>();
        for (TmsInput tmsInput : tmsInputList) {
            try {
                tmsDataRepository.save(modelMapper.map(tmsInput, TmsData.class));
            } catch (Exception ex) {
                tmsOutputList.add(tmsInput);
            }
        }
        return tmsOutputList;
    }

    public void deletePreviousTmsEntries(String[] rollNos, String startDate, String endDate) {
        for(String rollNo : rollNos) {
            tmsDataRepository.deleteByRollNoAndDates(rollNo, startDate, endDate);
        }
    }

    public void makePreviousTmsEntriesInactive(String[] rollNos, String startDate, String endDate) throws Exception {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date from = formatter.parse(startDate);
            Date to = formatter.parse(endDate);
            for (String rollNo : rollNos) {
                int inactivatedEntries = tmsDataRepository.updateByRollNoAndDates(rollNo, from, to);
                log.info("Inactivated {} entries for {} between dates {} and {}", inactivatedEntries, rollNo, from, to);
            }
            log.info("Deactivating entries between {} and {}", startDate, endDate);
        } catch (Exception ex) {
            log.error("Deactivating entries between {} and {} failed", startDate, endDate);
            throw ex;
        }
    }

    public List<TmsInput> getAllFromDb() {
        List<TmsData> tmsDataList = tmsDataRepository.findAll();
        List<TmsInput> tmsInputList = new ArrayList<>();
        for(TmsData tmsData: tmsDataList) {
            tmsInputList.add(modelMapper.map(tmsData, TmsInput.class));
        }
        return tmsInputList;
    }

    public List<TmsInput> convertToTmsInput(WorklogResponse[] worklogResponseList) {
        List<TmsInput> tmsInputList = new ArrayList<>();
        for(WorklogResponse worklogResponse : worklogResponseList) {
            tmsInputList.add(convertWorklogDetailsToTmsInput(worklogResponse));
        }
        return tmsInputList;
    }

    public TmsInput convertWorklogDetailsToTmsInput (WorklogResponse worklogResponse) {
        TmsInput tmsInput = new TmsInput();
        tmsInput.setRollNo(worklogResponse.getWorker());
        //int issueId = worklogResponse.getIssue().getId();
        int projectId = worklogResponse.getIssue().getProjectId();
        //JiraConfig.ProjectDetails issueDetails = tempoService.getIssueDetails(issueId);
        JiraConfig.ProjectDetails projectDetails = tempoService.getProjectDetails(projectId);
        tmsInput.setProjectName(projectDetails.getProjectName());
        tmsInput.setActivityName("");
        tmsInput.setBillableType(projectDetails.getBillableType());
        tmsInput.setLocationIndicator(OFFSHORE);
        tmsInput.setDateOfEntry(worklogResponse.getStarted());
        tmsInput.setEffort((float) worklogResponse.getTimeSpentSeconds()/(SIXTY * SIXTY));
        tmsInput.setComments("");
        tmsInput.setTmsProjectName(projectDetails.getProjectName());
        tmsInput.setTmsActivityName(projectDetails.getTmsActivityName());
        tmsInput.setPurchaseOrderId(projectDetails.getPurchaseOrderId());
        tmsInput.setActive(1);
        return tmsInput;
    }

    /*
     * Tms Accepts date in mm/dd/yyyy eg : 08/31/2015
     * Jira gives date in yyyy-mm-dd hh:mm:ss.000 eg : 2019-09-23 00:00:00.000
     * */
    public String convertDateToTmsFormat(String inputDate) {
        String date = null;
        if(!StringUtils.isEmpty(inputDate)) {
            String tempDate = inputDate.substring(0, 10);
            String[] dateParts = tempDate.split("-");
            date = dateParts[1] + "/" + dateParts[2] + "/" + dateParts[0];
        }
        return date;
    }


    public List<String> getTeamDetails() {
        tempoService.getTeamMembersFromTempo(4);
        return  null;
    }
}
