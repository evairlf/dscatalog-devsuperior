package com.feldmann.dscatalog.services;

import java.util.List;

import com.feldmann.dscatalog.entities.Category;
import com.feldmann.dscatalog.repositories.CategoryRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoryService {
    
   
    private CategoryRepository repository;


    public CategoryService(CategoryRepository categoryRepository){
        this.repository = categoryRepository;
    }

    @Transactional(readOnly = true)
    public List<Category> findAll(){
        return repository.findAll();
    }
}