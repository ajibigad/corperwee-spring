package com.ajibigad.corperwee.controller;

import com.ajibigad.corperwee.exceptions.Error;
import com.ajibigad.corperwee.exceptions.ResourceNotFoundException;
import com.ajibigad.corperwee.exceptions.ReviewExistAlready;
import com.ajibigad.corperwee.model.Place;
import com.ajibigad.corperwee.model.Review;
import com.ajibigad.corperwee.model.User;
import com.ajibigad.corperwee.repository.PlaceRepository;
import com.ajibigad.corperwee.repository.ReviewRepository;
import com.ajibigad.corperwee.repository.UserRepository;
import com.ajibigad.corperwee.utils.SomeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Julius on 04/03/2016.
 */
@RestController
@RequestMapping("corperwee/api/review")
public class ReviewController {

    @Autowired
    ReviewRepository repository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PlaceRepository placeRepository;

    @RequestMapping(method = RequestMethod.POST)
    public Review addReview(@RequestBody Review review, Principal principal) {
        User user = userRepository.findByUsername(principal.getName());
        if (repository.findByUserAndPlace(user, review.getPlace()) != null) {
            review.setUser(user);
            return repository.save(review);
        } else {
            throw new ReviewExistAlready(user.getUsername(), review.getPlace().getName());
        }
    }

    @RequestMapping(method = RequestMethod.PUT)
    public Review updateReview(@RequestBody Review newReview, Principal principal) {
        User user = userRepository.findByUsername(principal.getName());
        Review review = repository.findOne(newReview.getId());
        if (review != null) {
            review.setRating(newReview.getRating());
            review.setReviewMessage(newReview.getReviewMessage());
            return repository.save(review);
        } else {
            throw new ResourceNotFoundException("Review with id : " + review.getId() + " not found");
        }
    }

    @RequestMapping(value = "/user/place")
    public Review getReviewByUserAndPlace(@RequestParam String username, @RequestParam long placeId) {
        Place place = placeRepository.findOne(placeId);
        User user = userRepository.findByUsername(username);
        List<Object> assertNotNull = new ArrayList<>();
        assertNotNull.add(place);
        assertNotNull.add(user);
        if (SomeUtils.isAllNotNull(assertNotNull)) {
            return repository.findByUserAndPlace(user, place);
        } else {
            throw new IllegalArgumentException("Username and Place must exist");
        }
    }

    @ExceptionHandler(ReviewExistAlready.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Error reviewExistAlready(ReviewExistAlready e) {
        return new Error(0, e.getMessage());
    }
}
