package com.quizhub.quiz.repositories;

import com.quizhub.quiz.model.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, UUID> {
    boolean existsByName(String name);

    List<Quiz> findAllByCategoryId(UUID id);

    List<Quiz> findAllByTournamentIdOrderByDateCreatedAsc(UUID tournamentId);

    List<Quiz> findAllByTournamentIdIsNotNull();

    List<Quiz> findAllByTournamentIdIsNull();

    @Query(value = "SELECT * FROM quiz WHERE lower(name) LIKE lower('%' || :name || '%')", nativeQuery = true)
    List<Quiz> getQuizzesByName(String name);

    @Query(value = "SELECT * FROM quiz ORDER BY random() LIMIT 1",nativeQuery = true)
    Optional<Quiz> getRandomQuiz();

    Optional<Quiz> getQuizById(UUID id);

    long countByTournamentId(UUID tournamentId);
}
