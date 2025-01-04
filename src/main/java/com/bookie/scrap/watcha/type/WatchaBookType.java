package com.bookie.scrap.watcha.type;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

public class WatchaBookType {

    public enum EXTERNAL_SERVICE {ALADIN, YES24, KYOBO}

    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    public static class Poster {
        private String hd;
        private String xlarge;
        private String large;
        private String medium;
        private String small;
    }
}
