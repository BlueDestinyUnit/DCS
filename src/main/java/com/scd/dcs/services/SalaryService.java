package com.scd.dcs.services;

import com.scd.dcs.domains.entities.PaymentEntity;
import com.scd.dcs.domains.entities.UserEntity;
import com.scd.dcs.domains.vos.UserProperty;
import com.scd.dcs.mappers.AdminMapper;
import com.scd.dcs.mappers.SalaryMapper;
import com.scd.dcs.mappers.UserMapper;
import com.scd.dcs.mappers.WorkMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SalaryService {
    private final SalaryMapper salaryMapper;

    @Autowired
    public SalaryService(SalaryMapper salaryMapper) {
        this.salaryMapper = salaryMapper;
    }
    public PaymentEntity[] selectPayment(String date) {
        this.salaryMapper.selectPayment(date);
        // 1. 모든 유저 가져오기
        // 2. 유저들의 출석 여부 판단
        //
        // 3. 2.번으로 판단한 유저들의 작업여부 판단
        // 4. 3.번 유저들의 급여 계산

        return null;
    }

}
