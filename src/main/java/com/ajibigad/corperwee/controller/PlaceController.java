package com.ajibigad.corperwee.controller;

import com.ajibigad.corperwee.exceptions.ResourceNotFoundException;
import com.ajibigad.corperwee.model.Place;
import com.ajibigad.corperwee.model.Review;
import com.ajibigad.corperwee.model.User;
import com.ajibigad.corperwee.model.apiModels.SearchParams;
import com.ajibigad.corperwee.repository.PlaceRepository;
import com.ajibigad.corperwee.repository.ReviewRepository;
import com.ajibigad.corperwee.repository.UserRepository;
import com.ajibigad.corperwee.utils.SomeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Julius on 02/03/2016.
 */
@RestController
@RequestMapping("corperwee/api/place")
public class PlaceController {

    @Autowired
    PlaceRepository repository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ReviewRepository reviewRepository;

    @RequestMapping(method = RequestMethod.POST)
    public Place addPlace(@RequestBody Place place, Principal principal){;
        User user = userRepository.findByUsername(principal.getName());
        place.setAddedBy(user);
        return repository.save(place);
    }

    @RequestMapping("/{id}")
    public Place getPlace(@PathVariable Long id){
        try {
            return repository.findOne(id);
        }
        catch (Exception e){
            throw new ResourceNotFoundException("Place with id : " + id + " not Found");
        }
    }

    /*
    * ideally i should use a get here but mehn!!!!
    * I love sending jsons, query params for GET request is boring!!!
    * Imagine me doing ?state=lagos&lga=epe&town=eko
    * Dry abegi*/
    @RequestMapping(value = "town/paged", method = RequestMethod.POST)
    public Page<Place> getPagedPlacesByTown(@RequestBody SearchParams searchParams){
        List<Object> searchParamsAttrs = new ArrayList<Object>()
        {{
                add(searchParams.getCategory());
                add(searchParams.getState());
                add(searchParams.getLga());
                add(searchParams.getTown());
            }};

        if(SomeUtils.isAllNotNull(searchParamsAttrs)){
            PageRequest pageRequest = new PageRequest(searchParams.getPageNumber(),
                    searchParams.getPageSize(),
                    Sort.Direction.valueOf(searchParams.getSortingOrder()),
                    "rating"); // this is to ensure we get the best places first
            return repository.findByCategoryAndStateAndLgaAndTown(searchParams.getCategory(), searchParams.getState(), searchParams.getLga(), searchParams.getTown(), pageRequest);
        }
        else{
            throw new IllegalArgumentException("BAD ARGUMENT .. One of either state, lga, or town is missing from search param");
        }
    }

    @RequestMapping("/reviews/{placeId}")
    public List<Review> getReviews(long placeId) {
        Place place = repository.findOne(placeId);
        if (place != null) {
            return reviewRepository.findByPlace(place);
        } else {
            throw new ResourceNotFoundException("Place with id : " + placeId + "not found");
        }
    }
}
