package com.scd.dcs.domains.entities;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class AttendanceEntity {
    private int index;
    private String userEmail;
    private LocalDateTime checkIn;
}
