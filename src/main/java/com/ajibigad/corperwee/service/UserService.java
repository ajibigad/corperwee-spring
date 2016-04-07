package com.ajibigad.corperwee.service;

import com.ajibigad.corperwee.model.Review;
import com.ajibigad.corperwee.model.User;
import com.ajibigad.corperwee.model.apiModels.PasswordChange;
import com.ajibigad.corperwee.model.apiModels.PasswordReset;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Julius on 07/04/2016.
 */
@Service
public interface UserService {

    public static final Logger LOG = Logger.getLogger(UserService.class);

    public User addUser(User user);

    public Iterable<User> getAllUsers();

    public User updateUser(User user);

    public User getUserByUsername(String username);

    public String uploadProfilePicture(String imageBase64URI, String type);

    //public HttpEntity<byte[]> getProfilePicture (String username

    public User changePassword(String username, PasswordChange passwordChange);

    public List<Review>getReviews(String username);

    public Boolean resetPassword(String username);

    public Boolean changePassword(PasswordReset passwordReset);
}
