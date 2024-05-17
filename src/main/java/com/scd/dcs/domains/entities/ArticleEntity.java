package com.scd.dcs.domains.entities;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode(of = "index")
@AllArgsConstructor
@NoArgsConstructor
public class ArticleEntity {
    private int index;
    private String boardCode;
    private String userEmail;
    private String title;
    private String content;
    private int view;
}
