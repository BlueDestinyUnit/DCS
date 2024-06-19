package com.scd.dcs.services;

import com.scd.dcs.domains.entities.AttendanceEntity;
import com.scd.dcs.domains.entities.EmailAuthEntity;
import com.scd.dcs.domains.entities.UserEntity;
import com.scd.dcs.domains.vos.UserProperty;
import com.scd.dcs.mappers.AdminMapper;
import com.scd.dcs.mappers.AttendanceMapper;
import com.scd.dcs.mappers.UserMapper;
import com.scd.dcs.mics.MailSender;
import com.scd.dcs.regexes.EmailAuthRegex;
import com.scd.dcs.regexes.UserRegex;
import com.scd.dcs.results.CommonResult;
import com.scd.dcs.results.Result;
import com.scd.dcs.results.user.RegisterResult;
import com.scd.dcs.results.user.SendRegisterEmailResult;
import com.scd.dcs.results.user.VerifyResult;
import jakarta.mail.MessagingException;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.token.Sha512DigestUtils;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;


@Service
public class UserService {

    private static void prepareEmailAuth(EmailAuthEntity emailAuth) throws NoSuchAlgorithmException {
        emailAuth.setCode(RandomStringUtils.randomNumeric(6));
        emailAuth.setSalt(Sha512DigestUtils.shaHex(String.format("%s%s%f%f",
                emailAuth.getEmail(),
                emailAuth.getCode(),
                SecureRandom.getInstanceStrong().nextDouble(),
                SecureRandom.getInstanceStrong().nextDouble())));
        emailAuth.setCreatedAt(LocalDateTime.now());
        emailAuth.setExpiresAt(LocalDateTime.now().plusMinutes(5));
        emailAuth.setExpired(false);
        emailAuth.setVerified(false);
        emailAuth.setUsed(false);
    }

    private final UserMapper userMapper;
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;
    private final AdminMapper adminMapper;
    private final AttendanceMapper attendanceMapper;

    @Autowired
    public UserService(UserMapper userMapper, JavaMailSender mailSender, SpringTemplateEngine templateEngine, AdminMapper adminMapper, AttendanceMapper attendanceMapper){
        this.userMapper = userMapper;
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
        this.adminMapper = adminMapper;
        this.attendanceMapper = attendanceMapper;
    }

    public Result<?> login(UserEntity user) {
        if (user == null ||
                !UserRegex.email.tests(user.getEmail()) ||
                !UserRegex.password.tests(user.getPassword())) {
            return CommonResult.FAILURE;
        }
        UserEntity dbUser = this.userMapper.selectUserByEmail(user.getEmail());
        if (dbUser == null) {
            return CommonResult.FAILURE;
        }
        if (!BCrypt.checkpw(user.getPassword(), dbUser.getPassword())) {
            return CommonResult.FAILURE;
        }
        user.setPassword(dbUser.getPassword());
        user.setNickname(dbUser.getNickname());
        return CommonResult.SUCCESS;
    }


    @Transactional
    public Result<?> register(EmailAuthEntity emailAuth,
                              UserEntity user){
        if (user == null || !EmailAuthRegex.email.tests(emailAuth.getEmail()) || !EmailAuthRegex.code.tests(emailAuth.getCode()) || !EmailAuthRegex.salt.tests(emailAuth.getSalt()) || !UserRegex.email.tests(user.getEmail()) || !UserRegex.password.tests(user.getPassword()) || !UserRegex.nickname.tests(user.getNickname())){
            return CommonResult.FAILURE;
        }
        EmailAuthEntity dbEmailAuth = this.userMapper.selectEmailAuthByEmailCodeSalt(
                emailAuth.getEmail(),
                emailAuth.getCode(),
                emailAuth.getSalt());
        if (dbEmailAuth == null || dbEmailAuth.isExpired() || !dbEmailAuth.isVerified() || dbEmailAuth.isUsed()){
            System.out.println(dbEmailAuth);
            return CommonResult.FAILURE;
        }
        if(this.userMapper.selectUserByEmail(user.getEmail()) != null){
            return RegisterResult.FAILURE_DUPLICATE_EMAIL;
        }
        if(this.userMapper.selectUserByName(user.getName()) != null){
            return RegisterResult.FAILURE_DUPLICATE_NAME;
        }
        if(this.userMapper.selectUserByNickname(user.getNickname()) != null) {
            return RegisterResult.FAILURE_DUPLICATE_NICKNAME;
        }
        if(this.userMapper.selectUserByTel(user.getTel()) != null){
            return RegisterResult.FAILURE_DUPLICATE_TEL;
        }
        dbEmailAuth.setUsed(true);
        this.userMapper.updateEmailAuth(dbEmailAuth);
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        return this.userMapper.insertUser(user) > 0
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

    @Transactional
    public Result<?> sendRegisterEmail(EmailAuthEntity emailAuth) throws NoSuchAlgorithmException, MessagingException {
        if(emailAuth == null || !EmailAuthRegex.email.tests(emailAuth.getEmail())){
            return CommonResult.FAILURE;
        }
        if(this.userMapper.selectUserByEmail(emailAuth.getEmail()) != null){
            return SendRegisterEmailResult.FAILURE_DUPLICATE_EMAIL;
        }
        prepareEmailAuth(emailAuth);
        if(this.userMapper.insertEmailAuth(emailAuth) != 1){
            return CommonResult.FAILURE;
        }
        Context context = new Context();
        context.setVariable("code", emailAuth.getCode());
        new MailSender(this.mailSender)
                .setFrom("gyust941326@gmail.com")
                .setSubject("[맛집] 회원가입 인증번호")
                .setText(this.templateEngine.process("user/registerEmail", context), true)
                .setTo(emailAuth.getEmail())
                .send();
        return CommonResult.SUCCESS;
    }

    public Result<?> verifyEmailAuth(EmailAuthEntity emailAuth){
        if(emailAuth == null || !EmailAuthRegex.email.tests(emailAuth.getEmail()) || !EmailAuthRegex.code.tests(emailAuth.getCode()) || !EmailAuthRegex.salt.tests(emailAuth.getSalt())){
            return CommonResult.FAILURE;
        }
        EmailAuthEntity dbEmailAuth = this.userMapper.selectEmailAuthByEmailCodeSalt(
                emailAuth.getEmail(),
                emailAuth.getCode(),
                emailAuth.getSalt());
        if(dbEmailAuth == null){
            return CommonResult.FAILURE;
        }
        if(dbEmailAuth.getExpiresAt().isBefore(LocalDateTime.now()) || dbEmailAuth.isExpired()|| dbEmailAuth.isVerified() || dbEmailAuth.isUsed()){
            return VerifyResult.FAILURE_EXPIRED;
        }
        dbEmailAuth.setVerified(true);
        return this.userMapper.updateEmailAuth(dbEmailAuth) > 0
                ? CommonResult.SUCCESS
                : CommonResult.FAILURE;
    }

    @Transactional
    public Result<?> sendResetPasswordEmail(EmailAuthEntity emailAuth) throws NoSuchAlgorithmException, MessagingException{
        if(emailAuth == null || !EmailAuthRegex.email.tests(emailAuth.getEmail())){
            return CommonResult.FAILURE;
        }
        if(this.userMapper.selectUserByEmail(emailAuth.getEmail()) == null){
            return CommonResult.FAILURE;
        }
        prepareEmailAuth(emailAuth);
        if(this.userMapper.insertEmailAuth(emailAuth) != 1){
            return CommonResult.FAILURE;
        }
        Context context = new Context();
        context.setVariable("code", emailAuth.getCode());
        new MailSender(this.mailSender)
                .setFrom("gyust941326@gmail.com")
                .setSubject("[맛집] 비밀번호 재설정 인증번호")
                .setText(this.templateEngine.process("user/resetPasswordEmail", context), true)
                .setTo(emailAuth.getEmail())
                .send();
        return CommonResult.SUCCESS;
    }

    @Transactional
    public Result<?> resetPassword(EmailAuthEntity emailAuth,
                                UserEntity user){
        System.out.println(emailAuth);
        System.out.println(user);
        if(emailAuth == null || user == null ||
                !EmailAuthRegex.email.tests(emailAuth.getEmail()) ||
                !EmailAuthRegex.code.tests(emailAuth.getCode()) ||
                !EmailAuthRegex.salt.tests(emailAuth.getSalt()) ||
                !UserRegex.password.tests(user.getPassword())){
            System.out.println(1);
            return CommonResult.FAILURE;
        }
        EmailAuthEntity dbEmailAuth = this.userMapper.selectEmailAuthByEmailCodeSalt(
                emailAuth.getEmail(),
                emailAuth.getCode(),
                emailAuth.getSalt());
        if(dbEmailAuth == null || !dbEmailAuth.isVerified() || dbEmailAuth.isUsed()){
            System.out.println(2);
            return CommonResult.FAILURE;
        }
        UserEntity dbUser = this.userMapper.selectUserByEmail(emailAuth.getEmail());
        if(dbUser == null){ // 필요 시 isDeleted 구문에 해당하는 엔티티 + 항목 만들기
            System.out.println(3);
            return CommonResult.FAILURE;
        }
        dbEmailAuth.setUsed(true);
        this.userMapper.updateEmailAuth(dbEmailAuth);
        dbUser.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        this.userMapper.updateUser(dbUser);

        return CommonResult.SUCCESS;
    }

    public Result<?> recoverEmail(UserEntity user){
        if(user == null ||
                !UserRegex.nickname.tests(user.getNickname())) {
            return CommonResult.FAILURE;
        }
        UserEntity dbUser = this.userMapper.selectUserByNickname(user.getNickname());
        if(dbUser == null){ // 필요 시 isDeleted 구문에 해당하는 엔티티 + 항목 만들기
            return CommonResult.FAILURE;
        }
        user.setEmail(dbUser.getEmail());
        return CommonResult.SUCCESS;
    }

    public Result<?> getAttendance(String email,
                                   String date,
                                   String StartDate,
                                   String endDate){
        UserProperty dbUser = this.adminMapper.selectUserProperty(email, date, StartDate, endDate);
        if(dbUser == null){
            return CommonResult.FAILURE;
        }
        return CommonResult.SUCCESS;
    }
}
    public Result<CommonResult> insertAttendance(UserEntity user){
        AttendanceEntity attendance = new AttendanceEntity();
        attendance.setUserEmail(user.getEmail());
        attendance.setCheckIn(LocalDateTime.now());

        attendanceMapper.insertAttendance(attendance);
        return CommonResult.SUCCESS;
    }


}
