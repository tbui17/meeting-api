package com.example.demo.mappers;

import com.example.demo.models.*;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR, componentModel = MappingConstants.ComponentModel.SPRING, uses = {UserMapper.class})
public abstract class MeetingMapper {


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "participants", ignore = true)
    @Mapping(target = "organizer", source = "user")
    public abstract Meeting toEntity(MeetingRequest meetingRequest, User user);

    public abstract MeetingResponse toResponse(Meeting meeting);

    public abstract FullMeetingResponse toFullResponse(Meeting meeting);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "participants", ignore = true)
    @Mapping(target = "organizer", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract void patch(MeetingField meetingField, @MappingTarget Meeting meeting);

}