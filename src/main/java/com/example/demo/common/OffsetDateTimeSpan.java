package com.example.demo.common;

import java.time.OffsetDateTime;

public record OffsetDateTimeSpan(OffsetDateTime start, OffsetDateTime end) {

    public Boolean isValid() {
        return !start.isAfter(end);
    }

    public Boolean contains(OffsetDateTime time) {
        var withinStart = isAfterOrEqual(time, start);
        var withinEnd = isBeforeOrEqual(time, end);
        return withinStart && withinEnd;
    }

    public Boolean contains(OffsetDateTimeSpan other) {
        return contains(other.start()) && contains(other.end());
    }

    private static Boolean isBeforeOrEqual(OffsetDateTime self, OffsetDateTime other) {
        return self.isBefore(other) || self.isEqual(other);
    }

    private static Boolean isAfterOrEqual(OffsetDateTime self, OffsetDateTime other) {
        return self.isAfter(other) || self.isEqual(other);
    }

}
