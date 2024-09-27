package islab1.controllers;


import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import islab1.auth.AuthenticationRequest;
import islab1.auth.AuthenticationResponse;
import islab1.auth.RegisterRequest;
import islab1.auth.services.AuthenticationService;
import islab1.repos.UserRepo;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;
    private final UserRepo userRepo;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @Valid @RequestBody RegisterRequest request) {
        if (userRepo.getUserByUsername(request.getUsername()) != null) {
            return ResponseEntity.status(400).body(new AuthenticationResponse("User with that name already exists"));
        }
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(service.authenticate(request));
    }

    @PostMapping("/register/admin")
    public ResponseEntity<AuthenticationResponse> registerAdmin(
            @Valid @RequestBody RegisterRequest request) {
        if (userRepo.getUserByUsername(request.getUsername()) != null) {
            return ResponseEntity.status(400).body(new AuthenticationResponse("User with that name already exists"));
        }
        return ResponseEntity.ok(service.registerAdmin(request));
    }
}

