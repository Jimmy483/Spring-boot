package com.gmi.learn.repository;

import com.gmi.learn.domain.UserCreateStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserCreateStatusRepository extends JpaRepository<UserCreateStatus, Long> {

    Optional<UserCreateStatus> findByStatus(String status);
    Optional<UserCreateStatus> findByUrlAndStatus(String url, String status);
    List<UserCreateStatus> findAll();
}
