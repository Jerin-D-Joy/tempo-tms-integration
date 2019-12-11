package com.siemens.internal.models;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class WorklogDetails {

    @NonNull private float timeSpent;
    @NonNull private String date;
    @NonNull private String issueKey;
    @NonNull private int projectId;
    @NonNull private String issueName;
    @NonNull private String projectName;
}
