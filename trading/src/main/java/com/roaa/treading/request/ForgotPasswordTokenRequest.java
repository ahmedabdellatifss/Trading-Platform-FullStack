package com.roaa.treading.request;

import com.roaa.treading.domain.VerificationType;
import lombok.Data;

@Data
public class ForgotPasswordTokenRequest {

    private String sendTo;
    private VerificationType verificationType;
}
