package com.scd.dcs.domains.entities;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode(of = "index")
@AllArgsConstructor
@NoArgsConstructor
public class PaymentEntity {
    private int index;
    private String workType;
    private double price;
}
