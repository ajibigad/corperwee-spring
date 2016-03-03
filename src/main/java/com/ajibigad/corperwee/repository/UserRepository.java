package com.ajibigad.corperwee.repository;

import com.ajibigad.corperwee.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Julius on 23/02/2016.
 */
@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    public User findByUsername(String username);
}
