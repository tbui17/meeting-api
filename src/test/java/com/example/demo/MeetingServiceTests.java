package com.example.demo;

import com.example.demo.common.Constants;
import com.example.demo.models.*;
import com.example.demo.services.*;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class MeetingServiceTests extends BaseIntegrationTest {
    @Autowired
    private MeetingService meetingService;
    @Autowired
    AppService appService;
    @Autowired
    private PlatformTransactionManager transactionManager;

    @Test
    public void post_shouldReturnResponseWithMeetingId() {

        var user = userRepository.saveAndFlush(User.builder()
                                                   .email("user1@mail.com")
                                                   .details("user details")
                                                   .build());
        var dto = MeetingRequest.builder()
                                .title("Test meeting")
                                .description("Test description")
                                .organizerId(user.getId())
                                .build();
        var response = meetingService.post(dto);


        assertThat(response).extracting(MeetingResponse::getId)
                            .isNotEqualTo(0);
    }


    @Test
    public void post_shouldDefaultToValidDateGetRangeInPresentOrFutureWhenUnspecified() {

        var now = OffsetDateTime.now();
        var user = userRepository.saveAndFlush(User.builder()
                                                   .email("user1@mail.com")
                                                   .details("user details")
                                                   .build());
        var dto = MeetingRequest.builder()
                                .title("Test meeting")
                                .description("Test description")
                                .organizerId(user.getId())
                                .build();

        var response = meetingService.post(dto);

        assertThat(response.getStart())
                .isAfterOrEqualTo(now)
                .isBeforeOrEqualTo(response.getEnd());
    }

    @Test
    public void post_shouldClientErrorWhenCannotParseDateGetRange() throws Exception {

        var now = OffsetDateTime.MIN;
        var user = userRepository.saveAndFlush(User.builder()
                                                   .email("user1@mail.com")
                                                   .details("user details")
                                                   .build());
        var dto = MeetingRequest.builder()
                                .title("Test meeting")
                                .description("Test description")
                                .organizerId(user.getId())
                                .end(now)
                                .start(now.plusNanos(1))
                                .build();

        mockMvc
                .perform(post("/meetings")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void post_shouldClientErrorWhenInvalidDateGetRange() throws Exception {

        var user = userRepository.saveAndFlush(User.builder()
                                                   .email("user1@mail.com")
                                                   .details("user details")
                                                   .build());
        var dto = MeetingRequest.builder()
                                .title("Test meeting")
                                .description("Test description")
                                .organizerId(user.getId())
                                .start(Constants.dbSafeRange.start()
                                                            .minusHours(1))
                                .end(Constants.dbSafeRange.end()
                                                          .plusNanos(1))
                                .build();

        mockMvc
                .perform(post("/meetings")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void post_shouldClientErrorWhenInvalidDate() throws Exception {

        var now = OffsetDateTime.MIN;
        var user = userRepository.saveAndFlush(User.builder()
                                                   .email("user1@mail.com")
                                                   .details("user details")
                                                   .build());


        var startTime = now.plusNanos(1);
        var endTime = now.plusNanos(1);

        var dto = MeetingRequest.builder()
                                .title("Test meeting")
                                .description("Test description")
                                .organizerId(user.getId())
                                .start(startTime)
                                .end(endTime)
                                .build();

        mockMvc
                .perform(post("/meetings")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void post_should404WhenUserDoesNotExist() throws Exception {

        var now = OffsetDateTime.now();

        var startTime = now.plusNanos(1);
        var endTime = now.plusNanos(1);

        var dto = MeetingRequest.builder()
                                .title("Test meeting")
                                .description("Test description")
                                .organizerId(123)
                                .start(startTime)
                                .end(endTime)
                                .build();

        mockMvc
                .perform(post("/meetings")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void post_meetingParticipant() {

        appService.partialSeed();

        var user2 = userRepository.findUserByEmail("2@email.com")
                                  .orElseThrow();
        var user3 = userRepository.findUserByEmail("3@email.com")
                                  .orElseThrow();

        var meeting = meetingRepository.findAll()
                                       .getFirst();

        var req = ParticipantJoinRequest.builder()
                                        .meetingId(meeting.getId())
                                        .participantId(user3.getId())
                                        .build();


        var req2 = ParticipantJoinRequest.builder()
                                         .participantId(user2.getId())
                                         .meetingId(meeting.getId())
                                         .build();

        meetingService.post(req);
        meetingService.post(req2);


        var res = new TransactionTemplate(transactionManager).execute((tx) -> meetingRepository
                .findById(meeting.getId())
                .orElseThrow()
                .getParticipants()
                .stream()
                .map(User::getId)
                .toList()
        );


        assertThat(res)
                .containsExactlyInAnyOrder(user2.getId(), user3.getId());

    }

    @Test
    public void post_meetingParticipant_throwsClientErrorOnDuplicates() throws Exception {

        appService.partialSeed();

        var users = userRepository.findAll();
        var user1 = users.getFirst();
        var meeting = meetingRepository.findAll()
                                       .getFirst();


        var p1 = ParticipantRequestBody.builder().participantId(user1.getId()).build();

        mockMvc
                .perform(post("/meetings/{id}/participants", meeting.getId())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(p1)))
                .andExpect(status().isOk());


        mockMvc
                .perform(post("/meetings/{id}/participants", meeting.getId())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(p1)))
                .andExpect(status().isBadRequest());


    }
}
