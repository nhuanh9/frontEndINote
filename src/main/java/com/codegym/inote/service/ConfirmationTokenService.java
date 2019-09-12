package com.codegym.inote.service;

import com.codegym.inote.model.ConfirmationToken;

public interface ConfirmationTokenService {
    ConfirmationToken findByConfirmationToken(String confirmationToken);

    void save(ConfirmationToken confirmationToken);
}
