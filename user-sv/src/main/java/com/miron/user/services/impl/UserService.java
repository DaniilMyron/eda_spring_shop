package com.miron.user.services.impl;

import com.miron.user.controllers.api.RegistrationRequest;
import com.miron.user.controllers.api.UserResponse;
import com.miron.user.converter.UserToResponseConverter;
import com.miron.user.domain.User;
import com.miron.user.exceptions.UserNotFoundException;
import com.miron.user.exceptions.UserRegisteredException;
import com.miron.user.publishers.IUserEventPublisher;
import com.miron.user.repositories.UserRepository;
import com.miron.user.services.IUserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private final IUserEventPublisher publisher;
    private final UserToResponseConverter userToResponseConverter = new UserToResponseConverter();

    @Override
    public UserResponse registerUser(RegistrationRequest request) {
        try{
            var foundedUser = userRepository.findByUsername(request.username());
            if(foundedUser.isPresent()) {
                throw new UserRegisteredException("This username taken by another user: " + foundedUser, foundedUser.get());
            }
        } catch (UserNotFoundException ignored) {}

        var user = userRepository.save(User.builder()
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .balance(0)
                .sumOnBuying(0)
                .build());
        publisher.publishUserRegistrationEvent(user);
        return userToResponseConverter.apply(user);
    }

    @Override
    public void authenticateUser() {

    }

    @Override
    public UserResponse replenishBalance(Authentication authentication, int sum) {
        var authenticatedUser = userRepository.findByUsername(authentication.getName())
                .orElseThrow(UserNotFoundException::new);
        authenticatedUser.setBalance(authenticatedUser.getBalance() + sum);
        userRepository.save(authenticatedUser);
        return userToResponseConverter.apply(authenticatedUser);
    }
}
