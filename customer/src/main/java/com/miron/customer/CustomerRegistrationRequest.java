package com.miron.customer;

public record CustomerRegistrationRequest(String firstName,
                                          String lastName,
                                          String email) {

}
