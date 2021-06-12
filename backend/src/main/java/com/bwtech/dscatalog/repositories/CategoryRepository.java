package com.bwtech.dscatalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bwtech.dscatalog.entities.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}
