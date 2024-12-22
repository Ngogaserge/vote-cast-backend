package com.auca.VotingApp2.controller;

import com.auca.VotingApp2.dto.CandidateDto;
import com.auca.VotingApp2.dto.CastVoteRequest;
import com.auca.VotingApp2.exception.ResourceNotFoundException;
import com.auca.VotingApp2.model.Candidate;
import com.auca.VotingApp2.model.Election;
import com.auca.VotingApp2.model.User;
import com.auca.VotingApp2.model.Vote;
import com.auca.VotingApp2.service.CandidateService;
import com.auca.VotingApp2.service.ElectionService;
import com.auca.VotingApp2.service.UserService;
import com.auca.VotingApp2.service.VoteService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/votes")
public class VoteController {

    @Autowired
    private VoteService voteService;

    @Autowired
    private UserService userService;

    @Autowired
    private ElectionService electionService;

    @Autowired
    private CandidateService candidateService;

    @GetMapping
    public ResponseEntity<List<Vote>> getAllVotes() {
        return ResponseEntity.ok(voteService.getAllVotes());
    }

    @GetMapping("/{voteId}")
    public ResponseEntity<Vote> getVoteById(@PathVariable Long voteId) {
        return voteService.getVoteById(voteId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/candidate/{candidateId}/votes")
    public ResponseEntity<Long> getVotesCount(@PathVariable Long candidateId) {
        Long voteCount = voteService.getVoteCountForCandidate(candidateId);
        return ResponseEntity.ok(voteCount);
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> castVote(@RequestBody CandidateDto candidateDto, @RequestHeader Map<String, String> headers, HttpSession session) {
        try {
            headers.forEach((key, value) -> System.out.println(key + ": " + value));
            long userId = candidateDto.getUserId();
            System.out.println(userId);
            Candidate candidate = candidateService.getCandidateById(candidateDto.getCandidateId());

            User user= new User();
            user.setId(userId);
            System.out.println(user.getId());
            Vote vote = new Vote();
            System.out.println(candidate.getCandidateId()+" is the candidate id");
            System.out.println(candidate.getElection().getElectionId()+" is the election id");
            vote.setCandidate(candidate);
            Election election = candidate.getElection();
            vote.setElection(election);
            if(user == null){
                return  ResponseEntity.badRequest().body("User is null");
            }
            vote.setUser(user);
            // Validate election and user voting status

            vote.setVoteTime(LocalDateTime.now());
            voteService.saveVote(vote);

            return ResponseEntity.ok("Vote cast successfully.");
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PutMapping("/{voteId}")
    public ResponseEntity<Vote> updateVote(@PathVariable Long voteId, @RequestBody Vote voteDetails) {
        return voteService.getVoteById(voteId)
                .map(existingVote -> {
                    existingVote.setCandidate(voteDetails.getCandidate());
                    existingVote.setElection(voteDetails.getElection());
                    existingVote.setUser(voteDetails.getUser());
                    existingVote.setVoteTime(LocalDateTime.now());
                    voteService.saveVote(existingVote);
                    return ResponseEntity.ok(existingVote);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{voteId}")
    public ResponseEntity<String> deleteVote(@PathVariable Long voteId) {
        voteService.deleteVote(voteId);
        return ResponseEntity.ok("Vote deleted successfully.");
    }
}




//package com.auca.VotingApp2.controller;
//
//import com.auca.VotingApp2.model.Vote;
//import com.auca.VotingApp2.service.VoteService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/api/votes")
//public class VoteController {
//
//    @Autowired
//    private VoteService voteService;
//
//    @GetMapping
//    public ResponseEntity<List<Vote>> getAllVotes() {
//        return ResponseEntity.ok(voteService.getAllVotes());
//    }
//
//    @GetMapping("/{voteId}")
//    public ResponseEntity<Vote> getVoteById(@PathVariable Long voteId) {
//        return voteService.getVoteById(voteId)
//                .map(ResponseEntity::ok)
//                .orElse(ResponseEntity.notFound().build());
//    }
//    @GetMapping("/candidate/{candidateId}/votes")
//    public ResponseEntity<Long> getVotesCount(@PathVariable Long candidateId) {
//        Long voteCount = voteService.getVoteCountForCandidate(candidateId);
//        return ResponseEntity.ok(voteCount);
//    }
//
//
//    @PostMapping(consumes = "application/json", produces = "application/json")
//    public ResponseEntity<String> castVote(@RequestBody Vote vote, @RequestHeader Map<String, String> headers) {
//
//        try {
//            headers.forEach((key, value) -> System.out.println(key + ": " + value));
//            // Validate election and user voting status
//            voteService.validateVote(vote.getUser().getId(), vote.getElection().getElectionId());
//
//            // Save vote
//            vote.setVoteTime(LocalDateTime.now());
//            voteService.saveVote(vote);
//
//            return ResponseEntity.ok("Vote cast successfully.");
//        } catch (IllegalStateException ex) {
//            return ResponseEntity.badRequest().body(ex.getMessage());
//        }
//    }
//
//
//
//    @PutMapping("/{voteId}")
//    public ResponseEntity<Vote> updateVote(@PathVariable Long voteId, @RequestBody Vote voteDetails) {
//        return voteService.getVoteById(voteId)
//                .map(existingVote -> {
//                    existingVote.setCandidate(voteDetails.getCandidate());
//                    existingVote.setElection(voteDetails.getElection());
//                    existingVote.setUser(voteDetails.getUser());
//                    existingVote.setVoteTime(LocalDateTime.now());
//                    voteService.saveVote(existingVote);
//                    return ResponseEntity.ok(existingVote);
//                })
//                .orElse(ResponseEntity.notFound().build());
//    }
//
//    @DeleteMapping("/{voteId}")
//    public ResponseEntity<String> deleteVote(@PathVariable Long voteId) {
//        voteService.deleteVote(voteId);
//        return ResponseEntity.ok("Vote deleted successfully.");
//    }
//}




