package com.major.majorproject.service;

import com.major.majorproject.DTO.LoginRequest;
import com.major.majorproject.DTO.LoginResponse;
import com.major.majorproject.DTO.RegisterRequest;
import com.major.majorproject.DTO.RegisterResponse;
import com.major.majorproject.entities.Role;
import com.major.majorproject.entities.User;
import com.major.majorproject.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserCommonService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JWTService jwtService;


    public RegisterResponse register(RegisterRequest registerRequest) {
        User user=new User();
        user.setUserName(registerRequest.getUserName());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setPhone(registerRequest.getPhone());
        user.setRole(Role.USER);
        userRepository.save(user);

        RegisterResponse registerResponse=new RegisterResponse();
        registerResponse.setUserId(user.getUserId());
        registerResponse.setUserName(registerRequest.getUserName());
        registerResponse.setEmail(registerRequest.getEmail());
        registerResponse.setPhone(registerRequest.getPhone());
        registerResponse.setRole(Role.USER);
        return registerResponse;
    }

    public LoginResponse login(LoginRequest loginRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),
                loginRequest.getPassword()));//user name and password is correct

        User user=userRepository.findByEmail(loginRequest.getEmail())//Get the user
                .orElseThrow(()->new IllegalArgumentException("Invalid email or password"));
        var jwt=jwtService.generateToken(user);//generate token
        LoginResponse loginResponse=new LoginResponse();
        loginResponse.setToken(jwt);
        return loginResponse;
    }


    public void removeUser(int userId) {
        userRepository.deleteById(userId);
    }

    public List<RegisterResponse> getAllUsers() {
        List<User> users= userRepository.findAll();
        List<User> newUsers=new ArrayList<>();
        for(User u:users){
            if(u.getRole().equals(Role.USER)){
                newUsers.add(u);
            }
        }
        List<RegisterResponse> registerResponses=new ArrayList<>();
        for(User user:newUsers){
            RegisterResponse registerResponse=new RegisterResponse();
            registerResponse.setUserId(user.getUserId());
            registerResponse.setUserName(user.getUserName());
            registerResponse.setEmail(user.getEmail());
            registerResponse.setRole(user.getRole());
            registerResponse.setPhone(user.getPhone());
            registerResponses.add(registerResponse);
        }
        return registerResponses;
    }

    public RegisterResponse getUser(int userId) {
        User user=userRepository.findById(userId)
                .orElseThrow(()->new UsernameNotFoundException("Invalid user"));
        RegisterResponse registerResponse=new RegisterResponse();
        registerResponse.setUserId(user.getUserId());
        registerResponse.setUserName(user.getUserName());
        registerResponse.setEmail(user.getEmail());
        registerResponse.setRole(user.getRole());
        registerResponse.setPhone(user.getPhone());

        return registerResponse;
    }

    public RegisterResponse updateUser(RegisterRequest registerRequest, int userId) {
        User user=userRepository.findById(userId)
                .orElseThrow(()-> new UsernameNotFoundException("Invalid Username"));

        user.setUserName(registerRequest.getUserName());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(registerRequest.getPassword());
        user.setPhone(registerRequest.getPhone());
        User user1= userRepository.save(user);
        RegisterResponse registerResponse=new RegisterResponse();
        registerResponse.setUserId(user1.getUserId());
        registerResponse.setUserName(user1.getUserName());
        registerResponse.setEmail(user1.getEmail());
        registerResponse.setRole(user1.getRole());
        registerResponse.setPhone(user1.getPhone());


        return registerResponse;
    }

}
