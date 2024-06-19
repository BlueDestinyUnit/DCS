package com.scd.dcs.mappers;

import com.scd.dcs.domains.entities.PaymentEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SalaryMapper {
    PaymentEntity selectPayment(@Param("date") String date);
}
