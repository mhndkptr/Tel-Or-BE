package com.pbo.telor.enums;

public enum EventType {
    SEMINAR,
    LOMBA,
    BEASISWA,
    COMPANY_VISIT,
    OPEN_RECRUITMENT;

    public static EventType fromString(String value) {
        return EventType.valueOf(value.trim().toUpperCase().replace("-", "_"));
    }
}
