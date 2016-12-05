package com.ajibigad.corperwee.service;

import com.ajibigad.corperwee.annotations.Notify;
import com.ajibigad.corperwee.model.Place;
import com.ajibigad.corperwee.model.Review;
import com.ajibigad.corperwee.model.User;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Julius on 07/04/2016.
 */
public interface ReviewService {

    public static final Logger LOG = Logger.getLogger(ReviewService.class);

    public List<Review> findByPlace(Place place);

    public List<Review> findByUser(User user);

    public Review findByUserAndPlace(User user, Place place);

    public Review addReview(Review review);

    public Review updateReview(Review newReview);

    public Review getReviewByUserAndPlace(String username, long placeId);
}
