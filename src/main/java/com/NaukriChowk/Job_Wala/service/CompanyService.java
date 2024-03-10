package com.NaukriChowk.Job_Wala.service;

import com.NaukriChowk.Job_Wala.dto.CompanyDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface CompanyService {

     CompanyDto addCompany(CompanyDto companyDto);

     CompanyDto getCompany(Long companyId);
     List<CompanyDto> getAllCompany();

     void updateCompanyDetails(Long companyId, CompanyDto companyDetail);

     void deleteCompany(Long companyId);
}
