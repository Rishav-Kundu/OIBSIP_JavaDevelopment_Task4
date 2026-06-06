package com.rishav.onlineexam;

import com.rishav.onlineexam.entity.Question;
import com.rishav.onlineexam.entity.User;
import com.rishav.onlineexam.repository.QuestionRepository;
import com.rishav.onlineexam.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;
    private final PasswordEncoder passwordEncoder;

    public DataLoader(UserRepository userRepository,
                      QuestionRepository questionRepository,
                      PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.questionRepository = questionRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {

        if (userRepository.count() == 0) {

            userRepository.save(new User(
                    "student1",
                    passwordEncoder.encode("pass123"),
                    "Student One",
                    "",
                    "",
                    "101",
                    "ROLE_STUDENT"
            ));

            userRepository.save(new User(
                    "student2",
                    passwordEncoder.encode("pass123"),
                    "Student Two",
                    "",
                    "",
                    "102",
                    "ROLE_STUDENT"
            ));

            userRepository.save(new User(
                    "student3",
                    passwordEncoder.encode("pass123"),
                    "Student Three",
                    "",
                    "",
                    "103",
                    "ROLE_STUDENT"
            ));

            userRepository.save(new User(
                    "student4",
                    passwordEncoder.encode("pass123"),
                    "Student Four",
                    "",
                    "",
                    "104",
                    "ROLE_STUDENT"
            ));

            userRepository.save(new User(
                    "student5",
                    passwordEncoder.encode("pass123"),
                    "Student Five",
                    "",
                    "",
                    "105",
                    "ROLE_STUDENT"
            ));
        }

        if (questionRepository.count() == 0) {

            questionRepository.save(new Question(
                    "What is Java?",
                    "Programming Language",
                    "Coffee",
                    "Animal",
                    "City",
                    "Programming Language"));

            questionRepository.save(new Question(
                    "Which keyword is used for inheritance?",
                    "extends",
                    "include",
                    "using",
                    "import",
                    "extends"));

            questionRepository.save(new Question(
                    "Which method starts execution?",
                    "main",
                    "run",
                    "start",
                    "execute",
                    "main"));

            questionRepository.save(new Question(
                    "Which collection allows duplicates?",
                    "List",
                    "Set",
                    "Map",
                    "Tree",
                    "List"));

            questionRepository.save(new Question(
                    "Java is platform?",
                    "Independent",
                    "Dependent",
                    "Specific",
                    "Windows Only",
                    "Independent"));

            questionRepository.save(new Question(
                    "Which package contains List?",
                    "java.util",
                    "java.io",
                    "java.net",
                    "java.sql",
                    "java.util"));

            questionRepository.save(new Question(
                    "Which loop executes at least once?",
                    "do while",
                    "while",
                    "for",
                    "foreach",
                    "do while"));

            questionRepository.save(new Question(
                    "Which is not primitive?",
                    "String",
                    "int",
                    "char",
                    "double",
                    "String"));

            questionRepository.save(new Question(
                    "Size of int?",
                    "4",
                    "2",
                    "8",
                    "16",
                    "4"));

            questionRepository.save(new Question(
                    "Java creator?",
                    "James Gosling",
                    "Dennis Ritchie",
                    "Guido",
                    "Linus",
                    "James Gosling"));
        }
    }
}