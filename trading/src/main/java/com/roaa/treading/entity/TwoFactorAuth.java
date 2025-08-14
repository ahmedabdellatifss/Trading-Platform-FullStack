package com.roaa.treading.entity;

import com.roaa.treading.domain.VerificationType;
import lombok.Data;

@Data
public class TwoFactorAuth {
    private boolean isEnable = false;
    private VerificationType sendTo;
}
