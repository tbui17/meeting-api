package com.example.demo.common;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.AssertTrue;

import java.time.OffsetDateTime;

public interface HasOffsetDateTimeSpan {

    OffsetDateTime getStart();

    OffsetDateTime getEnd();

    @JsonIgnore
    default OffsetDateTimeSpan getRange() {
        return new OffsetDateTimeSpan(getStart(), getEnd());
    }

    @JsonIgnore
    @AssertTrue(message = "meeting range is invalid")
    default boolean isValid() {
        var range = getRange();
        return Constants.dbSafeRange.contains(range) && range.isValid();
    }
}
