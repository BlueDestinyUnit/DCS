package com.scd.dcs.domains.entities;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode(of = "index")
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailEntity {
    private int index;
    private String userEmail;
    private boolean isState;
    private boolean isInsurance;
}
