package com.scd.dcs.domains.entities;


import lombok.*;

@Getter
@Setter
@EqualsAndHashCode(of = "index")
@AllArgsConstructor
@NoArgsConstructor
public class ArticleImageEntity {
    private int index;
    private Integer articleIndex;
    private String originalName;
    private String contentType;
    private byte[] data;
}
