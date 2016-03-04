package com.ajibigad.corperwee.repository;

import com.ajibigad.corperwee.model.Category;
import com.ajibigad.corperwee.model.Place;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * Created by Julius on 02/03/2016.
 */
public interface PlaceRepository extends PagingAndSortingRepository<Place, Long> {

    public List<Place> findByStateAndLgaAndTown(String state, String lga, String town);

    public Page<Place> findByStateAndLgaAndTown (String state, String lga, String town, Pageable page);

    public List<Place> findByCategoryAndStateAndLgaAndTown(Category category, String state, String lga, String town);

    public Page<Place> findByCategoryAndStateAndLgaAndTown (Category category, String state, String lga, String town, Pageable page);
}
