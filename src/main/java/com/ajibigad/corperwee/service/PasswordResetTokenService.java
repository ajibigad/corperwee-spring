package com.ajibigad.corperwee.service;

import com.ajibigad.corperwee.model.PasswordResetToken;
import com.ajibigad.corperwee.model.User;
import com.ajibigad.corperwee.repository.PasswordResetTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Julius on 21/03/2016.
 */
@Service
@Transactional
public class PasswordResetTokenService {

    @Autowired
    PasswordResetTokenRepository repository;

    public PasswordResetToken createPasswordResetTokenForUser(User user, String token) {
        PasswordResetToken passwordResetToken = new PasswordResetToken();
        passwordResetToken.setUser(user);
        passwordResetToken.setToken(token);
        passwordResetToken.calculateExpiryDate();//this would set the expiry date for the token
        return repository.save(passwordResetToken);
    }

    public PasswordResetToken findByUserAndToken(User user, String token) {
        return repository.findByUserAndToken(user, token);
    }

    public void delete(long id) {
        repository.delete(id);
    }
}
