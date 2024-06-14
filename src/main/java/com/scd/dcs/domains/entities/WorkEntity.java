package com.scd.dcs.domains.entities;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode(of = "index")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class WorkEntity {
    private int index;
    private String userEmail;
    private LocalDate date;

}
