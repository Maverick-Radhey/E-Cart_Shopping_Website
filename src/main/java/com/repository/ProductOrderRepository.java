package com.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.model.ProductOrder;

public interface ProductOrderRepository extends JpaRepository<ProductOrder, Integer>{

}
