package com.compute.rental.modules.auth.service;

public interface EmailService {

    void sendLoginCode(String email, String code);
}
