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
    private String nickname;
    private String password;
    private String name;
    private String tel;
    private String address;
    private String role;
}
