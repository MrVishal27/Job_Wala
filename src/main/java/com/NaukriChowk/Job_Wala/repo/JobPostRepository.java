package com.NaukriChowk.Job_Wala.repo;

import com.NaukriChowk.Job_Wala.model.JobDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobPostRepository extends JpaRepository<JobDetail,Long> {

     List<JobDetail> findJobsByCompanyName(String companyName);
     List<JobDetail> findJobsByCompanyDetailsCompanyId(Long id);
}
