package com.auca.VotingApp2.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Data
@Entity
public class Candidate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long candidateId;
    private String fullName;
    private String party;
    public String image;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "electionId")
    //@JsonManagedReference
    //@JsonBackReference
    private Election election;

    @OneToMany(mappedBy = "candidate", cascade = CascadeType.ALL)
//    @JsonManagedReference
    private Set<Vote> votes;

    public Candidate() {
    }
}
