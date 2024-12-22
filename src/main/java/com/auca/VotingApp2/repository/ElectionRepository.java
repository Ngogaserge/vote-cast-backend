package com.auca.VotingApp2.repository;

import com.auca.VotingApp2.model.Election;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ElectionRepository extends JpaRepository<Election, Long> {


}