package com.miron.carting.controllers.model;

public record CustomerRegistrationRequest(String firstName,
                                          String lastName,
                                          String email) {

}
