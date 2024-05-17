package com.scd.dcs.domains.entities;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
public class AttendanceEntity {
    private int index;
    private String userEmail;
    @Builder.Default
    private boolean isAttendance = false;
    private LocalDateTime attendDate;
}
