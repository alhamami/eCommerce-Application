package com.example.demo.controller;

import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import java.util.Optional;
import static org.junit.Assert.assertEquals;

public class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);
    @Mock
    private UserRepository userRepository = mock(UserRepository.class);


    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void testFindUserByIdReturnsUserDetailsWhenUserIsFound() {

        User newUser = new User();

        newUser.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(newUser));

        ResponseEntity<User> response = userController.findById(1L);

        assertNotNull(response);

        assertNotNull(response.getBody());

        assertNotNull(response.getStatusCodeValue());

        assertEquals(200, response.getStatusCodeValue());

        assertEquals(newUser, response.getBody());
    }


    @Test
    public void testLoginReturnsUserWhenUserFound() {

        User newUser = new User();

        newUser.setUsername("jalal");

        newUser.setPassword("jalal123Hashed");

        User user = userRepository.findByUsername(newUser.getUsername());

        when(user).thenReturn(newUser);

        ResponseEntity<User> response = userController.findByUserName("jalal");

        assertNotNull(response.getBody());

        assertNotNull(response.getBody().getUsername());

        assertNotNull(response.getStatusCodeValue());

        assertEquals("jalal", response.getBody().getUsername());

        assertEquals(200, response.getStatusCodeValue());

        verify(userRepository, times(1)).findByUsername("jalal");

    }


    @Test
    public void testFindUserByUsernameReturnsUserDetailsWhenUserIsFound() {

        User newUser = new User();

        newUser.setUsername("jalal");

        User user = userRepository.findByUsername("jalal");

        when(user).thenReturn(newUser);

        ResponseEntity<User> response = userController.findByUserName("jalal");

        assertNotNull(response);

        assertNotNull(response.getBody());

        assertEquals(newUser, response.getBody());

        assertNotNull(response.getStatusCodeValue());

        assertEquals(200, response.getStatusCodeValue());

    }


    @Test
    public void testNewUserPasswordReturnsHashedPasswordWhenRequestIsValid() {

        CreateUserRequest  createUserRequest = new CreateUserRequest();

        createUserRequest.setUsername("jalal");

        createUserRequest.setPassword("jalal123");

        createUserRequest.setConfirmPassword("jalal123");

        CharSequence rawPassword = encoder.encode(anyString());

        when(rawPassword).thenReturn("jalal123Hashed");

        final ResponseEntity<User> response = userController.createUser(createUserRequest);

        assertNotNull(response);

        assertNotNull(response.getBody());

        assertNotNull(response.getStatusCodeValue());

        assertNotNull(response.getBody().getId());

        assertNotNull(response.getBody().getPassword());

        assertEquals(200, response.getStatusCodeValue());

        assertNotNull(response.getBody().getUsername());

        assertEquals("jalal", response.getBody().getUsername());

        assertEquals(0, response.getBody().getId());

        assertEquals("jalal123Hashed", response.getBody().getPassword());

        verify(encoder, times(1)).encode("jalal123");
    }

}
