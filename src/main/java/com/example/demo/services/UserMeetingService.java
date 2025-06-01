package com.example.demo.services;

import com.example.demo.mappers.MeetingMapper;
import com.example.demo.models.*;


import com.example.demo.repositories.MeetingRepository;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class UserMeetingService {
    private final MeetingMapper meetingMapper;
    private final MeetingRepository meetingRepository;
    private final TimeService timeService;


    UserMeetingService(MeetingMapper meetingMapper, MeetingRepository meetingRepository, TimeService timeService) {
        this.meetingMapper = meetingMapper;
        this.meetingRepository = meetingRepository;
        this.timeService = timeService;
    }


    public List<MeetingResponse> getAllMeetings(Integer userId) {

        return meetingRepository.getAllMeetings(userId)
                                .stream()
                                .map(meetingMapper::toResponse)
                                .toList();
    }



    public List<MeetingResponse> getParticipatingMeetings(Integer userId) {

        return meetingRepository.getUserParticipatingMeetings(userId)
                                .stream()
                                .map(meetingMapper::toResponse)
                                .toList();
    }


    public List<MeetingResponse> getOrganizedMeetings(Integer userId) {
        return meetingRepository.getUserOrganizedMeetings(userId)
                                .stream()
                                .map(meetingMapper::toResponse)
                                .toList();
    }

    public List<MeetingResponse> getUpcomingMeetings(Integer userId) {

        var range = timeService.getUpcomingRange();
        // both organized and participating meetings

        return meetingRepository.getAllMeetings(userId, range.start(), range.end())
                                .stream()
                                .map(meetingMapper::toResponse)
                                .toList();

    }
}