package com.scd.dcs.mappers;

import com.scd.dcs.domains.entities.PaymentEntity;
import com.scd.dcs.domains.vos.PaymentVo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SalaryMapper {
    PaymentEntity[] selectPayment(PaymentVo[] work);
}
