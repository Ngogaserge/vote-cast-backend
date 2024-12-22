package com.auca.VotingApp2.dto;

import com.auca.VotingApp2.model.Role;
import lombok.Data;

@Data
public class UserResponse {

    private Long userId; // New field for user ID
    private String username;
    private Role role;

    public UserResponse(Long userId, String username, Role role) {
        this.userId = userId;
        this.username = username;
        this.role = role;
    }
}
