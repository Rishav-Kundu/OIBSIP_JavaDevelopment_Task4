package com.rishav.onlineexam.service;

import com.rishav.onlineexam.entity.Question;
import com.rishav.onlineexam.entity.Result;
import com.rishav.onlineexam.repository.QuestionRepository;
import com.rishav.onlineexam.repository.ResultRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class ExamService {

    private final QuestionRepository questionRepository;
    private final ResultRepository resultRepository;

    public ExamService(QuestionRepository questionRepository,
                       ResultRepository resultRepository) {

        this.questionRepository = questionRepository;
        this.resultRepository = resultRepository;
    }

    public List<Question> getRandomQuestions(int count) {

        List<Question> questions = questionRepository.findAll();

        Collections.shuffle(questions);

        if (questions.size() <= count) {
            return questions;
        }

        return questions.subList(0, count);
    }

    public Question findById(Long id) {
        return questionRepository.findById(id).orElse(null);
    }

    public Result saveResult(Result result) {
        return resultRepository.save(result);
    }

    public Result getLatestResult(String username) {
        return resultRepository
                .findTopByUsernameOrderByIdDesc(username)
                .orElse(null);
    }
}
