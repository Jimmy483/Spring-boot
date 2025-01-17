package com.gmi.learn.dao.impl;

import com.gmi.learn.dao.AuthorDao;
import com.gmi.learn.domain.Author;
import org.springframework.jdbc.core.JdbcTemplate;

public class AuthorDaoImpl implements AuthorDao {

    private final JdbcTemplate jdbcTemplate;

    public AuthorDaoImpl(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void create(Author author) {

        jdbcTemplate.update(
                "Insert into author(id, name, age) values(?, ?, ?)",
                author.getId(), author.getName(), author.getAge()
        );
    }
}
