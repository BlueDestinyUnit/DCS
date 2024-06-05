package com.scd.dcs.services;

import com.scd.dcs.domains.entities.UserEntity;
import com.scd.dcs.domains.vos.UserProperty;
import com.scd.dcs.mappers.AdminMapper;
import com.scd.dcs.mappers.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class AdminService {

    private final AdminMapper adminMapper;
    private final UserMapper userMapper;

    @Autowired
    public AdminService(AdminMapper adminMapper, UserMapper userMapper) {
        this.adminMapper = adminMapper;
        this.userMapper = userMapper;
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
                newUser.setName(user.getName()); // 여기
                newUser.setCount(0);
                userPropertyList.add(newUser);
            }else {
                userPropertyList.add(dbUser);
            }



        }
        return userPropertyList;
    }

}
