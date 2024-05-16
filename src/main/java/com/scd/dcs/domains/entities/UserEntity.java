package com.scd.dcs.domains.entities;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class UserEntity {
    private String email;
    private String password;
    private String role;
}
