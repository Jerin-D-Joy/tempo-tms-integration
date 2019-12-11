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
import java.util.ArrayList;
import java.util.List;

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

    public List<WorklogDetails> getTempoFormat(String token, TempoRequest tempoRequest) {
        WorklogResponse[] worklogResponses = tempoService.getUserDataFromTempo(
                token, tempoRequest.getFrom(), tempoRequest.getTo(), tempoRequest.getWorker());
        List<WorklogDetails> worklogDetailsList = extractWorklogDetailsFromResponse(token, worklogResponses);
        return worklogDetailsList;
    }

    public List<WorklogDetails> extractWorklogDetailsFromResponse(String token, WorklogResponse[] worklogResponseList) {
        List<WorklogDetails> worklogDetailsList = new ArrayList<>();
        if(worklogResponseList!=null && worklogResponseList.length > 0) {
            for (WorklogResponse worklogResponse : worklogResponseList) {
                worklogDetailsList.add(convertToWorklogDetails(token, worklogResponse));
            }
        }
        return worklogDetailsList;
    }

    public WorklogDetails convertToWorklogDetails(String token, WorklogResponse worklogResponse) {
        int issueId = worklogResponse.getIssue().getId();
        JiraConfig.ProjectDetails issueDetails = tempoService.getIssueDetails(token, issueId);
        WorklogDetails worklogDetails = new WorklogDetails(
                (float) worklogResponse.getTimeSpentSeconds()/(SIXTY * SIXTY),
                worklogResponse.getStarted(),
                worklogResponse.getIssue().getKey(),
                worklogResponse.getIssue().getProjectId(),
                issueDetails.getIssueName(),
                issueDetails.getProjectName());
        return worklogDetails;
    }

    @Transactional
    public List<TmsInput> getTmsFormat(String token, TempoRequest tempoRequest) {
        WorklogResponse[] worklogResponses = tempoService.getUserDataFromTempo(
                token,
                tempoRequest.getFrom(),
                tempoRequest.getTo(),
                tempoRequest.getWorker());
        List<TmsInput> tmsInputList = convertToTmsInput(token, worklogResponses);
        //deletePreviousTmsEntries(tempoRequest.getWorker(), tempoRequest.getFrom(), tempoRequest.getTo());
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

    public List<TmsInput> getAllFromDb() {
        List<TmsData> tmsDataList = tmsDataRepository.findAll();
        List<TmsInput> tmsInputList = new ArrayList<>();
        for(TmsData tmsData: tmsDataList) {
            tmsInputList.add(modelMapper.map(tmsData, TmsInput.class));
        }
        return tmsInputList;
    }

    public List<TmsInput> convertToTmsInput(String token, WorklogResponse[] worklogResponseList) {
        List<TmsInput> tmsInputList = new ArrayList<>();
        for(WorklogResponse worklogResponse : worklogResponseList) {
            tmsInputList.add(convertWorklogDetailsToTmsInput(token, worklogResponse));
        }
        return tmsInputList;
    }

    public TmsInput convertWorklogDetailsToTmsInput (String token, WorklogResponse worklogResponse) {
        TmsInput tmsInput = new TmsInput();
        tmsInput.setRollNo(worklogResponse.getWorker());
        int issueId = worklogResponse.getIssue().getId();
        JiraConfig.ProjectDetails issueDetails = tempoService.getIssueDetails(token, issueId);
        tmsInput.setProjectName(issueDetails.getProjectName());
        tmsInput.setActivityName("");
        tmsInput.setBillableType("Non-Billable");
        tmsInput.setLocationIndicator("Offshore");
        tmsInput.setDateOfEntry(convertDateToTmsFormat(worklogResponse.getStarted()));
        tmsInput.setEffort((float) worklogResponse.getTimeSpentSeconds()/(SIXTY * SIXTY));
        tmsInput.setComments("");
        tmsInput.setTmsProjectName(issueDetails.getProjectName());
        tmsInput.setTmsActivityName("CODE-DEVELOP");
        tmsInput.setPurchaseId("CUT01");
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
}
