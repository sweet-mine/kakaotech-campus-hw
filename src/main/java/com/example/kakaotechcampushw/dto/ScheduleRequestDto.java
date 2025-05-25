package com.example.kakaotechcampushw.dto;

import lombok.Getter;

/**
 * Memo 요청 DTO
 */
@Getter
public class ScheduleRequestDto {

    /**
     * 제목
     */
    private String task;

    /**
     * 내용
     */
    private String name;

    // 비밀번호
    private String password;

    // 이메일
    private String email;
}
