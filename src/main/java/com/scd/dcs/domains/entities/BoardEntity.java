package com.scd.dcs.domains.entities;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode(of = "code")
@AllArgsConstructor
@Builder
public class BoardEntity {
    private String code;
    private String text;
    @Builder.Default
    private boolean isAdminOnly = false;
}
