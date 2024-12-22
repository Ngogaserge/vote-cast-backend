package com.auca.VotingApp2.controller;

import com.auca.VotingApp2.model.Election;
import com.auca.VotingApp2.service.ElectionService;
import com.auca.VotingApp2.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/elections")
@CrossOrigin(origins = "http://localhost:3000") // Adjust origin as per your frontend URL
public class ElectionController {
    @Autowired
    private ElectionService electionService;

    @Autowired
    private EmailService emailService;

    @GetMapping
    public List<Election> getAllElections() {
        return electionService.getAllElections();
    }

//    @GetMapping("/{id}")
//    public Election getElectionById(@PathVariable long id) {
//        return electionService.getElectionById(id).orElseThrow(() -> new RuntimeException("Election not found with id " + id));
//    }
@GetMapping("/{id}")
public Election getElectionById(@PathVariable long id) {
    // Directly call service method (exception is handled inside the service)
    return electionService.getElectionById(id);
}
    @PostMapping("/{id}/announce-winner")
    public String announceWinner(@PathVariable long id, @RequestParam String email) {
        electionService.announceWinner(id, email);
        return "Winner announcement sent to " + email;
    }



    @PostMapping
    public Election createElection(@RequestBody Election election) {
        return electionService.createElection(election);
    }

    @PutMapping("/{id}")
    public Election updateElection(@PathVariable long id, @RequestBody Election updatedElection) {
        return electionService.updateElection(id, updatedElection);
    }

    @DeleteMapping("/{id}")
    public String deleteElection(@PathVariable long id) {
        electionService.deleteElection(id);
        return "Election deleted successfully";
    }

}
