package com.scd.dcs.domains.vos;

import lombok.Data;

@Data
public class EventCount {
    private String eventName;
    private String calendar = "Sports";
    private String color = "blue";
    private String date;
    private String eventDate;
}
