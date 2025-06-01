package com.example.demo.models;


import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Builder
public class PatchMeetingRequest implements MeetingField {
    private String title;
    private String description;
    private OffsetDateTime start;
    private OffsetDateTime end;
}
