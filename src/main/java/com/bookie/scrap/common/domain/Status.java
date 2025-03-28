package com.bookie.scrap.common.domain;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Status {
    ACTIVE(1),
    INACTIVE(0);

    @Getter
    private final int code;

    public static Status fromCode(Integer code) {
        for (Status status : Status.values()) {
            if (status.code == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown Status code: " + code);
    }
}
