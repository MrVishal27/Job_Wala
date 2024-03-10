package com.NaukriChowk.Job_Wala.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "JobDetail", uniqueConstraints = { @UniqueConstraint(columnNames = { "jobId" }) })
public class JobDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long jobId;


    @Column(unique = true)
    private Long companyJobId;

    private String companyName;

    private String jobTitle;

    private String jobDescription;

    private String jobDesignation;

    private String ctc;

    private Integer yearsOfExperience;

    private String jobLocation;

    private String skills;

    private String jobUrl;

    @ManyToOne(fetch = FetchType.EAGER)
    private Company companyDetails;
}
