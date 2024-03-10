package com.NaukriChowk.Job_Wala.controller;

import com.NaukriChowk.Job_Wala.dto.CompanyDto;
import com.NaukriChowk.Job_Wala.dto.JobDetailDto;
import com.NaukriChowk.Job_Wala.exception.ResourceNotFoundException;
import com.NaukriChowk.Job_Wala.model.Company;
import com.NaukriChowk.Job_Wala.service.CompanyService;
import com.NaukriChowk.Job_Wala.service.JobPostService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/company")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;
    private final JobPostService jobPostService;
    private final ModelMapper modelMapper;


    //Get Comapny Detials
    @GetMapping
    public ResponseEntity<?> getAllCompany(){
        try{
            List<CompanyDto> allCompany= this.companyService.getAllCompany();
            return new ResponseEntity<>(allCompany, HttpStatus.OK);
        }
        catch (Exception e) {
            // Handle constraint violation (duplicate company name)
            Map<String, String> response = new HashMap<>();
            response.put("error", "No details are there.");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    // Get company details by company id
    @GetMapping(path = "/{companyId}")
    public ResponseEntity<CompanyDto> getCompanyByID(@PathVariable Long companyId ){
        return new ResponseEntity<>(this.companyService.getCompany(companyId), HttpStatus.FOUND);
    }


    @PostMapping(path = "/add-company")
    public ResponseEntity<?> addNewCompany(@RequestBody CompanyDto companyDto){

        try {
            CompanyDto savedCompany =this. companyService.addCompany(companyDto);
            Map<String, String> response = new HashMap<>();
            response.put("Success", "Company details are added.");
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (DataIntegrityViolationException e) {
            // Handle constraint violation (duplicate company name)
            Map<String, String> response = new HashMap<>();
            response.put("error", "Company name already exists.");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    // Adding new job post within a company

    @PostMapping("/{companyId}/job-posts")
    public ResponseEntity<?> createJobPost(@PathVariable Long companyId,
                                           @RequestBody JobDetailDto jobPost) {
        // Fetch the company from database

        try {
            CompanyDto company = companyService.getCompany(companyId);
            Company com=modelMapper.map(company, Company.class);
            jobPost.setCompanyDetails(com);
            //jobPost.setCompanyId(newCompany.getCompanyId());

            JobDetailDto jd=jobPostService.postNewJob(jobPost);

            return new ResponseEntity<>(jd, HttpStatus.CREATED);
        } catch (ResourceNotFoundException e) {
            // Handle constraint violation (duplicate company name)
            Map<String, String> response = new HashMap<>();
            response.put("error", "Company details are not present.");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    // Update company Details
    @PutMapping(path = "/update_company/{companyId}")
    public ResponseEntity<?> updateCompanyDetails(@PathVariable Long companyId,
                                                  @RequestBody CompanyDto companyDetails){
        try{
            this.companyService.updateCompanyDetails(companyId,companyDetails);
            Map<String, String> response = new HashMap<>();
            response.put("Success", "Company details updated.");
            return new ResponseEntity<>(response,HttpStatus.ACCEPTED);

        }
        catch(Exception e){
            Map<String, String> response = new HashMap<>();
            response.put("Error", " Error in updating Company details .");
            return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
        }
    }

    @DeleteMapping(path = "/delete/{companyId}")
    public ResponseEntity<?> deleteCompany(@PathVariable Long companyId){
        try{
            this.companyService.deleteCompany(companyId);
            Map<String, String> response = new HashMap<>();
            response.put("Success", "Company details deleted.");
            return new ResponseEntity<>(response,HttpStatus.ACCEPTED);
        }
        catch(Exception e){
            Map<String, String> response = new HashMap<>();
            response.put("error", "Company details are not present.");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
}
