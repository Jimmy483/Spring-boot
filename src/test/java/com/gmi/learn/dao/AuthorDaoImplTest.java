package com.gmi.learn.dao;

import com.gmi.learn.dao.impl.AuthorDaoImpl;
import com.gmi.learn.domain.Author;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AuthorDaoImplTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private AuthorDaoImpl underTest;

    @Test
    public void testThatCreateAuthorGeneratesCorrectSql(){

        Author author= Author.builder()
                .id(1L)
                .name("Gmi")
                .age(28)
                .build();

        underTest.create(author);

        verify(jdbcTemplate).update(
                eq("Insert into author(id, name, age) values(?, ?, ?)"),
                eq(1L), eq("Gmi"), eq(28)

        );


    }
}
