package com.scd.dcs.mappers;

import com.scd.dcs.domains.vos.UserProperty;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestParam;

@Mapper
public interface AdminMapper {
    UserProperty selectUserProperty(@Param("email") String email,
                                    @Param("date") String date,
                                    @Param("firstDate") String firstDate,
                                    @Param("secondDate") String secondDate);

}
