package com.example.demo;

import com.example.demo.models.Meeting;
import com.example.demo.models.MeetingResponse;
import com.example.demo.models.User;
import com.example.demo.services.DateProvider;
import com.example.demo.services.UserMeetingService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

@TestPropertySource(properties = {
        "app.upcoming-window=P1D"
})
public class UserMeetingServiceTests extends BaseIntegrationTest {
    @Autowired
    private UserMeetingService userService;
    @MockitoBean
    private DateProvider dateprovider;

    @Test
    public void get_upcomingMeetings_returnsMeetingsStartingBetweenNowAndTomorrow() {
        var now = OffsetDateTime.of(2000, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);
        var justBeforeNow = now.minusDays(1);
        var tomorrow = now.plusDays(1);
        var justBeforeTomorrow = tomorrow.minus(Duration.ofMillis(1));
        var justAfterTomorrow = tomorrow.plus(Duration.ofMillis(1));
        var nextYear = now.plusYears(1);

        Mockito.when(dateprovider.now())
               .thenReturn(now);

        var user = User.builder()
                       .email("1@email.com")
                       .build();

        var justBeforeNowMeeting = Meeting.builder()
                                          .title("justBefore")
                                          .start(justBeforeNow)
                                          .end(tomorrow)
                                          .organizer(user)
                                          .participants(List.of(user))
                                          .build();

        var nowMeeting = Meeting.builder()
                                .title("now")
                                .start(now)
                                .end(nextYear)
                                .organizer(user)
                                .participants(List.of(user))
                                .build();

        var justBeforeTomorrowMeeting = Meeting.builder()
                                               .title("justBeforeTomorrow")
                                               .start(justBeforeTomorrow)
                                               .end(nextYear)
                                               .organizer(user)
                                               .participants(List.of(user))
                                               .build();

        var tomorrowMeeting = Meeting.builder()
                                     .title("tomorrow")
                                     .start(tomorrow)
                                     .end(nextYear)
                                     .organizer(user)
                                     .participants(List.of(user))
                                     .build();


        var justAfterMeeting = Meeting.builder()
                                      .title("justAfterTomorrow")
                                      .start(justAfterTomorrow)
                                      .end(nextYear)
                                      .organizer(user)
                                      .participants(List.of(user))
                                      .build();


        var afterMeeting = Meeting.builder()
                                  .title("after")
                                  .start(nextYear)
                                  .end(nextYear)
                                  .organizer(user)
                                  .participants(List.of(user))
                                  .build();

        userRepository.saveAndFlush(user);
        meetingRepository.saveAllAndFlush(List.of(justBeforeNowMeeting, nowMeeting, justBeforeTomorrowMeeting, tomorrowMeeting, justAfterMeeting, afterMeeting));

        var response = userService.getUpcomingMeetings(user.getId());

        var expectedMeetings = Stream.of(nowMeeting, justBeforeTomorrowMeeting, tomorrowMeeting)
                                     .map(Meeting::getTitle)
                                     .toArray(String[]::new);

        assertThat(response)
                .extracting(MeetingResponse::getTitle)
                .containsExactlyInAnyOrder(expectedMeetings);

    }
}