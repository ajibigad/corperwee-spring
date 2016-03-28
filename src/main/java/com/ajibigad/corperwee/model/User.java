package com.ajibigad.corperwee.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by Julius on 23/02/2016.
 */
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String username;
    private String password;
    private String firstname;
    private String lastname;
    private String state;
    private String lga;
    private String town;
    private String email;
    private String stateCode;
    private String phoneNumber;
    private String profilePicture;

    @Value("#{T(System).currentTimeMillis()}")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateJoined;

    @JsonIgnore
    @OneToMany(mappedBy = "addedBy", fetch = FetchType.LAZY)
    private List<Place> placesAdded;

    @PrePersist
    public void prePersist() {
        if(profilePicture == null) //We set default value in case if the value is not set yet.
            profilePicture = "default.jpeg";
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    @JsonProperty
    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getLga() {
        return lga;
    }

    public void setLga(String lga) {
        this.lga = lga;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<Place> getPlacesAdded() {
        return placesAdded;
    }

    public void setPlacesAdded(List<Place> placesAdded) {
        this.placesAdded = placesAdded;
    }

    public Date getDateJoined() {
        return dateJoined;
    }

    public void setDateJoined(Date dateJoined) {
        this.dateJoined = dateJoined;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }
}
