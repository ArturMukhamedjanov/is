package islab1.controllers;


import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import islab1.auth.AuthenticationRequest;
import islab1.auth.AuthenticationResponse;
import islab1.auth.RegisterRequest;
import islab1.auth.services.AuthenticationService;
import islab1.models.auth.Role;
import islab1.repos.AdminRequestRepo;
import islab1.repos.UserRepo;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;
    private final UserRepo userRepo;
    private final AdminRequestRepo adminRequestRepo;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @Valid @RequestBody RegisterRequest request) {
        if (userRepo.getUserByUsername(request.getUsername()) != null || adminRequestRepo.getAdminRequestByUsername(request.getUsername()) != null) {
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
        if (userRepo.getUsersByRole(Role.ADMIN).isEmpty()){
            return ResponseEntity.ok(service.registerAdmin(request));
        }
        else{
            if (adminRequestRepo.getAdminRequestByUsername(request.getUsername()) != null) {
                return ResponseEntity.status(400).body(new AuthenticationResponse("User with that name already exists"));
            }
            return ResponseEntity.status(202).body(service.addRegisterAdminRequest(request));
        }
    }

    @PostMapping("/register/accept/{request_id}")
    public ResponseEntity<String> acceptRegistrationRequest(
            @PathVariable("request_id") long requestId) {
        // Логика обработки запроса (можно добавить проверку на наличие request_id в базе)
        
        // Пример возврата строки
        return ResponseEntity.ok("Request with ID " + requestId + " has been accepted.");
    }

}

