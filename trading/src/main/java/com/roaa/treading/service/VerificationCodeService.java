package com.roaa.treading.service;

import com.roaa.treading.domain.VerificationType;
import com.roaa.treading.entity.User;
import com.roaa.treading.entity.VerificationCode;
import org.springframework.stereotype.Service;

@Service
public interface VerificationCodeService {

    VerificationCode sendVerificationCode(User user, VerificationType verificationType);

    VerificationCode getVerificationCodeById(Long id) throws Exception;

    VerificationCode getVerificationCodeByUser(Long userId);

    void deleteVerificationCodeById(VerificationCode verificationCode);
}
