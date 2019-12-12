package com.siemens.internal.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@ConfigurationProperties("jira")
@Getter
@Setter
public class JiraConfig {

    private String projectUrl;
    private String issueUrl;
    private String tempoUrl;
    private String teamUrl;
    private Map<Integer, ProjectDetails> projects = new HashMap<>();

    @Getter
    @Setter
    public static class ProjectDetails {
        private String issueName;
        private String projectName;

    }

}
