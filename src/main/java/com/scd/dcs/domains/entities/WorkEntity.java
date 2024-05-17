package com.scd.dcs.domains.entities;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode(of = "index")
@AllArgsConstructor
@NoArgsConstructor
public class WorkEntity {
    private int index;
    private String userEmail;
    private LocalDateTime workDate;
    private long content;
}
