package com.example.demo.models;

import com.example.demo.common.HasOffsetDateTimeSpan;

public interface MeetingField extends HasOffsetDateTimeSpan {
    String getTitle();
    String getDescription();
}

