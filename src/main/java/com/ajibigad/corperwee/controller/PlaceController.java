package com.ajibigad.corperwee.controller;

import com.ajibigad.corperwee.exceptions.ResourceNotFoundException;
import com.ajibigad.corperwee.exceptions.UnAuthorizedException;
import com.ajibigad.corperwee.model.Place;
import com.ajibigad.corperwee.model.Review;
import com.ajibigad.corperwee.model.User;
import com.ajibigad.corperwee.model.apiModels.SearchParams;
import com.ajibigad.corperwee.repository.PlaceRepository;
import com.ajibigad.corperwee.repository.ReviewRepository;
import com.ajibigad.corperwee.repository.UserRepository;
import com.ajibigad.corperwee.service.PlaceService;
import com.ajibigad.corperwee.utils.SomeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Julius on 02/03/2016.
 */
@RestController
@RequestMapping("corperwee/api/place")
public class PlaceController {

    @Autowired
    PlaceService placeService;

    @RequestMapping(method = RequestMethod.POST)
    public Place addPlace(@RequestBody Place place, Principal principal){;
        return placeService.addPlace(place);
    }

    @RequestMapping("/{id}")
    public Place getPlace(@PathVariable Long id){
        return placeService.getPlace(id);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public Place updatePlace(@RequestBody Place newPlace, Principal principal) {
        return placeService.updatePlace(newPlace);
    }

    /*
    * ideally i should use a get here but mehn!!!!
    * I love sending jsons, query params for GET request is boring!!!
    * Imagine me doing ?state=lagos&lga=epe&town=eko
    * Dry abegi*/
    @RequestMapping(value = "town/paged", method = RequestMethod.POST)
    public Page<Place> getPagedPlacesByTown(@RequestBody SearchParams searchParams){
        return placeService.getPagedPlacesByTown(searchParams);
    }

    @RequestMapping(method = RequestMethod.GET)
    public Iterable<Place> getPlaces() {
        return placeService.getPlaces();
    }

    @RequestMapping("/{placeId}/reviews")
    public List<Review> getReviews(@PathVariable long placeId) {
        return placeService.getReviews(placeId);
    }

    @RequestMapping("/searchPlacesByName/paged/{searchQuery}")
    public Page<Place> findPlacesByName(@PathVariable String searchQuery, @Value("0") int page) {
        return placeService.findPlacesByName(searchQuery, page);
    }

    @RequestMapping("/searchPlacesByName/{searchQuery}")
    public List<Place> findPlacesByName(@PathVariable String searchQuery) {
        return placeService.findPlacesByName(searchQuery);
    }
}
