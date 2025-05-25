package com.example.kakaotechcampushw.dto;

import com.example.kakaotechcampushw.entity.Schedule;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Memo 응답 DTO
 */
@Getter
@AllArgsConstructor
public class ScheduleResponseDto {

    /**
     * 작성자 식별자
     */
    private Long author_id;

    // 스케줄 식별자
    private Long id;
    
    // 할 일
    private String task;

    public ScheduleResponseDto(Schedule schedule) {
        this.author_id = schedule.getAuthor_id();
        this.id = schedule.getId();
        this.task = schedule.getTask();
    }
}
