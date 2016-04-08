package com.ajibigad.corperwee.service;

import com.ajibigad.corperwee.model.Category;
import com.ajibigad.corperwee.model.Place;
import com.ajibigad.corperwee.model.Review;
import com.ajibigad.corperwee.model.apiModels.SearchParams;
import com.ajibigad.corperwee.repository.PlaceRepository;
import org.apache.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Julius on 07/04/2016.
 */
public interface PlaceService{

    public static final Logger LOG = Logger.getLogger(PlaceService.class);

    public Place addPlace(Place place);

    public Place getPlace(Long id);

    public Place updatePlace(Place newPlace);

    public Iterable<Place> getPlaces();

    public Page<Place> getPagedPlacesByTown(SearchParams searchParams);

    public List<Review> getReviews(long placeId);

    public Page<Place> findPlacesByName(String searchQuery, int page);

    public List<Place> findPlacesByName(String searchQuery);

    public List<Place> findByStateAndLgaAndTown(String state, String lga, String town);

    public Page<Place> findByStateAndLgaAndTown(String state, String lga, String town, Pageable page);

    public List<Place> findByCategoryAndStateAndLgaAndTown(Category category, String state, String lga, String town);

    public Page<Place> findByCategoryAndStateAndLgaAndTown(Category category, String state, String lga, String town, Pageable page);

    public List<Place> findByNameContaining(String name);

    public Page<Place> findByNameContaining(String name, Pageable page);

}
