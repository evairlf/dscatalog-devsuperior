package com.feldmann.dscatalog.services;

import java.util.Optional;


import javax.persistence.EntityNotFoundException;

import com.feldmann.dscatalog.dto.CategoryDTO;
import com.feldmann.dscatalog.dto.ProductDTO;
import com.feldmann.dscatalog.entities.Category;
import com.feldmann.dscatalog.entities.Product;
import com.feldmann.dscatalog.repositories.CategoryRepository;
import com.feldmann.dscatalog.repositories.ProductRepository;
import com.feldmann.dscatalog.services.exceptions.DatabaseException;
import com.feldmann.dscatalog.services.exceptions.ResourceNotFoundException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    private ProductRepository repository;
    private CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.repository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional(readOnly = true)
    public Page<ProductDTO> findAllPaged(PageRequest pageRequest) {
        Page<Product> list = repository.findAll(pageRequest);
        // Stream() = transforma para stream para poder usar funções de alta ordem
        // map() = mapeia o objeto e para cada valor do index x ele faz algo com a arrow
        // function que no java é -> e nao =>
        // Collect() = Desfaz a transformação de stream e volta para a forma desejada no
        // caso CollectorstoList()
        return list.map(x -> new ProductDTO(x));

    }

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id) {
        Optional<Product> obj = repository.findById(id);
        Product entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not Found"));
        return new ProductDTO(entity, entity.getCategories());
    }

    @Transactional
    public ProductDTO insert(ProductDTO dto) {
        Product entity = new Product();
        copyDtoToEntity(dto, entity);
        entity = repository.save(entity);
        return new ProductDTO(entity);
    }

    @Transactional
    public ProductDTO update(Long id, ProductDTO dto) {
        try {
            Product entity = repository.getById(id);
            copyDtoToEntity(dto, entity);
            entity = repository.save(entity);
            return new ProductDTO(entity);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("id Not Found"+id);
        }
    }

    public void delete(Long id) {
        try {
            repository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("id not found "+ id);
        } catch (DataIntegrityViolationException e){
            throw new DatabaseException("Integrity Violation");
        }
        
    }

    private void copyDtoToEntity(ProductDTO dto, Product entity) {
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setDate((dto.getDate()));
        entity.setPrice(dto.getPrice());
        entity.setImgUrl(dto.getImgUrl());

        entity.getCategories().clear();

        for(CategoryDTO catDto : dto.getCategories()){
            Category category = categoryRepository.getById(catDto.getId());
            entity.getCategories().add(category);
        }
    }

}