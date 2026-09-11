package com.sridharnagula.userservice.services;

import com.sridharnagula.userservice.exceptions.DuplicateUserException;
import com.sridharnagula.userservice.exceptions.UserNotFoundException;
import com.sridharnagula.userservice.models.User;

import java.util.List;

public interface UserService {

    User createUser(String name, String email, String phoneNumber, String password) throws DuplicateUserException;

    User getUserById(Long userId) throws UserNotFoundException;

    User getUserByEmail(String email) throws UserNotFoundException;

    List<User> getAllUsers();

    User updateUser(Long userId, String name, String phoneNumber) throws UserNotFoundException;

    String deleteUser(Long userId) throws UserNotFoundException;
}
