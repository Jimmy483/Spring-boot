package com.gmi.learn.dao;

import com.gmi.learn.dao.impl.BookDaoImpl;
import com.gmi.learn.domain.Book;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class BookDaoImplTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private BookDaoImpl underTest;


    @Test
    public void testThatCreateBookGeneratesCorrectSql(){
        Book book= Book.builder()
                .isbn("1-32-4")
                .title("Get rich or die trying")
                .authorId(1L)
                .build();

        underTest.create(book);

        verify(jdbcTemplate).update(
                eq("Insert into book(isbn, title, authorId) values(?, ?, ?)"),
                eq("1-32-4"),
                eq("Get rich or die trying"),
                eq(1L)

        );
    }
}
