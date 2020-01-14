package com.siemens.internal.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class TmsInput {

    private String rollNo;
    private String projectName;
    private String activityName;
    private String billableType;
    private String locationIndicator;
    private Date dateOfEntry;
    private float effort;
    private String comments;
    private String tmsProjectName;
    private String tmsActivityName;
    private String purchaseOrderId;
    private int active;
    private Date createdOn;
}
