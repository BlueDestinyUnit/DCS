package com.scd.dcs.mappers;

import com.scd.dcs.domains.entities.EmailAuthEntity;
import com.scd.dcs.domains.entities.UserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
    int insertEmailAuth(EmailAuthEntity emailAuth);

    int insertUser(UserEntity user);

    UserEntity selectUserByEmail(@Param("email") String email);

    UserEntity selectUserByNickname(@Param(value = "nickname") String nickname);


    EmailAuthEntity selectEmailAuthByEmailCodeSalt(@Param("email") String email,
                                                   @Param("code") String code,
                                                   @Param("salt") String salt);
    int updateEmailAuth(EmailAuthEntity emailAuth);

    int updateUser(UserEntity user);

    UserEntity[] selectUsers();
}
