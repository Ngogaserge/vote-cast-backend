package com.auca.VotingApp2.repository;

import com.auca.VotingApp2.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {
    Optional<Vote> findByUser_IdAndElection_ElectionId(Long userId, Long electionId);
    @Query("SELECT COUNT(v) FROM Vote v WHERE v.candidate.candidateId = :candidateId")
    Long countVotesByCandidateId(@Param("candidateId") Long candidateId);

}
