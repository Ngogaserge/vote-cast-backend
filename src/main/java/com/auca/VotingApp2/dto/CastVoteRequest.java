package com.auca.VotingApp2.dto;

import lombok.Data;

@Data
public class CastVoteRequest {

    private Long userId;        // ID of the user casting the vote
    private Long electionId;    // ID of the election being voted in
    private Long candidateId;   // ID of the chosen candidate


    public CastVoteRequest() {}

    public CastVoteRequest(Long userId, Long electionId, Long candidateId) {
        this.userId = userId;
        this.electionId = electionId;
        this.candidateId = candidateId;
    }
}
