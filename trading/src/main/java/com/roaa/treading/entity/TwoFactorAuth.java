package com.roaa.treading.entity;

import com.roaa.treading.enums.VerificationType;
import lombok.Data;

@Data
public class TwoFactorAuth {
    private boolean isEnable = false;
    private VerificationType sendTo;
}
