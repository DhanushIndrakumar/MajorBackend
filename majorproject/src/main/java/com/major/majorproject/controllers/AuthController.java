package com.major.majorproject.controllers;

import com.major.majorproject.DTO.LoginRequest;
import com.major.majorproject.DTO.RegisterRequest;
import com.major.majorproject.repositories.UserRepository;
import com.major.majorproject.service.JWTService;
import com.major.majorproject.service.UserCommonService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/auth")
public class AuthController {

    private final UserCommonService userCommonService;

    private final JWTService jwtService;

    private final UserRepository userRepository;

    @Operation(
            description = "End point to register a user",
            summary="A user can register by providing the required details"
    )
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest registerRequest, BindingResult result){
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        return ResponseEntity.ok(userCommonService.register(registerRequest));
    }

    @Operation(
            description = "End point to login",
            summary="If the user is registered and tries to login with Valid Username and password, token and refreshToken is generated"
    )
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest){
        return ResponseEntity.ok(userCommonService.login(loginRequest));
    }
}
