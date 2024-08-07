package com.lms.usermanagement.service;
import java.lang.reflect.Method;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.lms.usermanagement.model.LoginRequest;
import com.lms.usermanagement.model.User;
import com.lms.usermanagement.model.UserRegistrationRequest;
import com.lms.usermanagement.repository.UserRepository;
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterUser() {
        UserRegistrationRequest request = new UserRegistrationRequest();
        request.setUsername("testuser");
        request.setPassword("password");
        request.setFullName("Test User");
        request.setEmail("testuser@example.com");

        User user = new User(request.getUsername(),
                             passwordEncoder.encode(request.getPassword()),
                             request.getFullName(),
                             request.getEmail());

        when(passwordEncoder.encode(request.getPassword())).thenReturn(user.getPassword());
        when(userRepository.save(any(User.class))).thenReturn(user);

        userService.registerUser(request);

        verify(userRepository).save(any(User.class));
    }

   @Test
void testLoginUser() throws Exception {
    LoginRequest request = new LoginRequest();
    request.setUsername("testuser");
    request.setPassword("password");

    User user = new User(request.getUsername(),
                         passwordEncoder.encode(request.getPassword()),
                         "Test User",
                         "testuser@example.com");

    when(userRepository.findByUsername(request.getUsername())).thenReturn(Optional.of(user));
    when(passwordEncoder.matches(request.getPassword(), user.getPassword())).thenReturn(true);

    // Use reflection to call the protected method
    Method method = UserService.class.getDeclaredMethod("generateToken", User.class);
    method.setAccessible(true); // Allow access to the protected method
    String token = (String) method.invoke(userService, user);

    assertNotNull(token); // Check that the token is not null

    // Validate token structure or contents if necessary
    String[] parts = token.split("\\.");
    assertEquals(3, parts.length, "Token should have 3 parts separated by '.'");
}

}
