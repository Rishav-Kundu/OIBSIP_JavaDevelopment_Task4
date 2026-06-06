package com.rishav.onlineexam.repository;

import com.rishav.onlineexam.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository
        extends JpaRepository<Question, Long> {
}
