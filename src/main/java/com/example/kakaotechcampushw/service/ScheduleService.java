package com.example.kakaotechcampushw.service;

import com.example.kakaotechcampushw.dto.ScheduleRequestDto;
import com.example.kakaotechcampushw.dto.ScheduleResponseDto;

import java.util.List;

public interface ScheduleService {

    ScheduleResponseDto saveSchedule(ScheduleRequestDto requestDto);

    List<ScheduleResponseDto> findAllSchedules();

    ScheduleResponseDto findScheduleById(Long id);

    ScheduleResponseDto updateSchedule(Long id, String task, String password);

    void updateName(Long id, String name);

    void deleteSchedule(Long id, String password);

}
