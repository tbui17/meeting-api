package com.example.demo.services;

import com.example.demo.mappers.UserMapper;
import com.example.demo.models.*;


import com.example.demo.repositories.UserRepository;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserMeetingService userMeetingService;


    UserService(UserRepository userRepository, UserMapper userMapper, UserMeetingService userMeetingService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.userMeetingService = userMeetingService;
    }

    public List<UserResponse> get(@Nullable String email) {
        if (email == null || email.isEmpty()) {
            return getUsers();
        }
        return getUserByEmail(email).stream()
                                    .toList();
    }

    public Optional<UserResponse> getUserByEmail(String email) {
        return userRepository.findUserByEmail(email)
                             .map(userMapper::toDto);
    }

    public List<UserResponse> getUsers() {
        return userRepository.findAll()
                             .stream()
                             .map(userMapper::toDto)
                             .toList();
    }


    public UserResponse post(UserField userField) {
        var user = userMapper.toEntity(userField);
        var savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }

    public void delete(Integer id) {
        userRepository.deleteById(id);
    }


    public List<MeetingResponse> getMeetings(Integer userId, String type) {
        return switch (type.toLowerCase()) {
            case "organized" -> userMeetingService.getOrganizedMeetings(userId);
            case "participating" -> userMeetingService.getParticipatingMeetings(userId);
            case "upcoming" -> userMeetingService.getUpcomingMeetings(userId);
            default -> userMeetingService.getAllMeetings(userId);
        };
    }

}