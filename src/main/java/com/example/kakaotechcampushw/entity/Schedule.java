package com.example.kakaotechcampushw.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class Schedule {
    private Long author_id;
    private Long id;
    private String task;
    private String password;

    public Schedule(String task, String password) {
        this.task = task;
        this.password = password;
    }

    public void update(String task) {
        this.task = task;
    }
}
