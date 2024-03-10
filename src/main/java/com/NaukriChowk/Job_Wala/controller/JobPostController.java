package com.NaukriChowk.Job_Wala.controller;

import com.NaukriChowk.Job_Wala.dto.JobDetailDto;
import com.NaukriChowk.Job_Wala.service.JobPostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/job")
@RequiredArgsConstructor
public class JobPostController {

    @Autowired
    private JobPostService jobPostService;

    // Get all the jobs
    @GetMapping("/all_jobs")
    public ResponseEntity<?> getAllPostedJobs(){
        try{
            List<JobDetailDto> allJobs= this.jobPostService.getAllJobPost();
            return new ResponseEntity<>(allJobs, HttpStatus.OK);
        }
        catch (Exception e) {
            // Handle constraint violation (duplicate company name)
            Map<String, String> response = new HashMap<>();
            response.put("error", "No details are there.");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        // return ResponseEntity.ok(this.jobPostService.getAllJobPost());
    }

    // Add a new job post
    @PostMapping("/add-job")
    private ResponseEntity<?> addJobPost( @Valid @RequestBody JobDetailDto jobDetailDto){

        try {
            this.jobPostService.addNewJobPost(jobDetailDto);
            return new ResponseEntity<>("New Job posted successfuly", HttpStatus.CREATED);
        }
        catch (Exception e) {
            // Handle constraint violation (duplicate company name)
            Map<String, String> response = new HashMap<>();
            response.put("Error", "Error in creating new job post.");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    // Get job post by job id
    @GetMapping("/{jobId}")
    public ResponseEntity<?> getJobByJobId(@PathVariable("jobId") Long jobId){

        try{
            JobDetailDto jd=this.jobPostService.getJobByJobId(jobId);
            return new ResponseEntity<>(jd,HttpStatus.ACCEPTED);
        }

        catch(Exception e){
            Map<String, String> response = new HashMap<>();
            response.put("error", "Job details are not present.");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

    }

    // Find all the jobs by comapny name

    @GetMapping(path="/by_company_name/{companyName}")
    public ResponseEntity<?> getJobByCompanyName(@PathVariable String companyName){
        try{
            List<JobDetailDto> allJobs= this.jobPostService.getJobByCompanyName(companyName);
            if(allJobs.size()==0){ throw new Exception();}
            return new ResponseEntity<>(allJobs,HttpStatus.OK);
        }

        catch(Exception e){
            Map<String, String> response = new HashMap<>();
            response.put("Success", " No Job details are not present for this company.");
            return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
        }

    }

    // Find jobs by company Id
    @GetMapping(path = "/by_company_id/{id}")
    public ResponseEntity<?> getJobByCompanyId(@PathVariable Long id){
        try{
            List<JobDetailDto> allJobs= this.jobPostService.getJobByCompanyId(id);
            if(allJobs.size()==0){ throw new Exception();}
            return new ResponseEntity<>(allJobs,HttpStatus.OK);
        }

        catch(Exception e){
            Map<String, String> response = new HashMap<>();
            response.put("Success", " No Job details are not present for this company.");
            return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
        }
    }



    // update the job details by job id

    @PutMapping(path = "/update_job/{jobId}")
    public ResponseEntity<?> updateJobDetails(@PathVariable Long jobId, @RequestBody JobDetailDto job)
    {
        try{
            JobDetailDto jobDetail=this.jobPostService.getJobByJobId(jobId);
            //  jobDetail.setCompanyId(job.getCompanyDetails().getCompanyId());
            jobDetail.setCompanyJobId(job.getCompanyJobId());
            jobDetail.setJobDesignation(job.getJobDesignation());
            jobDetail.setJobDescription(job.getJobDescription());
            jobDetail.setJobUrl(job.getJobUrl());
            jobDetail.setJobTitle(job.getJobTitle());
            jobDetail.setJobLocation(job.getJobLocation());
            jobDetail.setCompanyName(job.getCompanyName());
            jobDetail.setCtc(job.getCtc());
            jobDetail.setSkills(job.getSkills());
            jobDetail.setYearsOfExperience(job.getYearsOfExperience());


            this.jobPostService.updateJob(jobDetail);
            Map<String, String> response = new HashMap<>();
            response.put("Success", "Job details updated.");
            return new ResponseEntity<>(response,HttpStatus.ACCEPTED);

        }
        catch (Exception e){
            Map<String,String> response=new HashMap<>();
            response.put("Error", "Error in updating the job");
            return new ResponseEntity<>(response,HttpStatus.ACCEPTED);
        }
    }

    /// Delete the job post
    @DeleteMapping(path = "/delete/{jobId}")
    public ResponseEntity<?> deleteJobByJobId(@PathVariable Long jobId){
        try{
            this.jobPostService.deleteJobPost(jobId);
            Map<String, String> response = new HashMap<>();
            response.put("Success", "Job details deleted.");
            return new ResponseEntity<>(response,HttpStatus.ACCEPTED);
        }
        catch(Exception e){
            Map<String, String> response = new HashMap<>();
            response.put("error", "Errors in deleteing the job .");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

    }
}
