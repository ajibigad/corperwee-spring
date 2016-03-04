package com.ajibigad.corperwee.repository;

import com.ajibigad.corperwee.model.Place;
import com.ajibigad.corperwee.model.Review;
import com.ajibigad.corperwee.model.User;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * Created by Julius on 04/03/2016.
 */
public interface ReviewRepository extends PagingAndSortingRepository<Review, Long> {

    public List<Review> findByPlace(Place place);

    public List<Review> findByUser(User user);

    public Review findByUserAndPlace(User user, Place place); // used to determine if the user has reviewed this place before
}
