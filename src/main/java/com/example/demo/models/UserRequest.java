package com.example.demo.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserRequest implements UserField {
    private String email;
    private String details;
}