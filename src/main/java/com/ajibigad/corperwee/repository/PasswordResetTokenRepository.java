package com.ajibigad.corperwee.repository;

import com.ajibigad.corperwee.model.PasswordResetToken;
import com.ajibigad.corperwee.model.User;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Julius on 21/03/2016.
 */
public interface PasswordResetTokenRepository extends CrudRepository<PasswordResetToken, Long> {

    public PasswordResetToken findByUserAndToken(User user, String token);
}
