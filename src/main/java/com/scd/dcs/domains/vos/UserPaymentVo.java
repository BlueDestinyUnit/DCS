package com.scd.dcs.domains.vos;

import com.scd.dcs.domains.entities.UserEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserPaymentVo extends UserEntity {
    private int workDays;
    private double payment;
    private int workCount;
}
