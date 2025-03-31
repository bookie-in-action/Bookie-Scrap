package com.bookie.scrap.watcha.type;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
@Embeddable
@NoArgsConstructor
public class WatchaUserPhoto {
    private String original;
    private String large;
    private String small;
}
