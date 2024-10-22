package com.miron.user.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.miron.user.controllers.api.RegistrationRequest;
import com.miron.user.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import com.miron.user.repositories.UserRepository;
import com.miron.user.services.IUserService;
import com.miron.user.services.models.IUserJsonPublisher;
import com.miron.user.services.models.impl.UserJsonPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService, InitializingBean {
    @Value("${user-sv.topic.produces.userRegisteredEvent}")
    private String topicName;
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private final ObjectMapper objectMapper = new ObjectMapper();
    private IUserJsonPublisher publisher;

    @Override
    public void setPublisher(IUserJsonPublisher publisher) {
        this.publisher = publisher;
    }

    @Override
    public void registerUser(RegistrationRequest request) {
        var user = userRepository.save(User.builder()
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .balance(0)
                .build());
        publisher.publish(user);
    }

    @Override
    public void authenticateUser() {

    }

    @Override
    public User getAuthenticatedUser(Object authentication, int sum) {
        LOGGER.info(authentication.toString());
        int startPoint = authentication.toString().indexOf("Username=") + 9;
        int endPoint = authentication.toString().indexOf(",");
        var authenticatedUserUsername = authentication.toString().substring(startPoint, endPoint);
        var authenticatedUser = userRepository.findByUsername(authenticatedUserUsername);
        authenticatedUser.setBalance(authenticatedUser.getBalance() + sum);
        userRepository.save(authenticatedUser);
        return authenticatedUser;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        setPublisher(new UserJsonPublisher(kafkaTemplate, topicName));
    }
}
