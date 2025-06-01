
package com.example.demo.services;

import com.example.demo.mappers.MeetingMapper;
import com.example.demo.models.*;

import com.example.demo.repositories.MeetingRepository;
import com.example.demo.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class MeetingService {
    private final MeetingRepository meetingRepository;
    private final MeetingMapper meetingMapper;
    private final UserRepository userRepository;
    private final TimeService timeService;


    public MeetingService(MeetingRepository meetingRepository, MeetingMapper meetingMapper, UserRepository userRepository, TimeService timeService) {
        this.meetingRepository = meetingRepository;
        this.meetingMapper = meetingMapper;
        this.userRepository = userRepository;
        this.timeService = timeService;
    }

    public List<FullMeetingResponse> get() {
        return meetingRepository.getAllMeetings()
                                 .stream()
                                 .map(meetingMapper::toFullResponse)
                                 .toList();
    }

    public FullMeetingResponse get(Integer id) {
        var meeting = meetingRepository
                .getDetails(id)
                .orElseThrow();

        return meetingMapper.toFullResponse(meeting);
    }

    @Transactional
    public MeetingResponse post(MeetingRequest meetingRequest) {
        var user = userRepository.findById(meetingRequest.getOrganizerId())
                                 .orElseThrow();

        var meeting = meetingMapper.toEntity(meetingRequest, user);
        if (meeting.getStart() == null || meeting.getEnd() == null){
            meeting.setRange(timeService.getDefaultValue());
        }
        var result = meetingRepository.save(meeting);
        return meetingMapper.toResponse(result);
    }

    @Transactional
    public MeetingResponse post(ParticipantJoinRequest participantJoinRequest) {
        var meeting = meetingRepository
                .findById(participantJoinRequest.getMeetingId())
                .orElseThrow();

        var user = userRepository.findById(participantJoinRequest.getParticipantId())
                                 .orElseThrow();

        meeting.getParticipants().add(user);
        var res = meetingRepository.save(meeting);
        return meetingMapper.toResponse(res);
    }


    public MeetingResponse patch(Integer id, PatchMeetingRequest patchMeetingRequest) {
        var meeting = meetingRepository
                .findById(id)
                .orElseThrow();

        meetingMapper.patch(patchMeetingRequest, meeting);
        var res = meetingRepository.save(meeting);
        return meetingMapper.toResponse(res);
    }


    public void delete(Integer id) {
        meetingRepository.deleteById(id);
    }

    @Transactional
    public void delete(ParticipantLeaveRequest participantLeaveRequest) {
        var meeting = meetingRepository.findById(participantLeaveRequest.getMeetingId()).orElseThrow();
        var user = userRepository.getReferenceById(participantLeaveRequest.getParticipantId());
        meeting.getParticipants().remove(user);
        meetingRepository.save(meeting);
    }
}