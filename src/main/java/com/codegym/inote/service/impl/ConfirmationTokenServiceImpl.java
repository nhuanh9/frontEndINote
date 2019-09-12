package com.codegym.inote.service.impl;

import com.codegym.inote.model.ConfirmationToken;
import com.codegym.inote.repository.ConfirmationTokenRepository;
import com.codegym.inote.service.ConfirmationTokenService;
import org.springframework.beans.factory.annotation.Autowired;

public class ConfirmationTokenServiceImpl implements ConfirmationTokenService {

    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;

    @Override
    public ConfirmationToken findByConfirmationToken(String confirmationToken) {
        return confirmationTokenRepository.findByConfirmationToken(confirmationToken);
    }
}
