package com.scd.dcs.domains.vos;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class WorkListRequest {
    private List<Object> sendList;
    private String userEmail;
    private String date;
}
