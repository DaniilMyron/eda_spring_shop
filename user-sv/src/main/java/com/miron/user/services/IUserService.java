package com.miron.user.services;

import com.miron.core.message.ChangeBalanceStatusEnum;
import com.miron.user.controllers.api.RegistrationRequest;
import com.miron.user.domain.User;
import org.json.JSONObject;

public interface IUserService {
    void registerUser(RegistrationRequest request);
    void authenticateUser();

    User getAuthenticatedUser(Object authentication, int sum);

    void checkBalanceAndReserveOnBuying(String username, int requiredSum, int productRequiredId);

    void changeBalanceAndMakeCheck(String username, ChangeBalanceStatusEnum payloadStatus, JSONObject productsCountOnId);
}
