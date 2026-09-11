package com.sridharnagula.userservice.controllers;

import com.sridharnagula.userservice.dtos.CreateUserRequestDTO;
import com.sridharnagula.userservice.dtos.UpdateUserRequestDTO;
import com.sridharnagula.userservice.dtos.UserResponseDTO;
import com.sridharnagula.userservice.exceptions.DuplicateUserException;
import com.sridharnagula.userservice.exceptions.UserNotFoundException;
import com.sridharnagula.userservice.models.User;
import com.sridharnagula.userservice.services.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public UserResponseDTO createUser(@Valid @RequestBody CreateUserRequestDTO request) throws DuplicateUserException {
        User user = userService.createUser(request.getName(), request.getEmail(), request.getPhoneNumber(), request.getPassword());
        return UserResponseDTO.from(user);
    }

    @GetMapping
    public List<UserResponseDTO> getAllUsers() {
        return userService.getAllUsers().stream().map(UserResponseDTO::from).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public UserResponseDTO getUserById(@PathVariable("id") Long userId) throws UserNotFoundException {
        return UserResponseDTO.from(userService.getUserById(userId));
    }

    // Called by orderservice while orchestrating "create order" to confirm the
    // buyer exists before a payment is attempted - one of the cross-service hops
    // that shows up as a downstream span in the order-service trace.
    @GetMapping("/email/{email}")
    public UserResponseDTO getUserByEmail(@PathVariable("email") String email) throws UserNotFoundException {
        return UserResponseDTO.from(userService.getUserByEmail(email));
    }

    @PutMapping("/{id}")
    public UserResponseDTO updateUser(@PathVariable("id") Long userId, @RequestBody UpdateUserRequestDTO request) throws UserNotFoundException {
        return UserResponseDTO.from(userService.updateUser(userId, request.getName(), request.getPhoneNumber()));
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable("id") Long userId) throws UserNotFoundException {
        return userService.deleteUser(userId);
    }
}
