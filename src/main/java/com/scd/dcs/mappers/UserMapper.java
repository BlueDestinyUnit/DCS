package com.scd.dcs.mappers;

import com.scd.dcs.domains.entities.UserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
    UserEntity selectUserByEmail(@Param("email") String email);

}
