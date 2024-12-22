package com.auca.VotingApp2.controller;

import com.auca.VotingApp2.model.Candidate;
import com.auca.VotingApp2.model.User;
import com.auca.VotingApp2.service.CandidateService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/candidates")
public class CandidateController {
    private final CandidateService candidateService;

    public CandidateController(CandidateService candidateService) {
        this.candidateService = candidateService;
    }

    @PostMapping("/election/{electionId}")
    public ResponseEntity<Candidate> addCandidate(@PathVariable("electionId") Long electionId, @RequestBody Candidate candidate) {
        System.out.println("Received candidate: " + candidate);
        Candidate savedCandidate = candidateService.addCandidate(electionId, candidate);
        return ResponseEntity.ok(savedCandidate);
    }

    @PutMapping("/{candidateId}")
    public ResponseEntity<Candidate> updateCandidate(@PathVariable("candidateId") Long candidateId, @RequestBody Candidate candidateDetails) {
        Candidate updatedCandidate = candidateService.updateCandidate(candidateId, candidateDetails);
        return ResponseEntity.ok(updatedCandidate);
    }

    @DeleteMapping("/{candidateId}")
    public ResponseEntity<Void> deleteCandidate(@PathVariable Long candidateId) {
        candidateService.deleteCandidate(candidateId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<Candidate>> getAllCandidates() {
        List<Candidate> candidates = candidateService.getAllCandidatesWithElection();
        return ResponseEntity.ok(candidates);
    }

    @GetMapping("/election/{electionId}")
    public ResponseEntity<List<Candidate>> getCandidatesByElection(@PathVariable Long electionId) {
        List<Candidate> candidates = candidateService.getCandidatesByElection(electionId);

        return ResponseEntity.ok(candidates);
    }

    @GetMapping("/{candidateId}/votes")
    public ResponseEntity<Long> countVotesForCandidate(@PathVariable Long candidateId) {
        Long voteCount = candidateService.countVotesForCandidate(candidateId);
        return ResponseEntity.ok(voteCount);
    }
}
