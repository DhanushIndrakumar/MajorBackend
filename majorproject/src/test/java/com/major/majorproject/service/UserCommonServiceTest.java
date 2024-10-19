package com.major.majorproject.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.major.majorproject.AuthImplementaion.JWTServiceImpl;
import com.major.majorproject.DTO.RegisterRequest;
import com.major.majorproject.DTO.RegisterResponse;
import com.major.majorproject.entities.Bookings;
import com.major.majorproject.entities.Role;
import com.major.majorproject.entities.User;
import com.major.majorproject.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.access.intercept.RunAsImplAuthenticationProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {UserCommonService.class, PasswordEncoder.class, AuthenticationManager.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class UserCommonServiceTest {
    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JWTService jWTService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserCommonService userCommonService;

    @MockBean
    private UserRepository userRepository;


    @Test
    void testRegister() {

        User user = new User();
        user.setBookingList(new ArrayList<>());
        user.setEmail("jane.doe@example.org");
        user.setPassword("password");
        user.setPhone("6625550144");
        user.setRole(Role.USER);
        user.setUserId(1);
        user.setUserName("janedoe");
        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.save(Mockito.<User>any())).thenReturn(user);

        ArrayList<AuthenticationProvider> providers = new ArrayList<>();
        providers.add(new RunAsImplAuthenticationProvider());
        ProviderManager authenticationManager = new ProviderManager(providers);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        UserCommonService userCommonService = new UserCommonService(userRepository, passwordEncoder, authenticationManager,
                new JWTServiceImpl());


        RegisterResponse actualRegisterResult = userCommonService
                .register(new RegisterRequest("janedoe", "jane.doe@example.org", "password", "6625550144"));


        verify(userRepository).save(isA(User.class));
        assertEquals("6625550144", actualRegisterResult.getPhone());
        assertEquals("jane.doe@example.org", actualRegisterResult.getEmail());
        assertEquals("janedoe", actualRegisterResult.getUserName());
        assertEquals(0, actualRegisterResult.getUserId());
        assertEquals(Role.USER, actualRegisterResult.getRole());
    }


    @Test
    void testRegister2() {

        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.save(Mockito.<User>any())).thenThrow(new UsernameNotFoundException("Msg"));

        ArrayList<AuthenticationProvider> providers = new ArrayList<>();
        providers.add(new RunAsImplAuthenticationProvider());
        ProviderManager authenticationManager = new ProviderManager(providers);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        UserCommonService userCommonService = new UserCommonService(userRepository, passwordEncoder, authenticationManager,
                new JWTServiceImpl());


        assertThrows(UsernameNotFoundException.class, () -> userCommonService
                .register(new RegisterRequest("janedoe", "jane.doe@example.org", "password", "6625550144")));
        verify(userRepository).save(isA(User.class));
    }


    @Test
    void testRemoveUser() {

        UserRepository userRepository = mock(UserRepository.class);
        doNothing().when(userRepository).deleteById(Mockito.<Integer>any());

        ArrayList<AuthenticationProvider> providers = new ArrayList<>();
        providers.add(new RunAsImplAuthenticationProvider());
        ProviderManager authenticationManager = new ProviderManager(providers);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


        (new UserCommonService(userRepository, passwordEncoder, authenticationManager, new JWTServiceImpl())).removeUser(1);

        verify(userRepository).deleteById(eq(1));
    }


    @Test
    void testRemoveUser2() {

        UserRepository userRepository = mock(UserRepository.class);
        doThrow(new UsernameNotFoundException("Msg")).when(userRepository).deleteById(Mockito.<Integer>any());

        ArrayList<AuthenticationProvider> providers = new ArrayList<>();
        providers.add(new RunAsImplAuthenticationProvider());
        ProviderManager authenticationManager = new ProviderManager(providers);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        assertThrows(UsernameNotFoundException.class,
                () -> (new UserCommonService(userRepository, passwordEncoder, authenticationManager, new JWTServiceImpl()))
                        .removeUser(1));
        verify(userRepository).deleteById(eq(1));
    }


    @Test
    void testGetAllUsers() {

        when(userRepository.findAll()).thenReturn(new ArrayList<>());


        List<RegisterResponse> actualAllUsers = userCommonService.getAllUsers();

        verify(userRepository).findAll();
        assertTrue(actualAllUsers.isEmpty());
    }


    @Test
    void testGetAllUsers2() {

        User user = new User();
        user.setBookingList(new ArrayList<>());
        user.setEmail("jane.doe@example.org");
        user.setPassword("password");
        user.setPhone("6625550144");
        user.setRole(Role.USER);
        user.setUserId(1);
        user.setUserName("janedoe");

        ArrayList<User> userList = new ArrayList<>();
        userList.add(user);
        when(userRepository.findAll()).thenReturn(userList);


        List<RegisterResponse> actualAllUsers = userCommonService.getAllUsers();

        verify(userRepository).findAll();
        assertEquals(1, actualAllUsers.size());
        RegisterResponse getResult = actualAllUsers.get(0);
        assertEquals("6625550144", getResult.getPhone());
        assertEquals("jane.doe@example.org", getResult.getEmail());
        assertEquals("janedoe", getResult.getUserName());
        assertEquals(1, getResult.getUserId());
        assertEquals(Role.USER, getResult.getRole());
    }


    @Test
    void testGetAllUsers3() {

        User user = new User();
        user.setBookingList(new ArrayList<>());
        user.setEmail("jane.doe@example.org");
        user.setPassword("password");
        user.setPhone("6625550144");
        user.setRole(Role.USER);
        user.setUserId(1);
        user.setUserName("janedoe");

        User user2 = new User();
        user2.setBookingList(new ArrayList<>());
        user2.setEmail("john.smith@example.org");
        user2.setPassword("Password");
        user2.setPhone("8605550118");
        user2.setRole(Role.ADMIN);
        user2.setUserId(2);
        user2.setUserName("User Name");

        ArrayList<User> userList = new ArrayList<>();
        userList.add(user2);
        userList.add(user);
        when(userRepository.findAll()).thenReturn(userList);


        List<RegisterResponse> actualAllUsers = userCommonService.getAllUsers();


        verify(userRepository).findAll();
        assertEquals(1, actualAllUsers.size());
        RegisterResponse getResult = actualAllUsers.get(0);
        assertEquals("6625550144", getResult.getPhone());
        assertEquals("jane.doe@example.org", getResult.getEmail());
        assertEquals("janedoe", getResult.getUserName());
        assertEquals(1, getResult.getUserId());
        assertEquals(Role.USER, getResult.getRole());
    }


    @Test
    void testGetAllUsers4() {
        when(userRepository.findAll()).thenThrow(new UsernameNotFoundException("Msg"));
        assertThrows(UsernameNotFoundException.class, () -> userCommonService.getAllUsers());
        verify(userRepository).findAll();
    }


    @Test
    void testGetAllUsers5() {

        User user = mock(User.class);
        when(user.getUserId()).thenThrow(new IllegalArgumentException("foo"));
        when(user.getRole()).thenReturn(Role.USER);
        doNothing().when(user).setBookingList(Mockito.<List<Bookings>>any());
        doNothing().when(user).setEmail(Mockito.<String>any());
        doNothing().when(user).setPassword(Mockito.<String>any());
        doNothing().when(user).setPhone(Mockito.<String>any());
        doNothing().when(user).setRole(Mockito.<Role>any());
        doNothing().when(user).setUserId(anyInt());
        doNothing().when(user).setUserName(Mockito.<String>any());
        user.setBookingList(new ArrayList<>());
        user.setEmail("jane.doe@example.org");
        user.setPassword("password");
        user.setPhone("6625550144");
        user.setRole(Role.USER);
        user.setUserId(1);
        user.setUserName("janedoe");

        ArrayList<User> userList = new ArrayList<>();
        userList.add(user);
        when(userRepository.findAll()).thenReturn(userList);


        assertThrows(IllegalArgumentException.class, () -> userCommonService.getAllUsers());
        verify(user).getRole();
        verify(user).getUserId();
        verify(user).setBookingList(isA(List.class));
        verify(user).setEmail(eq("jane.doe@example.org"));
        verify(user).setPassword(eq("password"));
        verify(user).setPhone(eq("6625550144"));
        verify(user).setRole(eq(Role.USER));
        verify(user).setUserId(eq(1));
        verify(user).setUserName(eq("janedoe"));
        verify(userRepository).findAll();
    }

    @Test
    void testGetUser() {

        User user = new User();
        user.setBookingList(new ArrayList<>());
        user.setEmail("jane.doe@example.org");
        user.setPassword("password");
        user.setPhone("6625550144");
        user.setRole(Role.USER);
        user.setUserId(1);
        user.setUserName("janedoe");
        Optional<User> ofResult = Optional.of(user);
        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult);

        ArrayList<AuthenticationProvider> providers = new ArrayList<>();
        providers.add(new RunAsImplAuthenticationProvider());
        ProviderManager authenticationManager = new ProviderManager(providers);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


        RegisterResponse actualUser = (new UserCommonService(userRepository, passwordEncoder, authenticationManager,
                new JWTServiceImpl())).getUser(1);

        verify(userRepository).findById(eq(1));
        assertEquals("6625550144", actualUser.getPhone());
        assertEquals("jane.doe@example.org", actualUser.getEmail());
        assertEquals("janedoe", actualUser.getUserName());
        assertEquals(1, actualUser.getUserId());
        assertEquals(Role.USER, actualUser.getRole());
    }


    @Test
    void testGetUser2() {

        UserRepository userRepository = mock(UserRepository.class);
        Optional<User> emptyResult = Optional.empty();
        when(userRepository.findById(Mockito.<Integer>any())).thenReturn(emptyResult);

        ArrayList<AuthenticationProvider> providers = new ArrayList<>();
        providers.add(new RunAsImplAuthenticationProvider());
        ProviderManager authenticationManager = new ProviderManager(providers);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        assertThrows(UsernameNotFoundException.class,
                () -> (new UserCommonService(userRepository, passwordEncoder, authenticationManager, new JWTServiceImpl()))
                        .getUser(1));
        verify(userRepository).findById(eq(1));
    }


    @Test
    void testGetUser3() {

        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.findById(Mockito.<Integer>any())).thenThrow(new UsernameNotFoundException("Invalid user"));

        ArrayList<AuthenticationProvider> providers = new ArrayList<>();
        providers.add(new RunAsImplAuthenticationProvider());
        ProviderManager authenticationManager = new ProviderManager(providers);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        assertThrows(UsernameNotFoundException.class,
                () -> (new UserCommonService(userRepository, passwordEncoder, authenticationManager, new JWTServiceImpl()))
                        .getUser(1));
        verify(userRepository).findById(eq(1));
    }


    @Test
    void testUpdateUser() {

        User user = new User();
        user.setBookingList(new ArrayList<>());
        user.setEmail("jane.doe@example.org");
        user.setPassword("password");
        user.setPhone("6625550144");
        user.setRole(Role.USER);
        user.setUserId(1);
        user.setUserName("janedoe");
        Optional<User> ofResult = Optional.of(user);

        User user2 = new User();
        user2.setBookingList(new ArrayList<>());
        user2.setEmail("jane.doe@example.org");
        user2.setPassword("password");
        user2.setPhone("6625550144");
        user2.setRole(Role.USER);
        user2.setUserId(1);
        user2.setUserName("janedoe");
        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.save(Mockito.<User>any())).thenReturn(user2);
        when(userRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult);

        ArrayList<AuthenticationProvider> providers = new ArrayList<>();
        providers.add(new RunAsImplAuthenticationProvider());
        ProviderManager authenticationManager = new ProviderManager(providers);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        UserCommonService userCommonService = new UserCommonService(userRepository, passwordEncoder, authenticationManager,
                new JWTServiceImpl());


        RegisterResponse actualUpdateUserResult = userCommonService
                .updateUser(new RegisterRequest("janedoe", "jane.doe@example.org", "password", "6625550144"), 1);


        verify(userRepository).findById(eq(1));
        verify(userRepository).save(isA(User.class));
        assertEquals("6625550144", actualUpdateUserResult.getPhone());
        assertEquals("jane.doe@example.org", actualUpdateUserResult.getEmail());
        assertEquals("janedoe", actualUpdateUserResult.getUserName());
        assertEquals(1, actualUpdateUserResult.getUserId());
        assertEquals(Role.USER, actualUpdateUserResult.getRole());
    }


    @Test
    void testUpdateUser2() {

        User user = new User();
        user.setBookingList(new ArrayList<>());
        user.setEmail("jane.doe@example.org");
        user.setPassword("password");
        user.setPhone("6625550144");
        user.setRole(Role.USER);
        user.setUserId(1);
        user.setUserName("janedoe");
        Optional<User> ofResult = Optional.of(user);
        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.save(Mockito.<User>any())).thenThrow(new UsernameNotFoundException("Msg"));
        when(userRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult);

        ArrayList<AuthenticationProvider> providers = new ArrayList<>();
        providers.add(new RunAsImplAuthenticationProvider());
        ProviderManager authenticationManager = new ProviderManager(providers);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        UserCommonService userCommonService = new UserCommonService(userRepository, passwordEncoder, authenticationManager,
                new JWTServiceImpl());

        // Act and Assert
        assertThrows(UsernameNotFoundException.class, () -> userCommonService
                .updateUser(new RegisterRequest("janedoe", "jane.doe@example.org", "password", "6625550144"), 1));
        verify(userRepository).findById(eq(1));
        verify(userRepository).save(isA(User.class));
    }


    @Test
    void testUpdateUser3() {

        UserRepository userRepository = mock(UserRepository.class);
        Optional<User> emptyResult = Optional.empty();
        when(userRepository.findById(Mockito.<Integer>any())).thenReturn(emptyResult);

        ArrayList<AuthenticationProvider> providers = new ArrayList<>();
        providers.add(new RunAsImplAuthenticationProvider());
        ProviderManager authenticationManager = new ProviderManager(providers);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        UserCommonService userCommonService = new UserCommonService(userRepository, passwordEncoder, authenticationManager,
                new JWTServiceImpl());


        assertThrows(UsernameNotFoundException.class, () -> userCommonService
                .updateUser(new RegisterRequest("janedoe", "jane.doe@example.org", "password", "6625550144"), 1));
        verify(userRepository).findById(eq(1));
    }


}
