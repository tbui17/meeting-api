package com.example.demo.models;

import com.example.demo.common.OffsetDateTimeSpan;
import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(
        indexes = {
                @Index(name = "idx_start_time", columnList = "start_time"),
                @Index(name = "idx_end_time", columnList = "end_time")
        }
)
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Meeting implements MeetingField {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Integer id;

    @Column(nullable = false)
    @Builder.Default
    private String title = "";

    @Column(nullable = false)
    @Builder.Default
    private String description = "";

    @Column(nullable = false, name = "start_time")
    @Builder.Default
    private OffsetDateTime start = OffsetDateTime.now();

    @Column(nullable = false, name = "end_time")
    @Builder.Default
    private OffsetDateTime end = OffsetDateTime.now();

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private User organizer;

    @ManyToMany
    @JoinTable(name = "meeting_participants",
            joinColumns = @JoinColumn(name = "meeting_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"),
            uniqueConstraints = {
                    @UniqueConstraint(name = "meeting_participants_unique", columnNames = {"meeting_id", "user_id"})
            }
    )
    @Builder.Default
    private List<User> participants = new ArrayList<>();


    public void setRange(OffsetDateTimeSpan span) {
        setStart(span.start());
        setEnd(span.end());
    }


}
