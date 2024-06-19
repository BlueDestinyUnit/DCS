package com.scd.dcs.domains.vos;

import lombok.Data;

@Data
public class EventAttendance {
    private String eventName = "출석";
    private String calendar = "Work";
    private String color = "orange";
    private String date;
    private String eventDate;
}
