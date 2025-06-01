package com.example.demo.common;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public class Constants {

    public static final OffsetDateTimeSpan dbSafeRange = new OffsetDateTimeSpan(
            OffsetDateTime.of(1000, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC),
            OffsetDateTime.of(3000, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC)
    );
}
