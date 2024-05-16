package com.scd.dcs.services;

import com.scd.dcs.domains.entities.EmailAuthEntity;
import com.scd.dcs.domains.entities.UserEntity;
import com.scd.dcs.mappers.UserMapper;
import com.scd.dcs.regexes.EmailAuthRegex;
import com.scd.dcs.regexes.UserRegex;
import com.scd.dcs.results.CommonResult;
import com.scd.dcs.results.Result;
import com.scd.dcs.results.user.RegisterResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class UserService {
    private final UserMapper userMapper;

    @Autowired
    public UserService(UserMapper userMapper){
        this.userMapper = userMapper;
    }

    @Transactional
    public Result<?> register(EmailAuthEntity emailAuth,
                              UserEntity user){
        if (emailAuth == null || user == null || !EmailAuthRegex.email.tests(emailAuth.getEmail()) || !EmailAuthRegex.code.tests(emailAuth.getCode()) || !EmailAuthRegex.salt.tests(emailAuth.getSalt()) || !UserRegex.email.tests(user.getEmail()) || !UserRegex.password.tests(user.getPassword())){
            return CommonResult.FAILURE;
        }
        EmailAuthEntity dbEmailAuth = this.userMapper.selectEmailAuthByEmailCodeSalt(
                emailAuth.getEmail(),
                emailAuth.getCode(),
                emailAuth.getCode());
        if (dbEmailAuth == null || dbEmailAuth.isExpired() || !dbEmailAuth.isVerified() || dbEmailAuth.isUsed()){
            return CommonResult.FAILURE;
        }
        if(this.userMapper.selectUserByEmail(user.getEmail()) != null){
            return RegisterResult.FAILURE_DUPLICATE_EMAIL;
        }
        dbEmailAuth.setUsed(true);
        this.userMapper.updateEmailAuth(dbEmailAuth);
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
//        user.setCreatedAt(LocalDateTime.now());
//        user.setAdmin(false);
//        user.setDeleted(false);
//        user.setSuspended(false);
        return this.userMapper.insertUser(user) > 0
                ? CommonResult.SUCCESS
                : CommonResult.FAILURE;

    }
}
