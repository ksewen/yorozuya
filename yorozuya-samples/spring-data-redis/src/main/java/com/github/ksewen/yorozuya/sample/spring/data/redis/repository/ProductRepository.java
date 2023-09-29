package com.github.ksewen.yorozuya.sample.spring.data.redis.repository;

import com.github.ksewen.yorozuya.sample.spring.data.redis.model.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author ksewen
 * @date 22.09.2023 12:11
 */
@Repository
public interface ProductRepository extends CrudRepository<Product, Long> {}
