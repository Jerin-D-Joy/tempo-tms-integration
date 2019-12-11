package com.siemens.internal.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TmsInput {

    private String rollNo;
    private String projectName;
    private String activityName;
    private String billableType;
    private String locationIndicator;
    private String dateOfEntry;
    private float effort;
    private String comments;
    private String tmsProjectName;
    private String tmsActivityName;
    private String purchaseId;

}
