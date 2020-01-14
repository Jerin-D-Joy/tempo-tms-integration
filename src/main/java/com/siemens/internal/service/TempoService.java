package com.siemens.internal.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.siemens.internal.common.Constants;
import com.siemens.internal.config.JiraConfig;
import com.siemens.internal.models.TempoRequest;
import com.siemens.internal.models.WorklogResponse;
import com.siemens.internal.utils.RestClient;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
@Getter
@Setter
public class TempoService {

    @Autowired
    private JiraConfig jiraConfig;

    @Autowired
    private RestClient restClient;

    private String username;
    private String password;

    public WorklogResponse[] getUserDataFromTempo(String startDate, String endDate, String[] userNames) {
        WorklogResponse[] worklogResponses = null;
        TempoRequest reqBody = new TempoRequest(startDate, endDate, userNames);
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
       // System.out.println(username + " : " + password);
        headers.setBasicAuth(username, password);
        HttpEntity<TempoRequest> entity = new HttpEntity<>(reqBody,headers);
        ResponseEntity<WorklogResponse[]> response = restClient.getRestTemplate().exchange(jiraConfig.getTempoUrl(),
                HttpMethod.POST,
                entity, WorklogResponse[].class);
        if(response!=null && response.getStatusCode() == HttpStatus.OK) {
            worklogResponses = response.getBody();
        }
        return worklogResponses;
    }

    public JiraConfig.ProjectDetails getProjectDetails(int projectId) {
        JiraConfig.ProjectDetails projectDetails = null;
        if(jiraConfig.getProjects().containsKey(projectId)) {
            projectDetails = jiraConfig.getProjects().get(projectId);
        } else {
            projectDetails = getProjectFromJira(projectId);
            jiraConfig.getProjects().put(projectId, projectDetails);
        }
        return projectDetails;
    }

    public JiraConfig.ProjectDetails getProjectFromJira(int projectId) {
        JiraConfig.ProjectDetails projectDetails = new JiraConfig.ProjectDetails();
        String url = jiraConfig.getProjectUrl() + projectId;
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(username, password);
        HttpEntity<String> entity = new HttpEntity<String>(null,headers);
        ResponseEntity<String> response = restClient.getRestTemplate().exchange(
                url, HttpMethod.GET,
                entity, String.class);
        if(response.getStatusCode() == HttpStatus.OK) {
            String result = response.getBody();
            JsonNode projectData = restClient.parseJsonAsJsonNode(result);
            projectDetails.setProjectName(projectData.get("name").asText());
            String description = projectData.get("description").asText();
            if(!StringUtils.isEmpty(description) && description.startsWith(Constants.PO_PREFIX)) {
                projectDetails.setBillableType(Constants.BILLABLE);
                projectDetails.setPurchaseOrderId(extractPurchaseOrderIdFromDescription(description));
                projectDetails.setTmsActivityName(Constants.CODE_DEVELOP);
            } else {
                projectDetails.setBillableType(Constants.NON_BILLABLE);
                projectDetails.setTmsActivityName(Constants.COM_LEAVE);
                projectDetails.setPurchaseOrderId("");
            }
        }
        return projectDetails;
    }

    private String extractPurchaseOrderIdFromDescription(String description) {
        String purchaseOrderId = description.split("\\r+|\\n+", 2)[0].substring(3);
        return purchaseOrderId;
    }

    public JiraConfig.ProjectDetails getIssueDetails(int issueId) {
        JiraConfig.ProjectDetails projectDetails = null;
        if(jiraConfig.getProjects().containsKey(issueId)) {
            projectDetails = jiraConfig.getProjects().get(issueId);
        } else {
            projectDetails = getIssueFromJira(issueId);
            jiraConfig.getProjects().put(issueId, projectDetails);
        }
        return projectDetails;
    }

    public JiraConfig.ProjectDetails getIssueFromJira(int issueId) {
        JiraConfig.ProjectDetails projectDetails = new JiraConfig.ProjectDetails();
        String url = jiraConfig.getIssueUrl() + issueId;
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(username, password);
        HttpEntity<String> entity = new HttpEntity<String>(null,headers);
        ResponseEntity<String> response = restClient.getRestTemplate().exchange(
                url, HttpMethod.GET,
                entity, String.class);
        if(response.getStatusCode() == HttpStatus.OK) {
            String result = response.getBody();
            JsonNode issueData = restClient.parseJsonAsJsonNode(result);
            //projectDetails.setIssueName(issueData.get("fields").get("summary").asText());
            projectDetails.setProjectName(issueData.get("fields").get("project").get("name").asText());
            String description = issueData.get("fields").get("description").asText();
            if(description.startsWith(Constants.PO_PREFIX)) {
                projectDetails.setBillableType(Constants.BILLABLE);
                projectDetails.setPurchaseOrderId(description.split("\\n")[0].substring(3));
                projectDetails.setTmsActivityName(Constants.CODE_DEVELOP);
            } else {
                projectDetails.setBillableType(Constants.NON_BILLABLE);
                projectDetails.setTmsActivityName(Constants.COM_LEAVE);
                projectDetails.setPurchaseOrderId("");
            }
        }
        return projectDetails;
    }

    public List<String> getTeamMembersFromTempo(int teamId) {
        String url = jiraConfig.getTeamUrl().replaceAll("team_id", String.valueOf(teamId));
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<String>(null,headers);
        ResponseEntity<String> response = restClient.getRestTemplate().exchange(
                url, HttpMethod.GET,
                entity, String.class);
        if(response.getStatusCode() == HttpStatus.OK) {
            String result = response.getBody();
            JsonNode teamData = restClient.parseJsonAsJsonNode(result);
            List<String> members = teamData.findValuesAsText("member.name");
            System.out.println(members.get(2));
            //teamData.get("fields").get("summary").asText());
            //teamData.get("fields").get("project").get("name").asText();
        }
        return null;
    }

}
