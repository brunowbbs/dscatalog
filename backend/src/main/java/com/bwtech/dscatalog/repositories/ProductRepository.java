package com.bwtech.dscatalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bwtech.dscatalog.entities.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
