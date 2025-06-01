package com.example.demo.models;

import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Builder
public class MeetingResponse implements MeetingField {
    private Integer id;
    private String title;
    private String description;
    private OffsetDateTime start;
    private OffsetDateTime end;
}