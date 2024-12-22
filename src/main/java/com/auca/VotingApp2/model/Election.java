package com.auca.VotingApp2.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
@Data
@Entity
public class Election {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long electionId;
    private String electionName;
    private String electionDescription;
    private String image;
    private String electionStartDate;
    private String electionEndDate;
    private String electionTime;
    private boolean electionStatus;
    //@JsonIgnore
    @OneToMany(mappedBy = "election", cascade = CascadeType.ALL)

    @JsonBackReference
    //@JsonManagedReference
    private List<Candidate> candidates;

    @OneToMany(mappedBy = "election")
    @JsonIgnore
    private List<Vote> votes;

    public Election() {
    }
}
