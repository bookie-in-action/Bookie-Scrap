package com.bookie.scrap.watcha.dto;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
public class WatchaEntity {

    private String pk;
    private String createdAt;
    private String updatedAt;

    @Getter private String code;


}
