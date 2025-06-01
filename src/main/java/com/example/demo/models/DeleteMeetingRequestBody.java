package com.example.demo.models;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeleteMeetingRequestBody {
    @NotNull
    private Integer meetingId;
}
