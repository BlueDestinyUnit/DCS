package com.scd.dcs.config.security.domains;

import com.scd.dcs.domains.entities.UserEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
@Setter
@ToString
public class SecurityUser extends User {
    private UserEntity userEntity;

    public SecurityUser(UserEntity userEntity, Collection<? extends GrantedAuthority> authorities) {
        super(userEntity.getEmail(), userEntity.getPassword(), authorities);
        this.userEntity = userEntity;
    }

}