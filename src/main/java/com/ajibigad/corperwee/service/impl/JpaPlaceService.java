package com.ajibigad.corperwee.service.impl;

import com.ajibigad.corperwee.exceptions.ResourceNotFoundException;
import com.ajibigad.corperwee.exceptions.UnAuthorizedException;
import com.ajibigad.corperwee.model.Category;
import com.ajibigad.corperwee.model.Place;
import com.ajibigad.corperwee.model.Review;
import com.ajibigad.corperwee.model.User;
import com.ajibigad.corperwee.model.apiModels.SearchParams;
import com.ajibigad.corperwee.repository.PlaceRepository;;
import com.ajibigad.corperwee.service.PlaceService;
import com.ajibigad.corperwee.service.ReviewService;
import com.ajibigad.corperwee.service.UserService;
import com.ajibigad.corperwee.utils.SomeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Julius on 07/04/2016.
 */
@Service
public class JpaPlaceService implements PlaceService {

    @Autowired
    PlaceRepository placeRepository;

    @Autowired
    UserService userService;

    @Autowired
    ReviewService reviewService;

    private String placeNotFoundMessage = "Place with id : ? not Found";

    private String formPlaceNotFoundMessage(long id){
        return placeNotFoundMessage.replace("?", String.valueOf(id));
    }

    @Override
    public Place addPlace(Place place) {
        String principal = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByUsername(principal);
        place.setAddedBy(user);
        return placeRepository.save(place);
    }

    @Override
    public Place getPlace(Long id) {
        try {
            return placeRepository.findOne(id);
        }
        catch (Exception e){
            throw new ResourceNotFoundException(formPlaceNotFoundMessage(id));
        }
    }

    @Override
    public Place updatePlace(Place newPlace) {
        Place oldPlace = placeRepository.findOne(newPlace.getId());
        if (oldPlace != null) {
            //the next step is to verify that the person trying to change this place
            //is actually the addedBy of the place and also the logged in user
            User oldAddedBy = oldPlace.getAddedBy();
            String principal = SecurityContextHolder.getContext().getAuthentication().getName();
            boolean authorized = principal.equals(oldPlace.getAddedBy().getUsername());
            if (authorized) {
                try {
                    newPlace.setAddedBy(oldAddedBy);
                    return placeRepository.save(newPlace);
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

    @Override
    public Page<Place> getPagedPlacesByTown(SearchParams searchParams) {
        List<Object> searchParamsAttrs = Arrays.asList(searchParams.getCategory(), searchParams.getState(), searchParams.getLga(), searchParams.getTown());

        if(SomeUtils.isAllNotNull(searchParamsAttrs)){
            PageRequest pageRequest = new PageRequest(searchParams.getPageNumber(),
                    searchParams.getPageSize(),
                    Sort.Direction.valueOf(searchParams.getSortingOrder()),
                    "rating"); // this is to ensure we get the best places first
            return findByCategoryAndStateAndLgaAndTown(searchParams.getCategory(), searchParams.getState(), searchParams.getLga(), searchParams.getTown(), pageRequest);
        }
        else{
            throw new IllegalArgumentException("BAD ARGUMENT .. One of either state, lga, or town is missing from search param");
        }
    }

    @Override
    public Iterable<Place> getPlaces() {
        return placeRepository.findAll();
    }

    @Override
    public List<Review> getReviews(long placeId) {
        Place place = placeRepository.findOne(placeId);
        if (place != null) {
            return reviewService.findByPlace(place);
        } else {
            throw new ResourceNotFoundException(formPlaceNotFoundMessage(placeId));
        }
    }

    /* place service repository methods */

    @Override
    public Page<Place> findPlacesByName(String searchQuery, int page) {
        return placeRepository.findByNameContaining(searchQuery, new PageRequest(page, 10));
    }

    @Override
    public List<Place> findPlacesByName(String searchQuery) {
        return placeRepository.findByNameContaining(searchQuery);
    }

    @Override
    public List<Place> findByStateAndLgaAndTown(String state, String lga, String town) {
        return placeRepository.findByStateAndLgaAndTown(state, lga, town);
    }

    @Override
    public Page<Place> findByStateAndLgaAndTown(String state, String lga, String town, Pageable page) {
        return placeRepository.findByStateAndLgaAndTown(state, lga, town, page);
    }

    @Override
    public List<Place> findByCategoryAndStateAndLgaAndTown(Category category, String state, String lga, String town) {
        return placeRepository.findByCategoryAndStateAndLgaAndTown(category, state, lga, town);
    }

    @Override
    public Page<Place> findByCategoryAndStateAndLgaAndTown(Category category, String state, String lga, String town, Pageable page) {
        return placeRepository.findByCategoryAndStateAndLgaAndTown(category, state, lga, town, page);
    }

    @Override
    public List<Place> findByNameContaining(String name) {
        return placeRepository.findByNameContaining(name);
    }

    @Override
    public Page<Place> findByNameContaining(String name, Pageable page) {
        return placeRepository.findByNameContaining(name, page);
    }
}
