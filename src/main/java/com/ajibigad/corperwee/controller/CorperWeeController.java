package com.ajibigad.corperwee.controller;

import com.ajibigad.corperwee.model.User;
import com.ajibigad.corperwee.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Julius on 19/02/2016.
 */
@RestController
@RequestMapping("corperwee/api")
public class CorperWeeController {

    @RequestMapping("/hello")
    public String sayHello(@RequestParam String message){
        return "Hello World" + message;
    }

    @RequestMapping("/login")
    public Principal user(Principal user) {
        return user;
    }

    @RequestMapping("/logout")
    public void logout(HttpServletRequest request) {
        System.out.println(request.getHeader("Referer"));
    }
}
