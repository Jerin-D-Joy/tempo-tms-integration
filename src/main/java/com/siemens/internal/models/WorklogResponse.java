package com.siemens.internal.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WorklogResponse {

    String timeSpent;
    String comment;
    Issue issue;
    int timeSpentSeconds;
    int tempoWorklogId;
    int billableSeconds;
    int originId;
    String started;
    String originTaskId;
    String dateUpdated;
    String dateCreated;
    String worker;
    String updater;

    @Getter
    @Setter
    public static class Issue {
        String key;
        int id;
        int projectId;
        String projectKey;
        String issueType;
        String iconUrl;
        String summary;
        String issueStatus;
        boolean internalIssue;
        String reporterKey;
    }

}
