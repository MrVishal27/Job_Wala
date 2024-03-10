package com.NaukriChowk.Job_Wala.service;

import com.NaukriChowk.Job_Wala.dto.JobDetailDto;
import com.NaukriChowk.Job_Wala.model.JobDetail;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface JobPostService {

    List<JobDetailDto> getAllJobPost();

    String addNewJobPost(JobDetailDto jobdetaildto);

    JobDetailDto  getJobByJobId(Long jobId);


    JobDetailDto postNewJob(JobDetailDto jobPost);

     void deleteJobPost(Long jobId);

     List<JobDetailDto> getJobByCompanyName(String companyName);

     List<JobDetailDto> getJobByCompanyId(Long id);
     void updateJob(JobDetailDto jobDetailDto);
}
