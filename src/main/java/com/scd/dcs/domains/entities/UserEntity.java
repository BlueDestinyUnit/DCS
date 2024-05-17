package com.scd.dcs.domains.entities;


import lombok.*;

@ToString
@Getter
@Setter
@EqualsAndHashCode(of = "email")
@AllArgsConstructor
public class UserEntity {
    private String email;
    private String nickname;
    private String password;
    private String role;
}
