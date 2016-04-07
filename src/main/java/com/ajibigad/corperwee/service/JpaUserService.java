package com.ajibigad.corperwee.service;

import com.ajibigad.corperwee.exceptions.ResourceNotFoundException;
import com.ajibigad.corperwee.exceptions.UnAuthorizedException;
import com.ajibigad.corperwee.exceptions.UserExistAlready;
import com.ajibigad.corperwee.model.Review;
import com.ajibigad.corperwee.model.User;
import com.ajibigad.corperwee.model.apiModels.PasswordChange;
import com.ajibigad.corperwee.model.apiModels.PasswordReset;
import com.ajibigad.corperwee.repository.ReviewRepository;
import com.ajibigad.corperwee.repository.UserRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

/**
 * Created by Julius on 07/04/2016.
 */
public class JpaUserService implements UserService{

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

    @Autowired
    Principal principal;

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
        if(principal.getName().equals(user.getUsername())){
            user.setPassword(userRepository.findByUsername(principal.getName()).getPassword());
            return userRepository.save(user);
        }
        else{
            throw new UnAuthorizedException();
        }
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
    public String uploadProfilePicture(String imageBase64URI, String type) {
        return null;
    }

    @Override
    public User changePassword(String username, PasswordChange passwordChange) {
        return null;
    }

    @Override
    public List<Review> getReviews(String username) {
        return null;
    }

    @Override
    public Boolean resetPassword(String username) {
        return null;
    }

    @Override
    public Boolean changePassword(PasswordReset passwordReset) {
        return null;
    }

    private ResourceNotFoundException resourceNotFoundFactory(String username) {
        return new ResourceNotFoundException("User with username : " + username + "not found");
    }
}
