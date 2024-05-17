package com.scd.dcs.domains.entities;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode(of = "index")
@AllArgsConstructor
@NoArgsConstructor
public class CommentEntity {
    private int index;
    private int articleIndex;
    private String userEmail;
    private String content;
    private LocalDateTime createdAt;
}
