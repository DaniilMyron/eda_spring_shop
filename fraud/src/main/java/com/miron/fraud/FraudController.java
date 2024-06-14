package com.miron.fraud;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/fraud-check")
@Slf4j
public class FraudController {

    @Autowired
    private FraudCheckService fraudCheckService;

    @GetMapping(path = "{customerId}")
    public FraudCheckResponse isFraudster(
            @PathVariable("customerId") int customerId){
        boolean isFraudulentCustomer = fraudCheckService
                .isFraudulentCustomerAndSave(customerId);
        log.info("fraud check request from customer {}", customerId);
        return new FraudCheckResponse(isFraudulentCustomer);
    }
}
