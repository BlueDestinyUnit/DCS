package com.scd.dcs.mappers;

import com.scd.dcs.domains.entities.EmailAuthEntity;
import com.scd.dcs.domains.entities.UserEntity;
import com.scd.dcs.domains.vos.UserPaymentVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.parameters.P;

@Mapper
public interface UserMapper {
    int insertEmailAuth(EmailAuthEntity emailAuth);

    int insertUser(UserEntity user);

    UserEntity selectUserByEmail(@Param("email") String email);

    UserEntity selectUserByName(@Param("name") String name);

    UserEntity selectUserByNickname(@Param(value = "nickname") String nickname);

    UserEntity selectUserByTel(@Param("tel") String tel);


    EmailAuthEntity selectEmailAuthByEmailCodeSalt(@Param("email") String email,
                                                   @Param("code") String code,
                                                   @Param("salt") String salt);
    int updateEmailAuth(EmailAuthEntity emailAuth);

    int updateUser(UserEntity user);

    UserEntity[] selectUsers();

    UserPaymentVo selectUserPayment(@Param("email") String email,
                                      @Param("date") String date);
}
