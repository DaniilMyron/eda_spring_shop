package com.miron.user.services;

import com.miron.core.message.ChangeBalanceStatusEnum;
import com.miron.user.controllers.api.RegistrationRequest;
import com.miron.user.controllers.api.UserResponse;
import org.json.JSONObject;
import org.springframework.security.core.Authentication;

public interface IListenerService {

    void checkBalanceAndReserveOnBuying(String username, int requiredSum, int productRequiredId);

    void changeBalanceAndMakeCheck(String username, ChangeBalanceStatusEnum payloadStatus, JSONObject productsCountOnId);
}
