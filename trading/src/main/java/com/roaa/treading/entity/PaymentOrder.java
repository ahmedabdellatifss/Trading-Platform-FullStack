package com.roaa.treading.entity;

import com.roaa.treading.enums.PaymentMethod;
import com.roaa.treading.enums.PaymentOrderStatus;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class PaymentOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long paymobOrderId;

    private Long amount;

    @Enumerated(EnumType.STRING)
    private PaymentOrderStatus status;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @ManyToOne
    private User user;
}
