package com.scd.dcs.domains.vos;

import com.scd.dcs.domains.entities.WorkEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class WorkVo extends WorkEntity {
    private int workDays;
}
