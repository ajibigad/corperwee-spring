package com.ajibigad.corperwee.controller;

import com.ajibigad.corperwee.exceptions.Error;
import com.ajibigad.corperwee.exceptions.ResourceNotFoundException;
import com.ajibigad.corperwee.exceptions.UnAuthorizedException;
import com.ajibigad.corperwee.exceptions.UserExistAlready;
import com.ajibigad.corperwee.model.Review;
import com.ajibigad.corperwee.model.User;
import com.ajibigad.corperwee.model.apiModels.PasswordChange;
import com.ajibigad.corperwee.repository.ReviewRepository;
import com.ajibigad.corperwee.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Julius on 23/02/2016.
 */
@RestController
@RequestMapping("corperwee/api/user")
public class UserController {

    @Autowired
    UserRepository repository;

    @Autowired
    ReviewRepository reviewRepository;

    StandardPasswordEncoder passwordEncoder = new StandardPasswordEncoder("corperwee");

    @RequestMapping("/all")
    public List<User> getAllUsers(){
        List<User> users = new ArrayList<User>();
        for (User user : repository.findAll()){
            users.add(user);
        }
        return users;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public User addUser(@RequestBody User newUser){
        if(repository.findByUsername(newUser.getUsername())!=null){
            throw new UserExistAlready(newUser.getUsername());
        }
        else{
            newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
            User user = repository.save(newUser);
            return user;
        }
    }

    @RequestMapping(method = RequestMethod.PUT)
    public User updateUser(@RequestBody User user, Principal principal){
        if(principal.getName().equals(user.getUsername())){
            user.setPassword(repository.findByUsername(principal.getName()).getPassword());
            return repository.save(user);
        }
        else{
            throw new UnAuthorizedException();
        }
    }

    @RequestMapping(value = "/{username}", method = RequestMethod.GET)
    public User getUserByUsername(@PathVariable String username, HttpServletRequest request){
        User user = repository.findByUsername(username);
        if(user == null){
            throw new ResourceNotFoundException("User with username : [" + username + "] not found");
        }
        return user;
    }

    @RequestMapping(value = "/{username}/changePassword", method = RequestMethod.PUT)
    public User changePassword(@PathVariable String username, @RequestBody PasswordChange passwordChange, Principal principal) {
        if (username.equals(principal.getName())) {
            User user = repository.findByUsername(username);
            String encryptedPassword = passwordEncoder.encode(passwordChange.getOldPassword());
            if (user != null) {
                System.out.println(encryptedPassword);
                System.out.println(user.getPassword());
                if (user.getPassword().equals(encryptedPassword)) {
                    user.setPassword(passwordEncoder.encode(passwordChange.getNewPassword()));
                    repository.save(user);
                    return user;
                } else {
                    throw new UnAuthorizedException();
                }
            } else {
                throw new ResourceNotFoundException("User with username : [" + username + "] not found");
            }
        } else {
            throw new UnAuthorizedException();
        }
    }

    @RequestMapping("/reviews/{username}")
    public List<Review> getReviews(String username) {
        User user = repository.findByUsername(username);
        if (user != null) {
            return reviewRepository.findByUser(user);
        } else {
            throw new ResourceNotFoundException("User with id : " + username + "not found");
        }
    }

    @ExceptionHandler(UserExistAlready.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Error userExistAlready(UserExistAlready e) {
        return new Error(0, "User with username : [" + e.getUsername() + "] already exist");
    }
}
