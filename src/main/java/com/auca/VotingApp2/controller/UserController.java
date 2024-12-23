package com.auca.VotingApp2.controller;

import com.auca.VotingApp2.dto.PageResponse;
import com.auca.VotingApp2.dto.UserResponse;
import com.auca.VotingApp2.model.Role;
import com.auca.VotingApp2.model.User;
import com.auca.VotingApp2.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // Home endpoint (can be used for health checks)
    @GetMapping("/home")
    public ResponseEntity<String> home() {
        return ResponseEntity.ok("Welcome to the Voting Application API!");
    }

    // Register a new user
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        user.setRole(Role.ROLE_USER);
        userService.registerUser(user);
        return ResponseEntity.ok("Registration successful!");
    }

    // Login a user
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestParam String username, @RequestParam String password, HttpSession session) {
        try {
            System.out.println("Attempting to log in user: " + username);

            User user = userService.loginUser(username);
            if (user == null) {
                System.out.println("User not found: " + username);
                return ResponseEntity.badRequest().body("Invalid username or password.");
            }

            session.setAttribute("user", user);
            User luser = (User) session.getAttribute("user");
            System.out.println(luser.getId());

            if (!user.getPassword().equals(password)) {
                System.out.println("Invalid password for user: " + username);
                return ResponseEntity.badRequest().body("Invalid username or password.");
            }

            return ResponseEntity.ok(new UserResponse(user.getId(), user.getUsername(), user.getRole()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("An error occurred: " + e.getMessage());
        }
    }

    // Logout endpoint (invalidate session, token, etc.)
    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        System.out.println("User logged out.");
        // Clear authentication state if using token-based authentication
        return ResponseEntity.ok("You are logged out.");
    }
}


