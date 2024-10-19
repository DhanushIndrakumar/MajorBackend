package com.major.majorproject.controllers;

import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.major.majorproject.DTO.DetailsToken;
import com.major.majorproject.DTO.LoginRequest;
import com.major.majorproject.DTO.LoginResponse;
import com.major.majorproject.DTO.RegisterRequest;
import com.major.majorproject.DTO.RegisterResponse;
import com.major.majorproject.entities.Role;
import com.major.majorproject.entities.User;
import com.major.majorproject.repositories.UserRepository;
import com.major.majorproject.service.JWTService;
import com.major.majorproject.service.UserCommonService;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {AuthController.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class AuthControllerTest {
    @Autowired
    private AuthController authController;

    @MockBean
    private JWTService jWTService;

    @MockBean
    private UserCommonService userCommonService;

    @MockBean
    private UserRepository userRepository;


    @Test
    void testRegister() throws Exception {

        when(userCommonService.register(Mockito.<RegisterRequest>any()))
                .thenReturn(new RegisterResponse(1, "janedoe", "jane.doe@example.org", "6625550144", Role.USER));

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail("jane.doe@example.org");
        registerRequest.setPassword("password");
        registerRequest.setPhone("6625550144");
        registerRequest.setUserName("janedoe");
        String content = (new ObjectMapper()).writeValueAsString(registerRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        // Act and Assert
        MockMvcBuilders.standaloneSetup(authController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"userId\":1,\"userName\":\"janedoe\",\"email\":\"jane.doe@example.org\",\"phone\":\"6625550144\",\"role\":\"USER\"}"));
    }


    @Test
    void testRegister2() throws Exception {
        // Arrange
        when(userCommonService.register(Mockito.<RegisterRequest>any()))
                .thenReturn(new RegisterResponse(1, "janedoe", "jane.doe@example.org", "6625550144", Role.USER));

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail("U.U.U");
        registerRequest.setPassword("password");
        registerRequest.setPhone("6625550144");
        registerRequest.setUserName("janedoe");
        String content = (new ObjectMapper()).writeValueAsString(registerRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        // Act
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(authController).build().perform(requestBuilder);

        // Assert
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "[{\"codes\":[\"Email.registerRequest.email\",\"Email.email\",\"Email.java.lang.String\",\"Email\"],\"arguments\""
                                        + ":[{\"codes\":[\"registerRequest.email\",\"email\"],\"arguments\":null,\"defaultMessage\":\"email\",\"code\":\"email"
                                        + "\"},[],{\"arguments\":null,\"codes\":[\".*\"],\"defaultMessage\":\".*\"}],\"defaultMessage\":\"Email should be"
                                        + " valid\",\"objectName\":\"registerRequest\",\"field\":\"email\",\"rejectedValue\":\"U.U.U\",\"bindingFailure\":false"
                                        + ",\"code\":\"Email\"}]"));
    }


    @Test
    void testGetDetails() throws Exception {

        when(jWTService.extractUserName(Mockito.<String>any())).thenReturn("janedoe");

        User user = new User();
        user.setBookingList(new ArrayList<>());
        user.setEmail("jane.doe@example.org");
        user.setPassword("password");
        user.setPhone("6625550144");
        user.setRole(Role.USER);
        user.setUserId(1);
        user.setUserName("janedoe");
        Optional<User> ofResult = Optional.of(user);
        when(userRepository.findByEmail(Mockito.<String>any())).thenReturn(ofResult);

        DetailsToken detailsToken = new DetailsToken();
        detailsToken.setToken("ABC123");
        String content = (new ObjectMapper()).writeValueAsString(detailsToken);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/auth/getDetailsByToken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);


        MockMvcBuilders.standaloneSetup(authController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"userId\":1,\"userName\":\"janedoe\",\"email\":\"jane.doe@example.org\",\"phone\":\"6625550144\",\"role\":\"USER\"}"));
    }


    @Test
    void testLogin() throws Exception {

        when(userCommonService.login(Mockito.<LoginRequest>any())).thenReturn(new LoginResponse("ABC123"));

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("jane.doe@example.org");
        loginRequest.setPassword("password");
        String content = (new ObjectMapper()).writeValueAsString(loginRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        // Act and Assert
        MockMvcBuilders.standaloneSetup(authController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"token\":\"ABC123\"}"));
    }
}
