package com.auca.VotingApp2.controller;

import com.auca.VotingApp2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class PasswordResetController {

    @Autowired
    private UserService userService;

    // Forgot Password Request
    @PostMapping("/forgot-password")
    public ResponseEntity<String> handleForgotPassword(@RequestParam("email") String email) {
        // Check if the user exists
        if (!userService.doesEmailExist(email)) {
            return ResponseEntity.badRequest().body("Email address not found.");
        }

        // Delete existing token if it exists
        userService.deleteExistingResetTokenByEmail(email);

        // Generate password reset token and send email
        boolean emailSent = userService.sendPasswordResetEmail(email);

        if (emailSent) {
            return ResponseEntity.ok("A reset link has been sent to your email.");
        } else {
            return ResponseEntity.status(500).body("Failed to send email. Please try again.");
        }
    }

    // Validate Reset Token
    @GetMapping("/reset-password")
    public ResponseEntity<String> validateResetToken(@RequestParam("token") String token) {
        boolean isValidToken = userService.validatePasswordResetToken(token);

        if (!isValidToken) {
            return ResponseEntity.badRequest().body("Invalid or expired password reset token.");
        }

        return ResponseEntity.ok("Valid token.");
    }

    // Handle Password Reset
    @PostMapping("/reset-password")
    public ResponseEntity<String> handlePasswordReset(
            @RequestParam("token") String token,
            @RequestParam("newPassword") String newPassword,
            @RequestParam("confirmNewPassword") String confirmNewPassword) {

        // Check if newPassword matches confirmNewPassword
        if (!newPassword.equals(confirmNewPassword)) {
            return ResponseEntity.badRequest().body("Passwords do not match. Please try again.");
        }

        boolean isResetSuccessful = userService.resetUserPassword(token, newPassword);

        if (isResetSuccessful) {
            return ResponseEntity.ok("Your password has been successfully reset.");
        } else {
            return ResponseEntity.status(500).body("Failed to reset password. Please try again.");
        }
    }
}
