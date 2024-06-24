package com.scd.dcs.domains.entities;


import lombok.*;

@ToString
@Getter
@Setter
@EqualsAndHashCode(of = "email")
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {
    private String email;
    private String password;
    private String name;
    private String nickname;
    private String tel;
    private String address;
    private String role;
    private boolean isState; // 정규직인지 비정규직인지
    private boolean isInsurance; // 보험이 있는 지 없는 지
}