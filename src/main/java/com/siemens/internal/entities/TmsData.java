package com.siemens.internal.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Getter
@Setter
@ToString
@Entity
@Table(name = "ISETMSData")
@IdClass(TmsId.class)
public class TmsData {

    @Id
    @Column(name = "RollNo")
    private String rollNo;

    @Id
    @Column(name = "ProjectName")
    private String projectName;

    @Column(name = "ActivityName")
    private String activityName;

    @Column(name = "BillableType")
    private String billableType;

    @Column(name = "LocationIndicator")
    private String locationIndicator;

    @Id
    @Column(name = "DateofEntry")
    private String dateOfEntry;

    @Column(name = "Effort")
    private float effort;

    @Column(name = "Comments")
    private String comments;

    @Column(name = "TMSProjectName")
    private String tmsProjectName;

    @Column(name = "TMSActivityName")
    private String tmsActivityName;

    @Column(name = "PO_ID")
    private String purchaseId;
}
