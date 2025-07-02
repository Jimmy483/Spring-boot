package com.gmi.learn.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Messages {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @Column(name="receiverId")
    long receiverId;

    @Column(name="senderId")
    long senderId;


    String content;

    @Column(name="isRead")
    Boolean isRead;

    @Column(name="lastRead")
    Date lastRead;
}
