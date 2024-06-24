package com.scd.dcs.services;

import com.scd.dcs.domains.entities.SubmitImageEntity;
import com.scd.dcs.domains.entities.UserEntity;
import com.scd.dcs.domains.vos.UserProperty;
import com.scd.dcs.domains.vos.PaymentVo;
import com.scd.dcs.mappers.AdminMapper;
import com.scd.dcs.mappers.UserMapper;
import com.scd.dcs.mappers.WorkMapper;
import com.scd.dcs.results.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class AdminService {

    private final AdminMapper adminMapper;
    private final UserMapper userMapper;
    private final WorkMapper workMapper;

    @Autowired
    public AdminService(AdminMapper adminMapper, UserMapper userMapper, WorkMapper workMapper) {
        this.adminMapper = adminMapper;
        this.userMapper = userMapper;
        this.workMapper = workMapper;
    }

    public List<UserProperty> getUserProperty(String date) {
        UserEntity[] users = this.userMapper.selectUsers();
//        System.out.println(Arrays.toString(users));

        List<UserProperty> userPropertyList = new ArrayList<>();

        String firstDate = date + " 00:00:00";
        String secondDate = date + " 23:59:59";
//        System.out.println(firstDate);
//        System.out.println(secondDate);
        for (UserEntity user : users) {
//            System.out.println(user.getEmail());
            UserProperty dbUser = this.adminMapper.selectUserProperty(user.getEmail(), date, firstDate, secondDate);
            if (dbUser == null) {
                UserProperty newUser = new UserProperty();
                newUser.setName(user.getName());
                newUser.setEmail(user.getEmail());
                newUser.setCount(0);
                userPropertyList.add(newUser);
            } else {
                userPropertyList.add(dbUser);
            }
        }
        return userPropertyList;
    }


    @Transactional
    public CommonResult updateComment(List<Object> sendList) {
        // 선택 바로 , 검수 x
        try {
            for (Object item : sendList) {
                Map<String, Object> listItem = (Map<String, Object>) item;
                Object comment = listItem.get("comment");
                Object index = listItem.get("index");
                SubmitImageEntity dbSubmitImage = this.workMapper.selectSubmitImage(Integer.parseInt((String) index));
//                System.out.println(dbSubmitImage);
                dbSubmitImage.setComment((String) comment);
//                System.out.println(3);
                dbSubmitImage.setSign(true);

                this.workMapper.updateImage(dbSubmitImage);
            }
        } catch (Exception e) {
            return CommonResult.FAILURE;
        }
        return CommonResult.SUCCESS;
    }

    public UserEntity selectUser(String email) {
        return this.userMapper.selectUserByEmail(email);
    }

    public PaymentVo[] selectWorkVo(String date) {
//        System.out.println("초기화면임");
        return this.workMapper.selectUserAndWorkDaysByDate(date);
    }

    public PaymentVo[] selectWorkVoByOption(String date, String option) {
//        System.out.println(option + " 이 선택됨");
        if (option.equals("year")) {
            date = date.substring(0, 4);
        }
//        System.out.println(date);
        return this.workMapper.selectUserAndWorkDaysByDateAndOption(date, option);
    }
}
