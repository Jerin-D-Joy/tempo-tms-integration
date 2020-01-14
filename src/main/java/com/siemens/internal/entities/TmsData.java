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
import java.util.Date;

@Getter
@Setter
@ToString
@Entity
@Table(name = "ISETMSData")
@IdClass(TmsId.class)
public class TmsData extends BaseEntity {

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
    private Date dateOfEntry;

    @Column(name = "Effort")
    private float effort;

    @Column(name = "Comments")
    private String comments;

    @Column(name = "TMSProjectName")
    private String tmsProjectName;

    @Column(name = "TMSActivityName")
    private String tmsActivityName;

    @Column(name = "PO_ID")
    private String purchaseOrderId;

    @Column(name = "Active")
    private int active;
}
