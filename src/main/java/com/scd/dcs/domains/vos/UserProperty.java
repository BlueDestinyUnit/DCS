package com.scd.dcs.domains.vos;

import com.scd.dcs.domains.entities.UserEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserProperty extends UserEntity {
    private Boolean attendance;
    private int count;
}
