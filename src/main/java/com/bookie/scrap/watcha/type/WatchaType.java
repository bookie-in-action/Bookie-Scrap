package com.bookie.scrap.watcha.type;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

public class WatchaType {

    public enum EXTERNAL_SERVICE {ALADIN, YES24, KYOBO}

    @ToString
    @Embeddable
    @Getter @Setter
    @NoArgsConstructor
    public static class Poster {
        private String hd;
        private String xlarge;
        private String large;
        private String medium;
        private String small;
    }

    @ToString
    @Embeddable
    @Getter @Setter
    @NoArgsConstructor
    public static class UserPhoto {
        private String original;
        private String large;
        private String small;
    }
}
