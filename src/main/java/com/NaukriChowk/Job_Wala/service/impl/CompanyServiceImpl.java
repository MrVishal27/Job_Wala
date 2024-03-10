package com.NaukriChowk.Job_Wala.service.impl;

import com.NaukriChowk.Job_Wala.dto.CompanyDto;
import com.NaukriChowk.Job_Wala.model.Company;
import com.NaukriChowk.Job_Wala.repo.CompanyRepository;
import com.NaukriChowk.Job_Wala.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {


    private final ModelMapper modelMapper;
    private final CompanyRepository companyRepository;

    @Override
    public CompanyDto addCompany(CompanyDto companyDto) {
        // Convert DTO to entity

        // Company newCompany= CompanyMapper.mapToCompany(companyDto);
        Company newCompany= modelMapper.map(companyDto,Company.class);
        Company c= companyRepository.save(newCompany);

        // COnvert Entity to DTO
        CompanyDto result=modelMapper.map(c,CompanyDto.class);

        return result;
    }

    @Override
    public CompanyDto getCompany(Long companyId) {
        Company c= this.companyRepository.findById(companyId).get();
        return modelMapper.map(c,CompanyDto.class);

    }

    @Override
    public List<CompanyDto> getAllCompany() {
        List<Company> companies=this.companyRepository.findAll();
        return companies.stream().map(company -> modelMapper.map(company,CompanyDto.class)).collect(Collectors.toList());

    }

    @Override
    public void updateCompanyDetails(Long companyId, CompanyDto companyDetail) {
        Company c= this.companyRepository.findById(companyId).get();
        // c.setCompanyName(companyDetail.getCompanyName());
        c.setCompanyName(companyDetail.getCompanyName());
        this.companyRepository.save(c);
    }

    @Override
    public void deleteCompany(Long companyId) {
        Company c=this.companyRepository.findById(companyId).get();
        this.companyRepository.delete(c);
    }
}
