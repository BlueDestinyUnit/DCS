package com.scd.dcs.domains.entities;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode(of = "index")
@AllArgsConstructor
@NoArgsConstructor
public class ImageCommentEntity {
    private int index;
    private int imageIndex;
    private String content;
}
