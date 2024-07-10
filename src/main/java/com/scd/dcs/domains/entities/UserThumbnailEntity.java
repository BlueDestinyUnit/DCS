package com.scd.dcs.domains.entities;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode(of = "index")
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "imageData")
public class UserThumbnailEntity {
    private int index;
    private String userEmail;
    private String contentType;
    private String imageName;
    private byte[] imageData;
}
