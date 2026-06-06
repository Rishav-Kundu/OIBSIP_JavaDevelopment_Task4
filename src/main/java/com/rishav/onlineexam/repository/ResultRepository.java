package com.rishav.onlineexam.repository;

import com.rishav.onlineexam.entity.Result;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ResultRepository
        extends JpaRepository<Result, Long> {

    Optional<Result> findTopByUsernameOrderByIdDesc(String username);
}
