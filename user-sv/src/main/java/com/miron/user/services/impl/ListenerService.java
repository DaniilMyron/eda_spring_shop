package com.miron.user.services.impl;

import com.miron.core.converter.ObjectToMapConverter;
import com.miron.core.message.ChangeBalanceStatusEnum;
import com.miron.core.message.CheckBalanceStatusEnum;
import com.miron.core.models.ProductsInCartToReturn;
import com.miron.core.models.UserInfoForCheck;
import com.miron.user.exceptions.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.miron.user.repositories.UserRepository;
import com.miron.user.services.IListenerService;
import com.miron.user.publishers.IUserEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ListenerService implements IListenerService {
    private final UserRepository userRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(ListenerService.class);
    private final IUserEventPublisher publisher;

    @Override
    public void checkBalanceAndReserveOnBuying(String username, int requiredSum, int productRequestId) {
        var user = userRepository.findByUsername(username)
                .orElseThrow(UserNotFoundException::new);
        var status = user.getBalance() >= requiredSum ? CheckBalanceStatusEnum.CONFIRMED : CheckBalanceStatusEnum.CANCELLED;
        if(status == CheckBalanceStatusEnum.CONFIRMED) {
            user.setSumOnBuying(requiredSum);
            userRepository.save(user);
        }
        publisher.publishCheckBalanceEvent(username, requiredSum, productRequestId, status);
    }

    @Override
    public void changeBalanceAndMakeCheck(String username, ChangeBalanceStatusEnum payloadStatus, JSONObject productsCountOnId) {
        var user = userRepository.findByUsername(username)
                .orElseThrow(UserNotFoundException::new);
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
