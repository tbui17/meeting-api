package com.example.demo.controllers;

import com.example.demo.mappers.UserMapper;
import com.example.demo.models.*;
import com.example.demo.services.MeetingService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/meetings")
class MeetingController {


    private final MeetingService meetingService;
    private final UserMapper userMapper;

    MeetingController(MeetingService meetingService, UserMapper userMapper) {
        this.meetingService = meetingService;
        this.userMapper = userMapper;
    }

    @Operation(
            summary = "Get all meetings"
    )
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<FullMeetingResponse> get() {
        return meetingService.get();
    }

    @Operation(
            summary = "Get meeting details"
    )
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public FullMeetingResponse get(@PathVariable Integer id) {
        return meetingService.get(id);
    }

    @Operation(
            summary = "Create a meeting"
    )
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public MeetingResponse post(@RequestBody MeetingRequest body) {
        return meetingService.post(body);
    }

    @Operation(
            summary = "Modify a meeting"
    )
    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public MeetingResponse patch(@PathVariable Integer id, @RequestBody @Valid PatchMeetingRequest body) {
        return meetingService.patch(id, body);
    }

    @Operation(
            summary = "Have a participant join a meeting"
    )
    @PostMapping("/{id}/participants")
    @ResponseStatus(HttpStatus.OK)
    public MeetingResponse post(@PathVariable Integer id, @RequestBody ParticipantRequestBody body) {
        var req = userMapper.toParticipantJoinRequest(body,id);
        return meetingService.post(req);
    }

    @Operation(
            summary = "Remove a participant from a meeting"
    )
    @DeleteMapping("/{id}/participants")
    @ResponseStatus(HttpStatus.OK)
    public void deleteParticipant(@PathVariable Integer id, @RequestBody DeleteMeetingRequestBody body) {
        var req = userMapper.toParticipantLeaveRequest(body,id);
        meetingService.delete(req);
    }

    @Operation(
            summary = "Delete a meeting"
    )
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        meetingService.delete(id);
    }
}

