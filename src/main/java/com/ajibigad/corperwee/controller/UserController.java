package com.ajibigad.corperwee.controller;

import com.ajibigad.corperwee.exceptions.Error;
import com.ajibigad.corperwee.exceptions.ExpiredResetPasswordToken;
import com.ajibigad.corperwee.exceptions.InvalidResetPasswordToken;
import com.ajibigad.corperwee.exceptions.UserExistAlready;
import com.ajibigad.corperwee.model.Review;
import com.ajibigad.corperwee.model.User;
import com.ajibigad.corperwee.model.apiModels.PasswordChange;
import com.ajibigad.corperwee.model.apiModels.PasswordReset;
import com.ajibigad.corperwee.service.ProfilePictureService;
import com.ajibigad.corperwee.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

/**
 * Created by Julius on 23/02/2016.
 */
@RestController
@RequestMapping("corperwee/api/user")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    ProfilePictureService profilePictureService;

    @RequestMapping("/all")
    public Iterable<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public User addUser(@RequestBody User newUser){
        return userService.addUser(newUser);
    }

    @RequestMapping(method = RequestMethod.PUT)
    @PreAuthorize("#user.username == principal.username")
    public User updateUser(@RequestBody User user){
        return userService.updateUser(user);
    }

    @RequestMapping(value = "/{username}", method = RequestMethod.GET)
    public User getUserByUsername(@PathVariable String username){
        return userService.getUserByUsername(username);
    }

    @RequestMapping(value = "/uploadProfilePicture", method = RequestMethod.POST, produces = "text/plain")
    public String uploadProfilePicture(@RequestBody String imageBase64URI, @RequestParam String type, Principal principal){
        return profilePictureService.handleImageUpload(imageBase64URI, type, principal.getName());
    }

    @RequestMapping("/profilePicture/{username}")
    public HttpEntity<byte[]> getProfilePicture (@PathVariable String username) throws IOException {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.IMAGE_JPEG);
        return new ResponseEntity<byte[]>(profilePictureService.getImage(username), httpHeaders, HttpStatus.OK);
    }

    @RequestMapping(value = "/{username}/changePassword", method = RequestMethod.PUT)
    @PreAuthorize("#username == principal.username")
    public User changePassword(@PathVariable String username, @RequestBody PasswordChange passwordChange) {
        return userService.changePassword(username, passwordChange);
    }

    @RequestMapping("/reviews/{username}")
    public List<Review> getReviews(@PathVariable String username) {
        return userService.getReviews(username);
    }

    @RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
    @ResponseBody
    public Boolean resetPassword(@RequestParam("username") String username) {
        return userService.resetPassword(username);
    }

    @RequestMapping(value = "/changePassword", method = RequestMethod.POST) //should be only opened to anonymous users
    public Boolean changePassword(@RequestBody PasswordReset passwordReset) {
        return userService.changePassword(passwordReset);
    }

    @ExceptionHandler(UserExistAlready.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Error userExistAlready(UserExistAlready e) {
        return new Error(0, "User with username : [" + e.getUsername() + "] already exist");
    }

    @ExceptionHandler(InvalidResetPasswordToken.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Error invalidPasswordResetToken() {
        return new Error(0, "Invalid Password Reset Token");
    }

    @ExceptionHandler(ExpiredResetPasswordToken.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Error passwordResetTokenExpired() {
        return new Error(0, "Password Reset Token has expired");
    }
}
