package com.auca.VotingApp2.controller;

import com.auca.VotingApp2.dto.PageResponse;
import com.auca.VotingApp2.model.User;
import com.auca.VotingApp2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Enable cross-origin for frontend requests
@RestController
@RequestMapping("/api/admin")
public class AdminUserController {

    @Autowired
    private UserService userService;

    // Add a new user (admin-only)
    @PostMapping("/users")
    public ResponseEntity<String> addUser(@RequestBody User user) {
        userService.registerUser(user);
        return ResponseEntity.ok("User added successfully.");
    }

    // Delete a user (admin-only)
    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully.");
    }

    // Get user by ID (admin-only)
    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    // Update user details (admin-only)
    @PutMapping("/users")
    public ResponseEntity<String> updateUser(@RequestBody User user) {
        userService.updateUser(user);
        return ResponseEntity.ok("User updated successfully.");
    }

    // Search users by username or email (admin-only)
    @GetMapping("/users/search")
    public ResponseEntity<List<User>> searchUsers(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String email
    ) {
        List<User> users = userService.searchUsers(username, email);
        return ResponseEntity.ok(users);
    }

    // Admin Dashboard: List all users (with pagination and sorting)
    @GetMapping("/users")
    public ResponseEntity<PageResponse<User>> getAllUsers(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "id") String sortBy) {

        // Debugging inputs for pagination and sorting
        System.out.println("Fetching users: pageNo=" + pageNo + ", pageSize=" + pageSize + ", sortBy=" + sortBy);

        // Pagination and sorting setup
        var pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        var userPage = userService.getAllUsers(pageable);

        // Debugging the total results fetched
        System.out.println("Fetched " + userPage.getTotalElements() + " users from database.");

        // Return paginated response
        return ResponseEntity.ok(new PageResponse<>(
                userPage.getContent(),
                pageNo,
                userPage.getTotalPages(),
                userPage.getTotalElements()
        ));
    }

    // Other admin-specific methods (upload, download, etc.) can remain the same...
    @GetMapping("/user-role-stats")
    public ResponseEntity<Map<String, Integer>> getUserRoleStatistics() {
        List<User> users = userService.getAllUsers();
        Map<String, Integer> roleStats = new HashMap<>();

        for (User user : users) {
            String role = user.getRole().name();
            roleStats.put(role, roleStats.getOrDefault(role, 0) + 1);
        }

        return ResponseEntity.ok(roleStats);
    }

    @PostMapping("/upload/users")
    public ResponseEntity<String> uploadUsers(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Please select a file to upload.");
        }

        try {
            List<User> userList = new ArrayList<>();
            BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
            String line;
            reader.readLine(); // Skip header line

            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                User user = new User();
                user.setUsername(data[1]);
                user.setFirstName(data[2]);
                user.setLastName(data[3]);
                user.setEmail(data[4]);
                user.setPhoneNumber(data[5]);
                userList.add(user);
            }

            userService.saveAll(userList);
            return ResponseEntity.ok("User file uploaded successfully!");
        } catch (IOException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Failed to upload user file: " + e.getMessage());
        }
    }

    @GetMapping("/download/users")
    public ResponseEntity<ByteArrayResource> downloadUsers() throws IOException {
        List<User> users = userService.getAllUsers();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(outputStream);

        writer.println("ID,Username,Email,First Name,Last Name,Phone Number");
        for (User user : users) {
            writer.printf(
                    "%d,%s,%s,%s,%s,%s%n",
                    user.getId(),
                    user.getUsername(),
                    user.getEmail(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getPhoneNumber()
            );
        }
        writer.flush();
        writer.close();

        ByteArrayResource resource = new ByteArrayResource(outputStream.toByteArray());
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=users.csv");

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(resource.contentLength())
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(resource);
    }

}
