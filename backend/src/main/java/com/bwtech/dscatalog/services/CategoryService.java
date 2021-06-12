package com.bwtech.dscatalog.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bwtech.dscatalog.dto.CategoryDTO;
import com.bwtech.dscatalog.entities.Category;
import com.bwtech.dscatalog.repositories.CategoryRepository;
import com.bwtech.dscatalog.services.exceptions.ResourceNotFoundException;

@Service
public class CategoryService {

	@Autowired
	private CategoryRepository repository;

	@Transactional(readOnly = true)
	public List<CategoryDTO> findAll() {
		List<Category> list = repository.findAll();
		return list.stream().map(item -> new CategoryDTO(item)).collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public CategoryDTO findById(Long id) {
		Optional<Category> optional = repository.findById(id);
		Category entity = optional.orElseThrow(() -> new ResourceNotFoundException("Resource not found "));
		return new CategoryDTO(entity);
	}

}
