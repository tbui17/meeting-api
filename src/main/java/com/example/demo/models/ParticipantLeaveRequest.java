package com.example.demo.models;


import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ParticipantLeaveRequest {
    private Integer participantId;
    private Integer meetingId;
}

