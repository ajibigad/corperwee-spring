package com.ajibigad.corperwee.controller;

import com.ajibigad.corperwee.model.apiModels.stateApiModels.State;
import com.ajibigad.corperwee.service.StatesApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Julius on 03/04/2016.
 */
@RestController
@RequestMapping("corperwee/api/state")
public class StatesApiController {

    @Autowired
    StatesApiService statesApiService;

    @RequestMapping("/states")
    public List<State> fetchStates(){
        return statesApiService.fetchStates();
    }

    @RequestMapping("/{stateName}")
    public State fetchState(@PathVariable String stateName){
        return statesApiService.fetchState(stateName);
    }

    @RequestMapping("/{stateName}/lgas")
    public List fetchStateLGAs(@PathVariable String stateName){
        return statesApiService.fetchStateLGAs(stateName);
    }

    @RequestMapping("/{stateName}/cities")
    public List fetchStateCities(@PathVariable String stateName){
        return statesApiService.fetchStateCities(stateName);
    }

    @RequestMapping(method = RequestMethod.GET)
    public StatesApiService.Model fetchLGA(@RequestParam String lga, @RequestParam String state){
        return statesApiService.fetchLGA(state, lga);
    }
}
