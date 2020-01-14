package com.siemens.internal.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

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

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss.SSS")
    Date started;
    String originTaskId;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss.SSS")
    Date dateUpdated;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss.SSS")
    Date dateCreated;
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
