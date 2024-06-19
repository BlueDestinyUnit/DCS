package com.scd.dcs.domains.entities;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode(of = "index")
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "imageData")
public class SubmitImageEntity {
    private int index;
    private int workIndex;
    private String contentType;
    private String originalName;
    private byte[] imageData;
    private boolean isSign;
    private boolean isMosaic;
    private String comment;
}
