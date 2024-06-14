package com.miron.fraud;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class FraudCheckService {

    @Autowired
    private FraudCheckHistoryRepository fraudCheckHistoryRepository;

    public boolean isFraudulentCustomerAndSave(int customerId){
        fraudCheckHistoryRepository.save(FraudCheckHistory.builder()
                .customerId(customerId)
                .isFraudster(false)
                .createdAt(LocalDateTime.now())
                .build());
        return false;
    }
}
