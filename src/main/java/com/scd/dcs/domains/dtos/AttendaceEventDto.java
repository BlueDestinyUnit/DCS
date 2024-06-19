package com.scd.dcs.domains.dtos;

import com.scd.dcs.domains.vos.EventAttendance;
import com.scd.dcs.domains.vos.EventCount;
import lombok.Data;

@Data
public class AttendaceEventDto {
    private EventAttendance eventAttendance;
    private EventCount eventCount;

    public AttendaceEventDto() {
        this.eventAttendance = new EventAttendance();
        this.eventCount = new EventCount();
    }
}
