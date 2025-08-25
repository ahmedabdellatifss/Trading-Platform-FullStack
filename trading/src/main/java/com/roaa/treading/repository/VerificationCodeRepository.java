package com.roaa.treading.repository;

import com.roaa.treading.entity.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Long> {

    VerificationCode findByUserId(Long userId);
}
