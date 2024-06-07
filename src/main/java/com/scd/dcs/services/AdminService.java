package com.scd.dcs.services;

import com.scd.dcs.domains.entities.UserEntity;
import com.scd.dcs.domains.vos.UserProperty;
import com.scd.dcs.mappers.AdminMapper;
import com.scd.dcs.mappers.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class AdminService {

    private final AdminMapper adminMapper;
    private final UserMapper userMapper;

    @Autowired
    public AdminService(AdminMapper adminMapper, UserMapper userMapper) {
        this.adminMapper = adminMapper;
        this.userMapper = userMapper;
    }

    public UserProperty[] getUserProperty(String date) {
        UserEntity[] users = this.userMapper.selectUsers();


        UserProperty[] userProperty = new UserProperty[users.length];
        int cnt = 0;

        for (UserEntity user : users) {
            userProperty[cnt] = this.adminMapper.selectUserProperty(user.getEmail(), date);
            if (this.adminMapper.selectUserProperty(user.getEmail(), date) != null) {
                cnt++;
            }
        }
        return userProperty;
    }

}
