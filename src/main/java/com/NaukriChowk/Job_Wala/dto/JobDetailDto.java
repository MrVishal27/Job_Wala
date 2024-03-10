package com.NaukriChowk.Job_Wala.dto;

import com.NaukriChowk.Job_Wala.model.Company;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class JobDetailDto {

    private Long jobId;
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
    private Company companyDetails;
}
