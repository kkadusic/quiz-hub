package com.quizhub.quiz.controllers;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.quizhub.quiz.exceptions.BadRequestException;
import com.quizhub.quiz.exceptions.ConflictException;
import com.quizhub.quiz.model.Quiz;
import com.quizhub.quiz.response.UpdateQuizModel;
import com.quizhub.quiz.services.QuizService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/quizzes")
public class QuizController {

    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @GetMapping(path = "/all")
    public ResponseEntity<List<Quiz>> getAllQuizzes(@RequestParam(required = false) Boolean tournament) {
        return ResponseEntity.ok(quizService.getAllQuizzes(tournament));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Quiz>> getQuizzesByName(@RequestParam String name) {
        return ResponseEntity.ok(quizService.getQuizzesByName(name));
    }

    @GetMapping("/category")
    public ResponseEntity<List<Quiz>> getQuizzesByCategory(@RequestParam UUID id) {
        return ResponseEntity.ok(quizService.getQuizzesByCategory(id));
    }

    @PostMapping("/update-quiz")
    public ResponseEntity<String> updateQuiz(@RequestBody UpdateQuizModel updateQuizModel) {
        quizService.createUpdateQuiz(updateQuizModel, true);
        return ResponseEntity.ok("Quiz has been successfully updated!");
    }

    @PostMapping("/create-quiz")
    public ResponseEntity<String> createQuiz(@RequestBody UpdateQuizModel updateQuizModel) {
        quizService.createUpdateQuiz(updateQuizModel, false);
        return ResponseEntity.ok("Quiz has been successfully created!");
    }

    @GetMapping("/random")
    public ResponseEntity<Quiz> getRandomQuiz() {
        return ResponseEntity.ok(quizService.getRandomQuiz());
    }

    @GetMapping
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Bad request", response = BadRequestException.class),
    })
    public ResponseEntity<Quiz> getQuiz(@RequestParam UUID id) {
        return ResponseEntity.ok(quizService.getQuiz(id));
    }

    @PostMapping
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Bad request", response = BadRequestException.class),
            @ApiResponse(code = 409, message = "Conflict", response = ConflictException.class),
    })
    public ResponseEntity<Quiz> add(@RequestBody @Valid Quiz quiz) {
        return ResponseEntity.ok(quizService.add(quiz));
    }

    @DeleteMapping
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Bad request", response = BadRequestException.class),
    })
    public ResponseEntity<JSONObject> deleteQuizById (@RequestParam UUID id) {
        return new ResponseEntity<>(quizService.deleteQuizById(id), HttpStatus.OK);
    }

    @PostMapping("/tournament")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Bad request", response = BadRequestException.class),
    })
    public ResponseEntity<Quiz> addQuizFromTournament(@RequestBody @Valid TournamentQuiz tournamentQuiz) {
        return ResponseEntity.ok(quizService.addQuizFromTournament(tournamentQuiz));
    }

    @GetMapping("/tournament")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Bad request", response = BadRequestException.class),
    })
    public ResponseEntity<List<Quiz>> getQuizzesForTournament(@RequestParam UUID id) {
        return ResponseEntity.ok(quizService.getQuizzesForTournament(id));
    }

    public static class TournamentQuiz {
        private final String tournamentId;
        private final Question[] questions;

        public TournamentQuiz(@JsonProperty("tournamentId") String tournamentId,
                              @JsonProperty("questions") Question[] questions) {
            this.tournamentId = tournamentId;
            this.questions = questions;
        }

        public String getTournamentId() {
            return tournamentId;
        }

        public Question[] getQuestions() {
            return questions;
        }

        public int getQuestionsLength() {
            return questions.length;
        }

        public static class Question {
            private final String name;
            private final String correctAnswer;
            private final String[] incorrectAnswers;

            public Question(@JsonProperty("name") String name,
                            @JsonProperty("correctAnswer") String correctAnswer,
                            @JsonProperty("incorrectAnswers") String[] incorrectAnswers) {
                this.name = name;
                this.correctAnswer = correctAnswer;
                this.incorrectAnswers = incorrectAnswers;
            }

            public String getName() {
                return name;
            }

            public String getCorrectAnswer() {
                return correctAnswer;
            }

            public String[] getIncorrectAnswers() {
                return incorrectAnswers;
            }
        }
    }
}

