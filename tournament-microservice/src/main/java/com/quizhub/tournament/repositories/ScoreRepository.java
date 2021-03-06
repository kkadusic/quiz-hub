package com.quizhub.tournament.repositories;

import com.quizhub.tournament.model.Score;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ScoreRepository extends JpaRepository<Score, UUID> {

    @Query(value = "SELECT * FROM score s " +
            "INNER JOIN person p on p.id = s.person_id " +
            "INNER JOIN quiz q on q.id = s.quiz_id " +
            "INNER JOIN tournament t on t.id = q.tournament_id " +
            "WHERE t.id = :id " +
            "ORDER BY points DESC, total_time, date_created;", nativeQuery = true)
    List<Score> getLeaderboardForTournament(@Param("id") String tournamentId);
}
