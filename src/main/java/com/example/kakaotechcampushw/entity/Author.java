package com.example.kakaotechcampushw.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class Author {
    private Long id;
    private String name;
    private String email;

    public Author(String name, String email) {
        this.name = name;
        this.email = email;
    }
}
