package com.bookie.scrap.watcha.request.book.bookmeta.rdb;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter @Setter
@Embeddable
@NoArgsConstructor
public class RdbBookPoster {
        @Column(columnDefinition = "TEXT")
        private String hd;

        @Column(columnDefinition = "TEXT")
        private String xlarge;

        @Column(columnDefinition = "TEXT")
        private String large;

        @Column(columnDefinition = "TEXT")
        private String medium;

        @Column(columnDefinition = "TEXT")
        private String small;
}
