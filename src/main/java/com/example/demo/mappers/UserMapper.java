package com.example.demo.mappers;

import com.example.demo.models.*;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR, componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "organizedMeetings", ignore = true)
    @Mapping(target = "meetings", ignore = true)
    public abstract User toEntity(UserField userResponse);

    public abstract UserResponse toDto(User user);


    public abstract ParticipantJoinRequest toParticipantJoinRequest(ParticipantRequestBody request, Integer meetingId);
    public abstract ParticipantLeaveRequest toParticipantLeaveRequest(DeleteMeetingRequestBody request, Integer participantId);
}