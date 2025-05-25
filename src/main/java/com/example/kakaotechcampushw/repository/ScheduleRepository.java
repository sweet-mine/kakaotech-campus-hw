package com.example.kakaotechcampushw.repository;

import com.example.kakaotechcampushw.dto.ScheduleResponseDto;
import com.example.kakaotechcampushw.entity.Author;
import com.example.kakaotechcampushw.entity.Schedule;

import java.util.List;
import java.util.Optional;

public interface ScheduleRepository {

    ScheduleResponseDto saveSchedule(Author author, Schedule schedule);

    List<ScheduleResponseDto> findAllSchedules();

    Optional<Schedule> findScheduleById(Long id);

    Schedule findScheduleByIdOrElseThrow(Long id);

    int updateSchedule(Long id, String task, String password);
    int updateName(Long id, String name);

    int deleteSchedule(Long id, String password);
    Optional<Author> findAuthorByNameAndEmail(String name, String email);

}