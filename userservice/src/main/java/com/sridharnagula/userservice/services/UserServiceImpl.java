package com.sridharnagula.userservice.services;

import com.sridharnagula.userservice.exceptions.DuplicateUserException;
import com.sridharnagula.userservice.exceptions.UserNotFoundException;
import com.sridharnagula.userservice.models.User;
import com.sridharnagula.userservice.repositories.UserRepository;
import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.StatusCode;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final Tracer tracer;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserServiceImpl(UserRepository userRepository, Tracer tracer) {
        this.userRepository = userRepository;
        this.tracer = tracer;
    }

    @Override
    public User createUser(String name, String email, String phoneNumber, String password) throws DuplicateUserException {
        // Manual child span: makes the "does this email already exist" check its own
        // node in the trace, so a slow uniqueness check is visible separately from the
        // DB insert when looking at request latency in SigNoz.
        Span span = tracer.spanBuilder("user-service.create-user")
                .setAttribute("user.email", email)
                .startSpan();
        try (Scope scope = span.makeCurrent()) {
            User existing = userRepository.findByEmail(email);
            if (existing != null) {
                span.setStatus(StatusCode.ERROR, "duplicate email");
                throw new DuplicateUserException("A user with email " + email + " already exists");
            }

            User user = new User();
            user.setName(name);
            user.setEmail(email);
            user.setPhoneNumber(phoneNumber);
            user.setPasswordHash(passwordEncoder.encode(password));

            User saved = userRepository.save(user);
            span.setAttribute("user.id", saved.getId());
            logger.info("Created user id={} email={}", saved.getId(), saved.getEmail());
            return saved;
        } catch (DuplicateUserException e) {
            span.recordException(e);
            throw e;
        } finally {
            span.end();
        }
    }

    @Override
    public User getUserById(Long userId) throws UserNotFoundException {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new UserNotFoundException("User with id " + userId + " was not found");
        }
        return user;
    }

    @Override
    public User getUserByEmail(String email) throws UserNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UserNotFoundException("User with email " + email + " was not found");
        }
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User updateUser(Long userId, String name, String phoneNumber) throws UserNotFoundException {
        User user = getUserById(userId);
        if (name != null) {
            user.setName(name);
        }
        if (phoneNumber != null) {
            user.setPhoneNumber(phoneNumber);
        }
        return userRepository.save(user);
    }

    @Override
    public String deleteUser(Long userId) throws UserNotFoundException {
        User user = getUserById(userId);
        userRepository.deleteById(user.getId());
        return "User successfully deleted";
    }
}
