package com.scd.dcs.domains.vos;

import com.scd.dcs.domains.entities.WorkEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PaymentVo extends WorkEntity {
    private int workDays;
    private double payment;
    private String workType;
    private String userName;
    private boolean isInsurance;
}
