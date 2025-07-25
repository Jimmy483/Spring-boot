package com.gmi.learn.dao.impl;

import com.gmi.learn.dao.BookDao;
import com.gmi.learn.domain.Book;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class BookDaoImpl implements BookDao {

    private final JdbcTemplate jdbcTemplate;

    public BookDaoImpl(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void create(Book book) {
        System.out.println("ISBN: " + book.getIsbn());
        System.out.println("Title: " + book.getTitle());
        System.out.println("Author ID: " + book.getAuthorId());
       int row= jdbcTemplate.update("Insert into book(isbn, title, authorId) values(?, ?, ?)",
                book.getIsbn(), book.getTitle(), book.getAuthorId()
                );

        System.out.println("rows affected = " + row);
    }
}
