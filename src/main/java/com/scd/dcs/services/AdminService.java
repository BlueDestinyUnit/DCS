package com.scd.dcs.services;

import com.scd.dcs.domains.entities.SubmitImageEntity;
import com.scd.dcs.domains.entities.UserEntity;
import com.scd.dcs.domains.entities.WorkEntity;
import com.scd.dcs.domains.vos.UserProperty;
import com.scd.dcs.mappers.AdminMapper;
import com.scd.dcs.mappers.UserMapper;
import com.scd.dcs.mappers.WorkMapper;
import com.scd.dcs.results.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        System.out.println(Arrays.toString(users));

        List<UserProperty> userPropertyList = new ArrayList<>();

        String firstDate = date + " 00:00:00";
        String secondDate = date + " 23:59:59";
        System.out.println(firstDate);
        System.out.println(secondDate);
        for (UserEntity user : users) {
            System.out.println(user.getEmail());
            UserProperty dbUser = this.adminMapper.selectUserProperty(user.getEmail(), date, firstDate, secondDate);
            if(dbUser == null){
                UserProperty newUser = new UserProperty();
                newUser.setName(user.getName());
                newUser.setEmail(user.getEmail());
                newUser.setCount(0);
                userPropertyList.add(newUser);
            }else {
                userPropertyList.add(dbUser);
            }



        }
        return userPropertyList;
    }

    public CommonResult insertComment(String date, String email, String comment) {
        WorkEntity dbWork = this.workMapper.findWorkByDateAndUser(LocalDate.parse(date), email);
        return null;
    }



}
