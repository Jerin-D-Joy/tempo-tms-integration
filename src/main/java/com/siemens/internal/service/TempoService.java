package com.siemens.internal.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.siemens.internal.config.JiraConfig;
import com.siemens.internal.models.TempoRequest;
import com.siemens.internal.models.WorklogResponse;
import com.siemens.internal.utils.RestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class TempoService {

    @Autowired
    private JiraConfig jiraConfig;

    @Autowired
    private RestClient restClient;

    public WorklogResponse[] getUserDataFromTempo(String token, String startDate, String endDate, String[] userNames) {
        WorklogResponse[] worklogResponses = null;
        TempoRequest reqBody = new TempoRequest(startDate, endDate, userNames);
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.set("Authorization", token);
        HttpEntity<TempoRequest> entity = new HttpEntity<>(reqBody,headers);
        ResponseEntity<WorklogResponse[]> response = restClient.getRestTemplate().exchange(jiraConfig.getTempoUrl(),
                HttpMethod.POST,
                entity, WorklogResponse[].class);
        if(response!=null && response.getStatusCode() == HttpStatus.OK) {
            worklogResponses = response.getBody();
        }
        return worklogResponses;
    }


    public String getProjectFromTempo(String token, int projectId) {
        String url = jiraConfig.getProjectUrl() + projectId;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        HttpEntity<String> entity = new HttpEntity<String>(null,headers);
        String result = restClient.getRestTemplate().exchange(
                url, HttpMethod.GET,
                entity, String.class).getBody();
        System.out.println(result);
        return result;
    }

    public JiraConfig.ProjectDetails getIssueDetails(String token, int issueId) {
        JiraConfig.ProjectDetails projectDetails = null;
        if(jiraConfig.getProjects().containsKey(issueId)) {
            projectDetails = jiraConfig.getProjects().get(issueId);
        } else {
            projectDetails = getIssueFromTempo(token, issueId);
            jiraConfig.getProjects().put(issueId, projectDetails);
        }
        return projectDetails;
    }

    public JiraConfig.ProjectDetails getIssueFromTempo(String token, int issueId) {
        JiraConfig.ProjectDetails projectDetails = new JiraConfig.ProjectDetails();
        String url = jiraConfig.getIssueUrl() + issueId;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        HttpEntity<String> entity = new HttpEntity<String>(null,headers);
        ResponseEntity<String> response = restClient.getRestTemplate().exchange(
                url, HttpMethod.GET,
                entity, String.class);
        if(response.getStatusCode() == HttpStatus.OK) {
            String result = response.getBody();
            JsonNode issueData = restClient.parseJsonAsJsonNode(result);
            projectDetails.setIssueName(issueData.get("fields").get("summary").asText());
            projectDetails.setProjectName(issueData.get("fields").get("project").get("name").asText());
        }
        return projectDetails;
    }

    public List<String> getTeamMembersFromTempo(String token, int teamId) {
        String url = jiraConfig.getTeamUrl().replaceAll("team_id", String.valueOf(teamId));
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
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
