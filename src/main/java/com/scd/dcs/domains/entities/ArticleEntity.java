package com.scd.dcs.domains.entities;


import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

@Getter
@Setter
@ToString
@EqualsAndHashCode(of = "index")
@AllArgsConstructor
@NoArgsConstructor
public class ArticleEntity {
    private int index;
    private String boardCode;
    private String userEmail;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private int view;
    private boolean isDeleted;
    private boolean isSecret;

}
