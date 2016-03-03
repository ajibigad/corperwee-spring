package com.ajibigad.corperwee.controller;

import com.ajibigad.corperwee.model.Category;
import com.ajibigad.corperwee.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Julius on 01/03/2016.
 */
@RestController
@RequestMapping("corperwee/api/category")
public class CategoryController {

    @Autowired
    CategoryRepository repository;

    @RequestMapping(method = RequestMethod.GET)
    public List<Category> getCategories(){
        List<Category> catogories = new ArrayList<Category>();
        for (Category category : repository.findAll()){
            catogories.add(category);
        }
        return catogories;
    }
}
