package com.example.demo.models;


import lombok.*;

import java.time.OffsetDateTime;
import java.util.List;


@Data
@Builder
public class FullMeetingResponse implements MeetingField {
    private Integer id;
    private String title;
    private String description;
    private OffsetDateTime start;
    private OffsetDateTime end;
    private UserResponse organizer;
    private List<UserResponse> participants;

}
