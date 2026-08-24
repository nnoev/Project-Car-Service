package com.example.car_service.user;

import com.example.car_service.exceptions.DuplicateException;
import com.example.car_service.exceptions.NothingFoundException;
import com.example.car_service.exceptions.UnauthorizedActionException;
import com.example.car_service.security.UserData;
import com.example.car_service.user.model.User;
import com.example.car_service.user.model.UserRole;
import com.example.car_service.user.repo.UserRepository;
import com.example.car_service.user.service.UserService;
import com.example.car_service.web.dtos.UserRegistration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceUTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void whenRegisteringUser_andUserAlreadyExists_thenThrowException() {
        UserRegistration dto = new UserRegistration();
        dto.setUsername("username");
        dto.setEmail("user@car-service.com");
        User user = User.builder()
                .id(UUID.randomUUID())
                .username("username")
                .email("user@car-service.com")
                .build();
        when(userRepository.findByUsername(any())).thenReturn(Optional.of(user));
        assertThrows(DuplicateException.class, () -> userService.registerUser(dto));

    }

    @Test
    void whenRegisteringUser_andEmailAlreadyExists_thenThrowException() {
        UserRegistration dto = new UserRegistration();
        dto.setUsername("newUsername");
        dto.setEmail("user@car-service.com");
        User user = User.builder()
                .id(UUID.randomUUID())
                .username("existingUsername")
                .email("user@car-service.com")
                .build();
        when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.of(user));
        assertThrows(DuplicateException.class, () -> userService.registerUser(dto));

    }

    @Test
    void whenUserServiceRegisterNewUser_thenSaveInvoke() {
        UserRegistration dto = UserRegistration.builder()
                .username("newUsername")
                .firstName("firstName")
                .lastName("lastName")
                .password("password")
                .email("newUser@carservice.com")
                .build();
        userService.registerUser(dto);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void whenLookForUserById_andUserIsAbsent_thenThrowException() {
        UUID id = UUID.randomUUID();
        when(userRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(NothingFoundException.class, () -> userService.getById(id));
    }

    @Test
    void whenSaveUser_thenSaveInvoke() {
        User user = User.builder().id(UUID.randomUUID()).build();
        userService.save(user);
        verify(userRepository).save(user);
    }

    @Test
    void whenDeleteUser_thenDeleteInvoke() {
        User user = User.builder().id(UUID.randomUUID()).build();
        userService.deleteUser(user);
        verify(userRepository).delete(user);
    }

    @Test
    void whenGettingAllUsers_thenFindAllInvoke() {
        userService.getAllUsers();
        verify(userRepository).findAll();
    }

    @Test
    void whenChangingUserRole_andTheUserRoleIsAdmin_thenExceptionIsThrown() {
        User user = User.builder()
                .id(UUID.randomUUID())
                .role(UserRole.ADMIN)
                .build();
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        assertThrows(UnauthorizedActionException.class, () -> userService.changeRole(user.getId(), UserRole.USER));
    }

    @Test
    void whenChangingUserRole_andTheUserRoleIsNotAdmin_thenUserRoleIsChanged_andSaveInvoke() {
        User user = User.builder()
                .id(UUID.randomUUID())
                .role(UserRole.USER)
                .build();
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        userService.changeRole(user.getId(), UserRole.ADMIN);
        assert (user.getRole() == UserRole.ADMIN);
        verify(userRepository).save(user);
    }

    @Test
    void whenLoadUserByUsername_andUserDontExist_thenThrowException() {
        when(userRepository.findByUsername("username")).thenReturn(Optional.empty());
        assertThrows(NothingFoundException.class, () -> userService.loadUserByUsername("username"));
    }  @Test
    void whenLoadUserByUsername_andUserExist_thenReturnUserData() {
        User user = User.builder().username("username").build();
        when(userRepository.findByUsername("username")).thenReturn(Optional.of(user));
        userService.loadUserByUsername("username");
        UserDetails userDetails = userService.loadUserByUsername("username");
        assert (userDetails instanceof UserData);
        assert (userDetails.getUsername().equals("username"));
    }

}
