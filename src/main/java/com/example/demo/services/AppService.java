package com.example.demo.services;

import com.example.demo.models.Meeting;
import com.example.demo.repositories.MeetingRepository;
import com.example.demo.models.User;
import com.example.demo.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.stream.IntStream;

@Service
public class AppService {

    private final UserRepository userRepository;
    private final MeetingRepository meetingRepository;


    public AppService(UserRepository userRepository, MeetingRepository meetingRepository) {
        this.userRepository = userRepository;
        this.meetingRepository = meetingRepository;
    }

    public void seed() {
        var range = IntStream.rangeClosed(1, 20)
                             .boxed()
                             .toList();
        var users = range
                .parallelStream()
                .map(x -> User.builder()
                              .email(x + "@email.com")
                              .details("details" + x)
                              .build())
                .map(userRepository::save)
                .toList();

        var meetings = range.stream()
             .map(x -> newMeeting(x, users.getFirst()))
                .toList();

        for (Meeting meeting : meetings) {
            var rnd = Math.random() * 100;
            var idx = (int) (rnd % users.size());
            var rnd2 = Math.random() * 100;
            var idx2 = (int) (rnd2 % users.size());
            if (idx % 2 == 0) {
                meeting.getParticipants()
                       .add(users.get(idx));
                meeting.setOrganizer(users.get(idx2));
            }
        }

        meetingRepository.saveAll(meetings);

    }

    public void partialSeed() {
        var range = IntStream.rangeClosed(1, 20)
                             .boxed()
                             .toList();
        var users = range
                .parallelStream()
                .map(x -> User.builder()
                          .email(x + "@email.com")
                          .details("details" + x)
                          .build())
                .map(userRepository::save)
                .toList();

        range.stream()
             .map(x -> newMeeting(x, users.getFirst()))
             .forEach(meetingRepository::save);

    }

    private Meeting newMeeting(Integer prefix, User organizer) {

        var start = OffsetDateTime.now().plusHours(1);
        var end = start.plusHours(1);

        return Meeting.builder()
                      .title("Meeting " + prefix)
                      .description("meeting description " + prefix)
                      .organizer(organizer)
                      .start(start)
                      .end(end)
                      .build();
    }

    public void deleteSeed(){
        userRepository.deleteAll();
    }
}
