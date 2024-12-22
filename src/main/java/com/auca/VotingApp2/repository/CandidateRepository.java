package com.auca.VotingApp2.repository;

import com.auca.VotingApp2.model.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CandidateRepository extends JpaRepository<Candidate, Long> {
    List<Candidate> findByElection_ElectionId(Long electionId);

    @Query("SELECT COUNT(v) FROM Vote v WHERE v.candidate.candidateId = :candidateId")
    Long countVotesByCandidateId(@Param("candidateId") Long candidateId);

    @Query("SELECT c FROM Candidate c JOIN FETCH c.election")
    List<Candidate> findAllWithElection(); // Fetch candidates with elections
}
