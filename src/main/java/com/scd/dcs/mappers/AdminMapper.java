package com.scd.dcs.mappers;

import com.scd.dcs.domains.vos.UserProperty;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AdminMapper {
     UserProperty selectUserProperty(@Param("email")String email, @Param("workDate") String workDate);
}
