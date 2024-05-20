package com.scd.dcs.domains.entities;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode(of = "index")
@AllArgsConstructor
@NoArgsConstructor
public class SubmitImageEntity {
    private int index;
    private int workIndex;
    private String contentType;
    private long imageData;
    private boolean isSign;
}