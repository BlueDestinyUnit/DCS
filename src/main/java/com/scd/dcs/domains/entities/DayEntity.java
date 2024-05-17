package com.scd.dcs.domains.entities;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode(of = "index")
@AllArgsConstructor
@NoArgsConstructor
public class DayEntity {
    private int index;
    private boolean isWork;
    private LocalDateTime workDay;
}
