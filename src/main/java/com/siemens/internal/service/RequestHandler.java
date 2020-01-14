package com.siemens.internal.service;

import com.siemens.internal.models.TempoRequest;
import com.siemens.internal.models.TmsInput;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class RequestHandler {

    @Autowired
    private TmsService tmsService;

    @Autowired
    private TempoService tempoService;

    public String handleSyncRequest(Map<String, String> requestMap) {
        StringBuilder sb = new StringBuilder();
        try {
            tempoService.setUsername(requestMap.get("username"));
            tempoService.setPassword(requestMap.get("password"));
            String workersInput = requestMap.get("workers").toLowerCase();
            String[] workers = workersInput.trim().split("\\s*,\\s*");
            TempoRequest tempoRequest = new TempoRequest(requestMap.get("from"), requestMap.get("to"), workers);
            List<TmsInput> errorList = tmsService.getDataFromTempoAndParseToTmsInput(tempoRequest);
            if (CollectionUtils.isEmpty(errorList)) {
                sb.append("Synced all details successfully");
                sb.append(getAllFromDatabase());
            } else {
                sb.append("Failed to sync the below details. Please sync manually \n");
                sb.append(createTableWithTmsInput(errorList));
            }
        } catch(Exception ex) {
            log.error("Exception occured", ex);
            sb.append("Exception occured : ").append(ex);
        }
        return sb.toString();
    }

    public String getAllFromDatabase() {
        return createTableWithTmsInput(tmsService.getAllFromDb());
    }

    public String createTableWithTmsInput(List<TmsInput> tmsInputList) {
        StringBuilder sb = new StringBuilder();
        sb.append("<table style=\"border: 1px solid black\"><tr><th> RollNo </th><th> Project_Name  </th>");
        sb.append("<th> ActivityName </th><th> BillableType </th><th> LocationIndicator </th><th> DateOfEntry </th>");
        sb.append("<th> Effort </th><th> Comments </th><th> TmsProjectName </th><th> TmsActivityName  </th>");
        sb.append("<th> PurchaseOrderId </th><th> Active </th>");
        sb.append("<th> Created On </th></tr>");
        for(TmsInput tmsInput : tmsInputList) {
            sb.append("<tr>");
            sb.append("<td>").append(tmsInput.getRollNo()).append("</td>");
            sb.append("<td>").append(tmsInput.getProjectName()).append("</td>");
            sb.append("<td>").append(tmsInput.getActivityName()).append("</td>");
            sb.append("<td>").append(tmsInput.getBillableType()).append("</td>");
            sb.append("<td>").append(tmsInput.getLocationIndicator()).append("</td>");
            sb.append("<td>").append(tmsInput.getDateOfEntry()).append("</td>");
            sb.append("<td>").append(tmsInput.getEffort()).append("</td>");
            sb.append("<td>").append(tmsInput.getComments()).append("</td>");
            sb.append("<td>").append(tmsInput.getTmsProjectName()).append("</td>");
            sb.append("<td>").append(tmsInput.getTmsActivityName()).append("</td>");
            sb.append("<td>").append(tmsInput.getPurchaseOrderId()).append("</td>");
            sb.append("<td>").append(tmsInput.getActive()).append("</td>");
            //sb.append("<td>").append(tmsInput.getCreatedOn()).append("</td>");
            sb.append("</tr>");
        }
        sb.append("</table>");
        return sb.toString();
    }
}
