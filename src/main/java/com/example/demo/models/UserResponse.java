package com.example.demo.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse implements UserField {
    private Integer id;
    private String email;
    private String details;
}