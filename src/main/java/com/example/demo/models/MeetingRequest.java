package com.example.demo.models;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.jetbrains.annotations.Nullable;

import java.time.OffsetDateTime;

@Data
@Builder
public class MeetingRequest implements MeetingField {
    @Builder.Default
    private String title = "";
    @Builder.Default
    private String description = "";
    private @Nullable OffsetDateTime start;
    private @Nullable OffsetDateTime end;
    private @NotNull Integer organizerId;
}