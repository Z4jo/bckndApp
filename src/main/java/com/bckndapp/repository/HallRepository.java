package com.bckndapp.repository;

import com.bckndapp.entity.Hall;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface HallRepository extends CrudRepository<Hall,Long> {
	Optional<Hall> findByHallNumber(char hallNumber);
}
