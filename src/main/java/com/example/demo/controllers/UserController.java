package com.example.demo.controllers;

import com.example.demo.models.MeetingResponse;
import com.example.demo.models.UserRequest;
import com.example.demo.models.UserResponse;
import com.example.demo.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.jetbrains.annotations.Nullable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
class UserController {

    private final UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(
            summary = "Get users"
    )
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserResponse> get(@RequestParam(required = false) @Nullable String email) {
        return userService.get(email);
    }

    @Operation(
            summary = "List meetings for a user.",
            parameters = {
                    @Parameter(name = "type", description = "Filter meetings by type Possible values are: organized, participating, upcoming. Defaults to listing all types for a user otherwise.")
            }
    )
    @GetMapping("/{id}/meetings")
    @ResponseStatus(HttpStatus.OK)
    public List<MeetingResponse> getMeetings(@PathVariable Integer id, @RequestParam(required = false, defaultValue = "") String type) {
        return userService.getMeetings(id, type);
    }

    @Operation(
            summary = "Create a user"
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse post(@RequestBody UserRequest userRequest) {
        return userService.post(userRequest);
    }

    @Operation(
            summary = "Delete a user"
    )
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(Integer id) {
        userService.delete(id);
    }
}

