package com.feldmann.dscatalog.resources;



import java.util.ArrayList;
import java.util.List;

import com.feldmann.dscatalog.entities.Category;
import com.feldmann.dscatalog.services.CategoryService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping( value = "/categories")
public class CategoryResource {

    private CategoryService categoryService;

    //Aqui ia o annotation @Autowired, mas nao estou usando por boas praticas
    public CategoryResource(CategoryService categoryService){
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<List<Category>> findAll(){
        List<Category> list = categoryService.findAll();
        return ResponseEntity.ok().body(list);
    }

}