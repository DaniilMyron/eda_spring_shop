package com.miron.user.services.impl;

import com.miron.core.converter.ObjectToMapConverter;
import com.miron.core.message.ChangeBalanceStatusEnum;
import com.miron.core.message.CheckBalanceStatusEnum;
import com.miron.core.models.ProductsInCartToReturn;
import com.miron.core.models.UserInfoForCheck;
import com.miron.user.controllers.api.RegistrationRequest;
import com.miron.user.domain.User;
import com.miron.user.exceptions.UserRegisteredException;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import com.miron.user.repositories.UserRepository;
import com.miron.user.services.IUserService;
import com.miron.user.publishers.IUserEventPublisher;
import com.miron.user.publishers.impl.UserEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private final IUserEventPublisher publisher;

    @Override
    public void registerUser(RegistrationRequest request) {
        var foundedUser = userRepository.findByUsername(request.username());
        if(foundedUser != null) {
            throw new UserRegisteredException("This username taken by another user: " + foundedUser, foundedUser);
        }
        var user = userRepository.save(User.builder()
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .balance(0)
                .sumOnBuying(0)
                .build());
        publisher.publishUserRegistrationEvent(user);
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
    public void checkBalanceAndReserveOnBuying(String username, int requiredSum, int productRequestId) {
        var user = userRepository.findByUsername(username);
        var status = user.getBalance() >= requiredSum ? CheckBalanceStatusEnum.CONFIRMED : CheckBalanceStatusEnum.CANCELLED;
        if(status == CheckBalanceStatusEnum.CONFIRMED) {
            user.setSumOnBuying(requiredSum);
            userRepository.save(user);
        }
        publisher.publishCheckBalanceEvent(username, requiredSum, productRequestId, status);
    }

    @Override
    public void changeBalanceAndMakeCheck(String username, ChangeBalanceStatusEnum payloadStatus, JSONObject productsCountOnId) {
        var user = userRepository.findByUsername(username);
        if(payloadStatus == ChangeBalanceStatusEnum.CONFIRMED) {
            user.setBalance(user.getBalance() - user.getSumOnBuying());
            publisher.publishUserInfoForCheck(new UserInfoForCheck(user.getSumOnBuying(), username));
        } else if(payloadStatus == ChangeBalanceStatusEnum.REJECTED){
            var map = ObjectToMapConverter.convertJSONObjectToMap(productsCountOnId);
            publisher.publishGetBackProductsInCart(new ProductsInCartToReturn(map));
        }
        user.setSumOnBuying(0);
    }
}
