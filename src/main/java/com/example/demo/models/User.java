package com.example.demo.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;


import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "app_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Integer id;

    @Column(nullable = false, unique = true)
    @Email
    private String email;

    @Column(nullable = false)
    @Builder.Default
    private String details = "";

    @ManyToMany(mappedBy = "participants")
    @Builder.Default
    private List<Meeting> meetings = new ArrayList<>();

    @OneToMany(mappedBy = "organizer")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @Builder.Default
    private List<Meeting> organizedMeetings = new ArrayList<>();
}