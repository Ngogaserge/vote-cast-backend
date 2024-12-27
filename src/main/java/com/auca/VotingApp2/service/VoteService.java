package com.auca.VotingApp2.service;

import com.auca.VotingApp2.exception.ResourceNotFoundException;
import com.auca.VotingApp2.model.Vote;
import com.auca.VotingApp2.model.Election;
import com.auca.VotingApp2.model.Candidate;
import com.auca.VotingApp2.repository.CandidateRepository;
import com.auca.VotingApp2.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Optional;

@Service
public class VoteService {

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private ElectionService electionService;

    @Autowired
    private CandidateService candidateService;

    @Autowired
    private CandidateRepository candidateRepository;

    public List<Vote> getAllVotes() {
        return voteRepository.findAll();
    }

    public Optional<Vote> getVoteById(Long voteId) {
        return voteRepository.findById(voteId);
    }

    public Vote saveVote(Vote vote) {
        return voteRepository.save(vote);
    }

    public void deleteVote(Long voteId) {
        voteRepository.deleteById(voteId);
    }

    public Optional<Vote> findVoteByUserAndElection(Long userId, Long electionId) {
        return voteRepository.findByUser_IdAndElection_ElectionId(userId, electionId);
    }


    public void validateVote(Long userId, Long electionId) {
        // Fetch election (exception is thrown if not found)
        Election election = electionService.getElectionById(electionId);

        // Check if the election is active
        if (!election.isElectionStatus()) {
            throw new IllegalStateException("Election is not active.");
        }

        // Check if the user has already voted in this election
        if (findVoteByUserAndElection(userId, electionId).isPresent()) {
            throw new IllegalStateException("User has already voted in this election.");
        }
    }



    public Long getVoteCountForCandidate(Long candidateId) {
        return voteRepository.countVotesByCandidateId(candidateId);
    }


}
