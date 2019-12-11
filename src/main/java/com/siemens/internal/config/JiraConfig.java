package com.siemens.internal.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@ConfigurationProperties("jira")
public class JiraConfig {

    private String projectUrl;
    private String issueUrl;
    private String tempoUrl;
    private Map<Integer, ProjectDetails> projects = new HashMap<>();

    public static class ProjectDetails {
        private String issueName;
        private String projectName;

        public String getIssueName() {
            return issueName;
        }

        public void setIssueName(String issueName) {
            this.issueName = issueName;
        }

        public String getProjectName() {
            return projectName;
        }

        public void setProjectName(String projectName) {
            this.projectName = projectName;
        }

    }

    public String getTempoUrl() {
        return tempoUrl;
    }

    public void setTempoUrl(String tempoUrl) {
        this.tempoUrl = tempoUrl;
    }

    public String getProjectUrl() {
        return projectUrl;
    }

    public void setProjectUrl(String projectUrl) {
        this.projectUrl = projectUrl;
    }

    public String getIssueUrl() {
        return issueUrl;
    }

    public void setIssueUrl(String issueUrl) {
        this.issueUrl = issueUrl;
    }

    public Map<Integer, ProjectDetails> getProjects() {
        return projects;
    }

    public void setProjects(Map<Integer, ProjectDetails> projects) {
        this.projects = projects;
    }
}
