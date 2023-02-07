package com.bckndapp.repository;

import com.bckndapp.entity.Seat;
import org.springframework.data.repository.CrudRepository;

public interface SeatRepository extends CrudRepository<Seat,Long> {
}
