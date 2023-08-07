package com.store.reservation.store.food.repository;

import com.store.reservation.store.food.domain.Food;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodRepository extends JpaRepository<Food, Long> {
}
