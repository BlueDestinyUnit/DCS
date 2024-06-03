package com.scd.dcs.services;

import com.scd.dcs.domains.entities.UserEntity;
import com.scd.dcs.mappers.AdminMapper;
import com.scd.dcs.mappers.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    private final AdminMapper adminMapper;
    private final UserMapper userMapper;

    @Autowired
    public AdminService(AdminMapper adminMapper, UserMapper userMapper) {
        this.adminMapper = adminMapper;
        this.userMapper = userMapper;
    }

    public UserEntity[] getName(String date) {

        return null;
    }
}
