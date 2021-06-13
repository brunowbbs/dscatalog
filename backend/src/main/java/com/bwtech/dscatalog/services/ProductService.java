package com.bwtech.dscatalog.services;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bwtech.dscatalog.dto.CategoryDTO;
import com.bwtech.dscatalog.dto.ProductDTO;
import com.bwtech.dscatalog.entities.Category;
import com.bwtech.dscatalog.entities.Product;
import com.bwtech.dscatalog.repositories.CategoryRepository;
import com.bwtech.dscatalog.repositories.ProductRepository;
import com.bwtech.dscatalog.services.exceptions.DatabaseException;
import com.bwtech.dscatalog.services.exceptions.ResourceNotFoundException;

@Service
public class ProductService {

	@Autowired
	private ProductRepository repository;
	
	@Autowired
	private CategoryRepository categoryRepository;

	@Transactional(readOnly = true)
	public Page<ProductDTO> findAll(Pageable pageable) {
		Page<Product> list = repository.findAll(pageable);
		return list.map(item -> new ProductDTO(item));
	}

	@Transactional(readOnly = true)
	public ProductDTO findById(Long id) {
		Optional<Product> optional = repository.findById(id);
		Product entity = optional.orElseThrow(() -> new ResourceNotFoundException("Resource not found "));
		return new ProductDTO(entity, entity.getCategories());
	}

	@Transactional
	public ProductDTO insert(ProductDTO dto) {
		Product entity = new Product();
		entity.setName(dto.getName());
		entity.setDescription(dto.getDescription());
		entity.setDate(dto.getDate());
		entity.setImgUrl(dto.getImgUrl());
		entity.setPrice(dto.getPrice());
		
		for(CategoryDTO item : dto.getCategories()) {
			Category category = categoryRepository.getOne(item.getId());
			entity.getCategories().add(category);
		}
		
		entity = repository.save(entity);
		return new ProductDTO(entity);
	}

	@Transactional
	public ProductDTO update(Long id, ProductDTO dto) {
		try {
			Product entity = repository.getOne(id);
			entity.setName(dto.getName());
			entity.setDescription(dto.getDescription());
			entity.setDate(dto.getDate());
			entity.setImgUrl(dto.getImgUrl());
			entity.setPrice(dto.getPrice());

			for(CategoryDTO item : dto.getCategories()) {
				Category category = categoryRepository.getOne(item.getId());
				entity.getCategories().add(category);
			} 
			
			entity = repository.save(entity);
			return new ProductDTO(entity);
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id " + id + " not found");
		}

	}

	public void delete(Long id) {
		try {
			repository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Id " + id + " not found");
		} catch (DataIntegrityViolationException e) {
			throw new DatabaseException("Integrity Violation");
		}
	}

}
