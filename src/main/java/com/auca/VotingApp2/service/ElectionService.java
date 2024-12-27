package com.auca.VotingApp2.service;

import com.auca.VotingApp2.exception.ResourceNotFoundException;
import com.auca.VotingApp2.model.Candidate;
import com.auca.VotingApp2.model.Election;
import com.auca.VotingApp2.repository.ElectionRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Optional;

@Service
public class ElectionService {
    @Autowired
    private ElectionRepository electionRepository;

    @Autowired
    private EmailService emailService;

    public List<Election> getAllElections() {
        return electionRepository.findAll();
    }

//    public Optional<Election> getElectionById(long id) {
//        return electionRepository.findById(id);
//    }
public Election getElectionById(long id) {
    return electionRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Election not found with ID: " + id));
}

    public Election createElection(Election election) {
        return electionRepository.save(election);
    }

    public Election updateElection(long id, Election updatedElection) {
        return electionRepository.findById(id).map(existingElection -> {
            existingElection.setElectionName(updatedElection.getElectionName());
            existingElection.setElectionDescription(updatedElection.getElectionDescription());
            existingElection.setImage(updatedElection.getImage());
            existingElection.setElectionStartDate(updatedElection.getElectionStartDate());
            existingElection.setElectionEndDate(updatedElection.getElectionEndDate());
            existingElection.setElectionTime(updatedElection.getElectionTime());
            existingElection.setElectionStatus(updatedElection.isElectionStatus());
            return electionRepository.save(existingElection);
        }).orElseThrow(() -> new RuntimeException("Election not found with id " + id));
    }
//    public void announceWinner(long electionId, String userEmail) {
//        Election election = getElectionById(electionId);
//        Candidate winner = election.getCandidates().stream()
//                .max((c1, c2) -> Integer.compare(c1.getVotes().size(), c2.getVotes().size()))
//                .orElseThrow(() -> new RuntimeException("No candidates found for election ID: " + electionId));
//
//        String subject = "Election Results for " + election.getElectionName();
//        String text = "The winner of the election " + election.getElectionName() + " is " + winner.getFullName() + " with " + winner.getVotes().size() + " votes.";
//
//        emailService.sendEmail(userEmail, subject, text);
//    }
public void announceWinner(long electionId, String userEmail) {
    Election election = getElectionById(electionId);

    // Debug logging
    System.out.println("Finding winner for election: " + election.getElectionName());

    Candidate winner = election.getCandidates().stream()
            .peek(candidate -> {
                // Debug logging
                System.out.println("Candidate: " + candidate.getFullName() +
                        " has " + (candidate.getVotes() != null ? candidate.getVotes().size() : 0) +
                        " votes");
            })
            .max((c1, c2) -> {
                int votes1 = c1.getVotes() != null ? c1.getVotes().size() : 0;
                int votes2 = c2.getVotes() != null ? c2.getVotes().size() : 0;
                return Integer.compare(votes1, votes2);
            })
            .orElseThrow(() -> new RuntimeException("No candidates found for election ID: " + electionId));

    // Debug logging
    System.out.println("Winner is: " + winner.getFullName() +
            " with " + (winner.getVotes() != null ? winner.getVotes().size() : 0) +
            " votes");

    String subject = "Election Results for " + election.getElectionName();
    String text = String.format("The winner of the election %s is %s with %d votes.",
            election.getElectionName(),
            winner.getFullName(),
            winner.getVotes().size());

    emailService.sendEmail(userEmail, subject, text);
}
    public Election updateElectionStatus(long id, boolean status) {
        return electionRepository.findById(id).map(existingElection -> {
            existingElection.setElectionStatus(status);
            return electionRepository.save(existingElection);
        }).orElseThrow(() -> new RuntimeException("Election not found with id " + id));
    }



    public void deleteElection(long id) {
        electionRepository.deleteById(id);
    }
}
