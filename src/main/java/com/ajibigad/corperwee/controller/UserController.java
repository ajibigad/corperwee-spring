package com.ajibigad.corperwee.controller;

import com.ajibigad.corperwee.exceptions.UnAuthorizedException;
import com.ajibigad.corperwee.exceptions.UserExistAlready;
import com.ajibigad.corperwee.exceptions.UserNotFoundException;
import com.ajibigad.corperwee.model.Place;
import com.ajibigad.corperwee.model.User;
import com.ajibigad.corperwee.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.ajibigad.corperwee.exceptions.Error;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;
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
            User user = repository.save(newUser);
            newUser.setPassword("");
            return user;
        }
    }

    @RequestMapping(method = RequestMethod.PUT)
    public User updateUser(@RequestBody User user, Principal principal){
        if(principal.getName().equals(user.getUsername())){
            return repository.save(user);
        }
        else{
            throw new UnAuthorizedException(user.getUsername());
        }
    }

    @RequestMapping(value = "/{username}", method = RequestMethod.GET)
    public User getUserByUsername(@PathVariable String username, HttpServletRequest request){
        User user = repository.findByUsername(username);
        if(user == null){
            throw new UserNotFoundException(0, username);
        }
        return user;
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Error userNotFound(UserNotFoundException e) {
        if(e.getUserId() > 0){
            return new Error(e.getUserId(), "User with id : [" + e.getUserId() + "] not found");
        }
        return new Error(e.getUserId(), "User with username : [" + e.getUsername() + "] not found");
    }

    @ExceptionHandler(UserExistAlready.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Error userExistAlready(UserExistAlready e) {
        return new Error(0, "User with username : [" + e.getUsername() + "] already exist");
    }

}
