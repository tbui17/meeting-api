package com.example.demo.services;

import com.example.demo.common.OffsetDateTimeSpan;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;


@Service
public class TimeService {
    private final DateProvider dateProvider;
    private final Duration window;
    private final Duration meetingDefaultDuration;


    public TimeService(DateProvider dateProvider, @Value("${app.upcoming-window}") Duration window, @Value("${app.meeting-default-duration}") Duration meetingDefaultDuration) {
        this.dateProvider = dateProvider;
        this.window = window;
        this.meetingDefaultDuration = meetingDefaultDuration;
    }

    public OffsetDateTimeSpan getDefaultValue() {
        var now = dateProvider.now();
        var end = now.plus(meetingDefaultDuration);
        return new OffsetDateTimeSpan(now, end);
    }

    public OffsetDateTimeSpan getUpcomingRange() {
        var now = dateProvider.now();
        var end = now.plus(window);
        return new OffsetDateTimeSpan(now, end);
    }
}
