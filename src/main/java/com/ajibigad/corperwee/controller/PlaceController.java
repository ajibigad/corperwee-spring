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
import com.ajibigad.corperwee.utils.SomeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
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

    private String placeNotFoundMessage = "Place with id : ? not Found";

    private String formPlaceNotFoundMessage(long id){
        return placeNotFoundMessage.replace("?", String.valueOf(id));
    }

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
            throw new ResourceNotFoundException(formPlaceNotFoundMessage(id));
        }
    }

    @RequestMapping(method = RequestMethod.PUT)
    public Place updatePlace(@RequestBody Place newPlace, Principal principal) {
        Place oldPlace = repository.findOne(newPlace.getId());
        if (oldPlace != null) {
            //the next step is to verify that the person trying to change this place
            //is actually the addedBy of the place and also the logged in user
            User oldAddedBy = oldPlace.getAddedBy();
            //User newPlaceAddedBy = newPlace.getAddedBy();
            //these are the three things we need to check to really confirm that the person has the full authority to perform this update
            //boolean authenticated = oldAddedBy.getId() == newPlaceAddedBy.getId() && oldAddedBy.getUsername().equals(newPlaceAddedBy.getUsername()) && principal.getName().equals(newPlaceAddedBy.getUsername());
            boolean authenticated = principal.getName().equals(oldPlace.getAddedBy().getUsername());
            if (authenticated) {
                try {
                    newPlace.setAddedBy(oldAddedBy);
                    return repository.save(newPlace);
                } catch (JpaObjectRetrievalFailureException ex) {
                    String modelNotfound = ex.getLocalizedMessage().substring(ex.getLocalizedMessage().indexOf("corperwee.model.") + "corperwee.model.".length(), ex.getLocalizedMessage().indexOf(";"));
                    throw new ResourceNotFoundException(modelNotfound + " not found");
                }
            }
            else{
                throw new UnAuthorizedException();
            }
        }
        else{
            throw new ResourceNotFoundException(formPlaceNotFoundMessage(newPlace.getId()));
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

    @RequestMapping("/{placeId}/reviews")
    public List<Review> getReviews(@PathVariable long placeId) {
        Place place = repository.findOne(placeId);
        if (place != null) {
            return reviewRepository.findByPlace(place);
        } else {
            throw new ResourceNotFoundException(formPlaceNotFoundMessage(placeId));
        }
    }
}
