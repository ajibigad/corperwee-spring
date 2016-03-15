package com.ajibigad.corperwee.controller;

import com.ajibigad.corperwee.model.Category;
import com.ajibigad.corperwee.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Julius on 01/03/2016.
 */
@RestController
@RequestMapping("corperwee/api/category")
public class CategoryController {

    @Autowired
    CategoryRepository repository;

    @RequestMapping(method = RequestMethod.GET)
    public Iterable<Category> getCategories() {
        return repository.findAll();
    }
}
