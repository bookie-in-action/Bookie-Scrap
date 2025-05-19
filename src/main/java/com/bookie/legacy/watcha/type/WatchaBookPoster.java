package com.bookie.legacy.watcha.type;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Embeddable
@Getter @Setter
@NoArgsConstructor
public class WatchaBookPoster {
        private String hd;
        private String xlarge;
        private String large;
        private String medium;
        private String small;
}
