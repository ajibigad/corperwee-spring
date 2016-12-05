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
import com.ajibigad.corperwee.service.ReviewService;
import com.ajibigad.corperwee.utils.SomeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Julius on 04/03/2016.
 */
@RestController
@RequestMapping("corperwee/api/review")
public class ReviewController {

    @Autowired
    ReviewService reviewService;

    @RequestMapping(method = RequestMethod.POST)
    public Review addReview(@RequestBody Review review) {
        return reviewService.addReview(review);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public Review updateReview(@RequestBody Review newReview) {
        return reviewService.updateReview(newReview);
    }

    @RequestMapping(value = "/user/place")
    public Review getReviewByUserAndPlace(@RequestParam String username, @RequestParam long placeId) {
        return reviewService.getReviewByUserAndPlace(username, placeId);
    }

    @ExceptionHandler(ReviewExistAlready.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Error reviewExistAlready(ReviewExistAlready e) {
        return new Error(0, e.getMessage());
    }
}
