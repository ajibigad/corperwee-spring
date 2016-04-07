package com.ajibigad.corperwee.service;

import com.ajibigad.corperwee.exceptions.CorperWeeException;
import com.ajibigad.corperwee.exceptions.ResourceNotFoundException;
import com.ajibigad.corperwee.model.apiModels.stateApiModels.State;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Julius on 03/04/2016.
 */
@Service
public class StatesApiService {

    private RestTemplate client = new RestTemplate();

    private final String ENDPOINT = "http://45.55.63.193/states-cities/api/v1/";

    private static final Logger LOG = Logger.getLogger(StatesApiService.class);

    public List<State> fetchStates(){
        try{
            State[] states = client.getForObject(ENDPOINT + "states", State[].class);
            return Arrays.asList(states);
        }
        catch(Exception ex){
            LOG.error(ex.getLocalizedMessage());
            LOG.error(ex.getCause().getLocalizedMessage());
            throw new CorperWeeException(ex, ex.getLocalizedMessage());
        }
    }

    public State fetchState(String stateName){
        try {
            ResponseEntity<State> entity = client.getForEntity(ENDPOINT + "state/{name}", State.class, stateName);
            return entity.getBody();
        }
        catch (HttpClientErrorException exception){
            if(exception.getStatusCode().equals(HttpStatus.NOT_FOUND)){
                throw new ResourceNotFoundException("State : "+ stateName + " Not found");
            }
            else{
                LOG.error("Error occurred while fetching state : " + stateName);
                LOG.error("Error occurred due to -> Reason "+ exception.getStatusText());
                throw exception;
            }
        }
    }

    public List<Model> fetchStateLGAs(String stateName){
        try {
            ResponseEntity<Model[]> entity = client.getForEntity(ENDPOINT + "state/{name}/lgas", Model[].class, stateName);
            return Arrays.asList(entity.getBody());
        }
        catch (HttpClientErrorException exception){
            if(exception.getStatusCode().equals(HttpStatus.NOT_FOUND)){
                throw new ResourceNotFoundException("State : "+ stateName + " Not found");
            }
            else{
                LOG.error("Error occurred while fetching lgas for state : " + stateName);
                LOG.error("Error occurred due to -> Reason "+ exception.getStatusText());
                throw exception;
            }
        }
    }

    public List<Model> fetchStateCities(String stateName){
        try {
            ResponseEntity<Model[]> entity = client.getForEntity(ENDPOINT + "state/{name}/cities", Model[].class, stateName);
            return Arrays.asList(entity.getBody());
        }
        catch (HttpClientErrorException exception){
            if(exception.getStatusCode().equals(HttpStatus.NOT_FOUND)){
                throw new ResourceNotFoundException("State : "+ stateName + "Not found");
            }
            else{
                LOG.error("Error occurred while fetching lgas for state : " + stateName);
                LOG.error("Error occurred due to -> Reason "+ exception.getStatusText());
            }
        }
        return null;
    }

    public static class Model { // must be marked as static for deserialization to work(JSON converters concerns)
        // just help sharply create a model with just name property
        public String name;

        public Model(){} // to make JSON converter happy :)
    }


}
