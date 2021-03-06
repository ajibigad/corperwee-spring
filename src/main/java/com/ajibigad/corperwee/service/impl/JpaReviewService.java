package com.ajibigad.corperwee.service.impl;

import com.ajibigad.corperwee.annotations.Notify;
import com.ajibigad.corperwee.exceptions.ResourceNotFoundException;
import com.ajibigad.corperwee.exceptions.ReviewExistAlready;
import com.ajibigad.corperwee.model.Place;
import com.ajibigad.corperwee.model.Review;
import com.ajibigad.corperwee.model.User;
import com.ajibigad.corperwee.model.apiModels.Notification;
import com.ajibigad.corperwee.repository.PlaceRepository;
import com.ajibigad.corperwee.repository.ReviewRepository;
import com.ajibigad.corperwee.repository.UserRepository;
import com.ajibigad.corperwee.service.NotificationService;
import com.ajibigad.corperwee.service.PlaceService;
import com.ajibigad.corperwee.service.ReviewService;
import com.ajibigad.corperwee.service.UserService;
import com.ajibigad.corperwee.utils.SomeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Julius on 08/04/2016.
 */
@Service
public class JpaReviewService implements ReviewService {

    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    UserService userService;

    @Autowired
    PlaceService placeService;

    @Override
    public List<Review> findByPlace(Place place) {
        return reviewRepository.findByPlace(place);
    }

    @Override
    public List<Review> findByUser(User user) {
        return reviewRepository.findByUser(user);
    }

    @Override
    public Review findByUserAndPlace(User user, Place place) {
        return reviewRepository.findByUserAndPlace(user, place);
    }

    // @Notify this annotation should be used to remove the concern of sending notifications of this action
    // similar to logging the action in an audit trail
    @Notify(type = Notification.NotificationType.REVIEW, message = ADD_REVIEW_MESSAGE , recipient = Notify.RECIPIENT.USER)
    public Review addReview(Review review) {
        User user = userService.findByUsername(getPrincipal());
        if (reviewRepository.findByUserAndPlace(user, review.getPlace()) == null) {
            review.setUser(user);
            // just temp. This concern must and would be removed
            //String message = user.getUsername() + " just reviewed on a place you added";
            return reviewRepository.save(review);
        } else {
            throw new ReviewExistAlready(review.getPlace().getName());
        }
    }

    @Notify(type = Notification.NotificationType.REVIEW, message = UPDATE_REVIEW_MESSAGE, recipient = Notify.RECIPIENT.USER)
    public Review updateReview(Review newReview) {
        User user = userService.findByUsername(getPrincipal());
        Review review = reviewRepository.findOne(newReview.getId());
        if (review != null) {
            review.setRating(newReview.getRating());
            review.setReviewMessage(newReview.getReviewMessage());
            // just temp. This concern must and would be removed and replaced with an annotation
            //String message = user.getUsername() + " just updated his/her review on a place you added";
            return reviewRepository.save(review);
        } else {
            throw new ResourceNotFoundException("Review with id : " + newReview.getId() + " not found");
        }
    }

    public Review getReviewByUserAndPlace(String username, long placeId) {
        Place place = placeService.getPlace(placeId);
        User user = userService.findByUsername(username);
        List<Object> assertNotNull = Arrays.asList(place, user);
        if (SomeUtils.isAllNotNull(assertNotNull)) {
            return reviewRepository.findByUserAndPlace(user, place);
        } else {
            throw new IllegalArgumentException("Username and Place must exist");
        }
    }

    public String getPrincipal(){
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    // still need to find a way to add the principal's username to the message
    public final static String ADD_REVIEW_MESSAGE = " just reviewed on a place you added";
    public final static String UPDATE_REVIEW_MESSAGE = " just updated his/her review on a place you added";
}
