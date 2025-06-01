package com.example.demo.repositories;

import com.example.demo.models.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;


@Repository
public interface MeetingRepository extends JpaRepository<Meeting, Integer> {
    @Query("""
from Meeting as m
left join fetch m.participants p
inner join fetch m.organizer
where p.id = :userId
""")
    List<Meeting> getUserParticipatingMeetings(Integer userId);

    @Query("""
from Meeting as m
left join fetch m.participants p
inner join fetch m.organizer o
where o.id = :userId
""")
    List<Meeting> getUserOrganizedMeetings(Integer userId);

    @Query("""
from Meeting as m
left join fetch m.participants p
inner join fetch m.organizer o
where m.organizer.id = :userId or p.id = :userId
""")
    List<Meeting> getAllMeetings(Integer userId);


    @Query("""
from Meeting as m
left join fetch m.participants p
inner join fetch m.organizer o
""")
    List<Meeting> getAllMeetings();

    @Query("""
from Meeting as m
left join fetch m.participants p
inner join fetch m.organizer o
where m.start >= :start and m.start <= :end
and (m.organizer.id = :userId or p.id = :userId)
""")
    List<Meeting> getAllMeetings(Integer userId, OffsetDateTime start, OffsetDateTime end);

    @Query("""
from Meeting as m
left join fetch m.participants
inner join fetch m.organizer
where m.id = :id
""")
    Optional<Meeting> getDetails(Integer id);
}