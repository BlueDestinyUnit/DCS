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
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
public class UserService {
    private final UserMapper userMapper;

    @Autowired
    public UserService(UserMapper userMapper){
        this.userMapper = userMapper;
    }

    public Result<?> login(UserEntity user) {
        if (user == null ||
                !UserRegex.email.tests(user.getEmail()) ||
                !UserRegex.password.tests(user.getPassword())) {
            System.out.println("1");
            return CommonResult.FAILURE;
        }
        UserEntity dbUser = this.userMapper.selectUserByEmail(user.getEmail());
        if (dbUser == null) {
            System.out.println("2");
            return CommonResult.FAILURE;
        }
        if (!BCrypt.checkpw(user.getPassword(), dbUser.getPassword())) {
            System.out.println("3");
            return CommonResult.FAILURE;
        }
        user.setPassword(dbUser.getPassword());
        user.setNickname(dbUser.getNickname());
        return CommonResult.SUCCESS;
    }

    @Transactional
    public Result<?> register(UserEntity user){
        if (user == null || !UserRegex.email.tests(user.getEmail()) || !UserRegex.password.tests(user.getPassword()) || !UserRegex.nickname.tests(user.getNickname())){
            System.out.println("1");
            System.out.println(user);
            return CommonResult.FAILURE;
        }
        if(this.userMapper.selectUserByEmail(user.getEmail()) != null){
            System.out.println(3);
            return RegisterResult.FAILURE_DUPLICATE_EMAIL;
        }
        if(this.userMapper.selectUserByNickname(user.getNickname()) != null){
            System.out.println(4);
            return RegisterResult.FAILURE_DUPLICATE_NICKNAME;
        }
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        System.out.println(user);
        return this.userMapper.insertUser(user) > 0
                ? CommonResult.SUCCESS
                : CommonResult.FAILURE;
    }

//    @Transactional
//    public Result<?> register2(EmailAuthEntity emailAuth,
//                              UserEntity user){
//        if (user == null || !EmailAuthRegex.email.tests(emailAuth.getEmail()) || !EmailAuthRegex.code.tests(emailAuth.getCode()) || !EmailAuthRegex.salt.tests(emailAuth.getSalt()) || !UserRegex.email.tests(user.getEmail()) || !UserRegex.password.tests(user.getPassword()) || !UserRegex.nickname.tests(user.getNickname())){
//            System.out.println("1");
//            return CommonResult.FAILURE;
//        }
//        EmailAuthEntity dbEmailAuth = this.userMapper.selectEmailAuthByEmailCodeSalt(
//                emailAuth.getEmail(),
//                emailAuth.getCode(),
//                emailAuth.getCode());
//        if (dbEmailAuth == null || dbEmailAuth.isExpired() || !dbEmailAuth.isVerified() || dbEmailAuth.isUsed()){
//            System.out.println(2);
//            return CommonResult.FAILURE;
//        }
//        if(this.userMapper.selectUserByEmail(user.getEmail()) != null){
//            System.out.println(3);
//            return RegisterResult.FAILURE_DUPLICATE_EMAIL;
//        }
//        if(this.userMapper.selectUserByNickname(user.getNickname()) != null){
//            System.out.println(4);
//            return RegisterResult.FAILURE_DUPLICATE_NICKNAME;
//        }
//        dbEmailAuth.setUsed(true);
//        this.userMapper.updateEmailAuth(dbEmailAuth);
//        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
//        return this.userMapper.insertUser(user) > 0
//                ? CommonResult.SUCCESS
//                : CommonResult.FAILURE;
//    }

    public Result<?> resetPassword(UserEntity resetUser, String newPassword){
        UserEntity dbUser = this.userMapper.selectUserByEmail(resetUser.getEmail());
        if(dbUser == null){
            return CommonResult.FAILURE;
        }
        dbUser.setPassword(newPassword);
        return this.userMapper.updateUser(dbUser) == 1
                ? CommonResult.SUCCESS
                : CommonResult.FAILURE;
    }

    public Result<?> findEmail(UserEntity findEmailUser){
        UserEntity dbUser = this.userMapper.selectUserByNickname(findEmailUser.getNickname());
        if(dbUser == null){
            return CommonResult.FAILURE;
        }
        if(!dbUser.getNickname().equals(findEmailUser.getNickname())){
            return CommonResult.FAILURE;
        }
        findEmailUser.setEmail(dbUser.getEmail());
        return CommonResult.SUCCESS;
    }


}
