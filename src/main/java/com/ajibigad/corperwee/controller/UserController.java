package com.ajibigad.corperwee.controller;

import com.ajibigad.corperwee.exceptions.Error;
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
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Decoder;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

/**
 * Created by Julius on 23/02/2016.
 */
@RestController
@RequestMapping("corperwee/api/user")
public class UserController {

    private static final Logger LOG = Logger.getLogger(UserController.class);

    @Autowired
    UserRepository repository;

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

    //StandardPasswordEncoder passwordEncoder = new StandardPasswordEncoder("corperwee");

    @RequestMapping("/all")
    public Iterable<User> getAllUsers() {
        return repository.findAll();
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
            throw resourceNotFoundFactory(username);
        }
        return user;
    }

    @RequestMapping(value = "/uploadProfilePicture", method = RequestMethod.POST, produces = "application/json")
    public String uploadProfilePicture(@RequestBody String imageBase64URI, @RequestParam String type, Principal principal){
        return profilePictureService.handleImageUpload(imageBase64URI, type, principal.getName());
    }

    @RequestMapping("/profilePicture/{username}")
    public HttpEntity<byte[]> getProfilePicture (@PathVariable String username, Principal principal) throws IOException {
        // send it back to the client
        // i think anybody should be able to see your profile picture so this api would be opened to all
//        if(!username.equals(principal.getName())){
//            throw new UnAuthorizedException();
//        }
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.IMAGE_JPEG);
        return new ResponseEntity<byte[]>(profilePictureService.getImage(username), httpHeaders, HttpStatus.OK);
    }

    @RequestMapping(value = "/{username}/changePassword", method = RequestMethod.PUT)
    public User changePassword(@PathVariable String username, @RequestBody PasswordChange passwordChange, Principal principal) {
        if (username.equals(principal.getName())) {
            User user = repository.findByUsername(username);
            //BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String existingPassword = passwordChange.getOldPassword(); // Password entered by user
            String dbPassword = user.getPassword(); // Load hashed DB password
            if (user != null) {
                if (passwordEncoder.matches(existingPassword, dbPassword)) {
                    // Encode new password and store it
                    user.setPassword(passwordEncoder.encode(passwordChange.getNewPassword()));
                    repository.save(user);
                    return user;
                } else {
                    // Report error
                    LOG.error(username + " is not authorized because wrong old password was sent");
                    throw new UnAuthorizedException();
                }
            } else {
                LOG.error(username + " not found");
                throw resourceNotFoundFactory(username);
            }
        } else {
            LOG.error(username + " is not equal to " + principal.getName() + " therefore not authorized");
            throw new UnAuthorizedException();
        }
    }

    @RequestMapping("/reviews/{username}")
    public List<Review> getReviews(String username) {
        User user = repository.findByUsername(username);
        if (user != null) {
            return reviewRepository.findByUser(user);
        } else {
            throw resourceNotFoundFactory(username);
        }
    }

    @RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
    @ResponseBody
    public Boolean resetPassword(
            HttpServletRequest request, @RequestParam("username") String username) {

        User user = repository.findByUsername(username);
        if (user == null) {
            throw resourceNotFoundFactory(username);
        }

        String token = UUID.randomUUID().toString();
        //i should if the token for this user exist b4 creating a new one
        //get that token and use it as the token to be sent to the user's mail
        passwordResetTokenService.createPasswordResetTokenForUser(user, token); //passwordResetTokenService
        String appUrl = "http://localhost/nysc/app/";
//                "http://" + request.getServerName() +
//                        ":" + request.getServerPort() +
//                        request.getContextPath();
        SimpleMailMessage email = constructResetTokenEmail(appUrl, token, user);
        corperWeeMailService.send(email);

        return true;
    }

    @RequestMapping(value = "/changePassword", method = RequestMethod.POST) //should be only opened to anonymous users
    public Boolean changePassword(@RequestBody PasswordReset passwordReset) {
        User tokenUser = repository.findOne(passwordReset.getUserId());
        if (tokenUser == null) {
            throw resourceNotFoundFactory(passwordReset.getUserId());
        }
        PasswordResetToken passToken = passwordResetTokenService.findByUserAndToken(tokenUser, passwordReset.getToken());
        if (passToken == null) {
            throw new ResourceNotFoundException("This token cant be found");
        }
        User user = passToken.getUser();
        if (passToken == null || user.getId() != passwordReset.getUserId()) {
            //throw invalid reset token error
            throw new InvalidResetPasswordToken();
        }

        Calendar cal = Calendar.getInstance();
        if ((passToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            passwordResetTokenService.delete(passToken.getId());
            throw new ExpiredResetPasswordToken();
        }

        user.setPassword(passwordEncoder.encode(passwordReset.getPassword()));
        repository.save(user);
        passwordResetTokenService.delete(passToken.getId());

//        Authentication auth = new UsernamePasswordAuthenticationToken(
//                user, null, userDetailsService.loadUserByUsername(user.getEmail()).getAuthorities());
//        SecurityContextHolder.getContext().setAuthentication(auth);

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

    private boolean checkIfUsernameIsPrincipal(String username, Principal principal) {
        return principal.equals(username);
    }

    private ResourceNotFoundException resourceNotFoundFactory(String username) {
        return new ResourceNotFoundException("User with username : " + username + "not found");
    }

    private ResourceNotFoundException resourceNotFoundFactory(long userId) {
        return new ResourceNotFoundException("User with user_id : " + userId + " not found");
    }
}
