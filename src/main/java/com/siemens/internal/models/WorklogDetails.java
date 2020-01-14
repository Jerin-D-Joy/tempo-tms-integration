package com.siemens.internal.models;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@RequiredArgsConstructor
public class WorklogDetails {

    @NonNull private float timeSpent;
    @NonNull private Date date;
    @NonNull private String issueKey;
    @NonNull private int projectId;
    @NonNull private String projectName;
}
