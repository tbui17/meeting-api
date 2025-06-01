package com.example.demo;

import com.example.demo.mappers.MeetingMapper;
import com.example.demo.mappers.MeetingMapperImpl;
import com.example.demo.mappers.UserMapperImpl;
import com.example.demo.models.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(classes = {MeetingMapperImpl.class, UserMapperImpl.class})
public class MapperTests {

    @Autowired
    private MeetingMapper meetingMapper;

    @Test
    public void shouldMapNestedFields() {
        var user = User.builder()
                .email("email@mail.com")
                .build();

        var meeting = Meeting.builder()
                .organizer(user)
                .build();
        var dto = meetingMapper.toFullResponse(meeting);

        assertThat(dto)
                .extracting(d -> d.getOrganizer().getEmail())
                .isEqualTo(user.getEmail());
    }

    @Test
    public void testIgnoreAltSourceId() {

        var user = User.builder()
                .id(1)
                .email("u1@mail.com")
                .build();
        var meetingRequest = MeetingRequest.builder()
                .build();

        var meeting = meetingMapper.toEntity(meetingRequest, user);
        assertThat(meeting)
                .extracting(Meeting::getId)
                .isNull();

    }

    @Test
    public void shouldAvoidRecursivelyMappingDtos() {
        var user = User.builder()
                .email("a@mail.com")
                .build();

        var meeting = Meeting.builder()
                .description("abc")
                .organizer(user)
                .title("abc")
                .start(OffsetDateTime.now())
                .end(OffsetDateTime.now())
                .build();

        meeting.getParticipants().add(user);
        user.getMeetings().add(meeting);
        assertThatNoException()
                .isThrownBy(() -> meetingMapper.toResponse(meeting));

    }
}