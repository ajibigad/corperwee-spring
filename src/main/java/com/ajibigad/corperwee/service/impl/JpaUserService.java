package com.ajibigad.corperwee.service.impl;

import com.ajibigad.corperwee.exceptions.*;
import com.ajibigad.corperwee.model.PasswordResetToken;
import com.ajibigad.corperwee.model.Review;
import com.ajibigad.corperwee.model.User;
import com.ajibigad.corperwee.model.apiModels.PasswordChange;
import com.ajibigad.corperwee.model.apiModels.PasswordReset;
import com.ajibigad.corperwee.repository.ReviewRepository;
import com.ajibigad.corperwee.repository.UserRepository;
import com.ajibigad.corperwee.service.PasswordResetTokenService;
import com.ajibigad.corperwee.service.ProfilePictureService;
import com.ajibigad.corperwee.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;
import java.util.UUID;

/**
 * Created by Julius on 07/04/2016.
 */
@Service
public class JpaUserService implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    PasswordResetTokenService passwordResetTokenService;

    @Autowired
    Environment env;

    @Autowired
    JavaMailSender corperWeeMailService;

    @Autowired
    ProfilePictureService profilePictureService;

    @Override
    public User addUser(User newUser) {
        if(userRepository.findByUsername(newUser.getUsername())!=null){
            throw new UserExistAlready(newUser.getUsername());
        }
        else{
            newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
            User user = userRepository.save(newUser);
            return user;
        }
    }

    @Override
    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User updateUser(User user) {
        user.setPassword(userRepository.findByUsername(user.getUsername()).getPassword());
        return userRepository.save(user);
    }

    @Override
    public User getUserByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if(user == null){
            throw resourceNotFoundFactory(username);
        }
        return user;
    }

    @Override
    public User findByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if(user == null){
            throw new ResourceNotFoundException("Username " + username + " not found!");
        }
        return user;
    }

    @Override
    public User changePassword(String username, PasswordChange passwordChange) {
        User user = userRepository.findByUsername(username);
        String existingPassword = passwordChange.getOldPassword(); // Password entered by user
        String dbPassword = user.getPassword(); // Load hashed DB password
        if (user != null) {
            if (passwordEncoder.matches(existingPassword, dbPassword)) {
                // Encode new password and store it
                user.setPassword(passwordEncoder.encode(passwordChange.getNewPassword()));
                userRepository.save(user);
                return user;
            } else {
                LOG.error(username + " is not authorized because wrong old password was sent");
                UnAuthorizedException unAuthorizedException = new UnAuthorizedException();
                unAuthorizedException.setExtraMessage(username + " is not authorized because wrong old password was sent");
                throw unAuthorizedException;
            }
        } else {
            LOG.error(username + " not found");
            throw resourceNotFoundFactory(username);
        }
    }

    @Override
    public List<Review> getReviews(String username) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            return reviewRepository.findByUser(user);
        } else {
            throw resourceNotFoundFactory(username);
        }
    }

    @Override
    public Boolean resetPassword(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw resourceNotFoundFactory(username);
        }
        String token = UUID.randomUUID().toString();
        //i should if the token for this user exist b4 creating a new one
        passwordResetTokenService.createPasswordResetTokenForUser(user, token);
        String appUrl = "http://localhost/nysc/app/";
        SimpleMailMessage email = constructResetTokenEmail(appUrl, token, user);
        corperWeeMailService.send(email);
        return true;
    }

    @Override
    public Boolean changePassword(PasswordReset passwordReset) {
        User tokenUser = userRepository.findOne(passwordReset.getUserId());
        if (tokenUser == null) {
            throw resourceNotFoundFactory(passwordReset.getUserId()+"");
        }
        PasswordResetToken passToken = passwordResetTokenService.findByUserAndToken(tokenUser, passwordReset.getToken());
        if (passToken == null) {
            throw new ResourceNotFoundException("This token cant be found");
        }
        User user = passToken.getUser();
        if (passToken == null || user.getId() != passwordReset.getUserId()) {
            throw new InvalidResetPasswordToken();
        }

        Calendar cal = Calendar.getInstance();
        if ((passToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            passwordResetTokenService.delete(passToken.getId());
            throw new ExpiredResetPasswordToken();
        }

        user.setPassword(passwordEncoder.encode(passwordReset.getPassword()));
        userRepository.save(user);
        passwordResetTokenService.delete(passToken.getId());
        return true;
    }

    private SimpleMailMessage constructResetTokenEmail(
            String contextPath, String token, User user) {
        String url = contextPath + "#/welcome/changePassword?id=" + user.getId() + "&token=" + token; //this should call the angular app
        String message = "Use this link to reset your password: %s . \nPls it expires after 24hrs";
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(user.getEmail());
        email.setSubject("Reset Password");
        email.setText(String.format(message, url));
        email.setFrom(env.getProperty("support.email"));
        return email;
    }

    private ResourceNotFoundException resourceNotFoundFactory(String identity) {
        return new ResourceNotFoundException("User with username/id : " + identity + "not found");
    }
}
