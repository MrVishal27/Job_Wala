package com.NaukriChowk.Job_Wala.repo;

import com.NaukriChowk.Job_Wala.model.OtpEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OtpRepository extends JpaRepository<OtpEntity,Long> {

    Optional<OtpEntity> findByEmail(String email);
}
