package com.ajibigad.corperwee.controller;

import com.ajibigad.corperwee.exceptions.*;
import com.ajibigad.corperwee.exceptions.Error;
import com.ajibigad.corperwee.model.Place;
import com.ajibigad.corperwee.model.User;
import com.ajibigad.corperwee.repository.PlaceRepository;
import com.ajibigad.corperwee.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.security.Principal;
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

    @RequestMapping(method = RequestMethod.GET, params = {"state", "lga", "town"})
    public List<Place> getPlaces(@RequestParam @NotNull String state, @RequestParam String lga, @RequestParam String town){
        return repository.findByStateAndLgaAndTown(state, lga, town);
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
            throw new PlaceNotFound(id);
        }
    }

    @RequestMapping("/paged")
    public Page<Place> getPagedPlaces(){
        return repository.findAll(new PageRequest(0, 10, Sort.Direction.DESC, "name"));
    }

    @ExceptionHandler(PlaceNotFound.class)
    public Error placeNotFound(PlaceNotFound e) {
        return new Error(e.getId(), "Place does not exist");
    }
}
