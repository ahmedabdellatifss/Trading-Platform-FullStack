package com.roaa.treading.service;


import com.roaa.treading.enums.VerificationType;
import com.roaa.treading.entity.ForgotPasswordToken;
import com.roaa.treading.entity.User;

public interface ForgotPasswordService {

    ForgotPasswordToken createToken(User user, String id, String otp, VerificationType verificationType, String sendTo);

    ForgotPasswordToken findById(String id);

    ForgotPasswordToken findByUser(Long userId);

    void deleteToken(ForgotPasswordToken token);
}
