package com.feldmann.dscatalog.services;

import java.util.List;
import java.util.stream.Collectors;

import com.feldmann.dscatalog.dto.CategoryDTO;
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
    public List<CategoryDTO> findAll(){
      List<Category> list = repository.findAll();
      //Stream() = transforma para stream para poder usar funções de alta ordem
      //map() = mapeia o objeto e para cada valor do index x ele faz algo com a arrow function que no java é -> e nao =>
      //Collect() = Desfaz a transformação de stream e volta para a forma desejada no caso CollectorstoList()
      return list.stream().map(x -> new CategoryDTO(x)).collect(Collectors.toList());
       
    }
}