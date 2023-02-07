package com.bckndapp.repository;

import com.bckndapp.entity.Hall;
import com.bckndapp.entity.Projection;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.data.repository.CrudRepository;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ProjectionRepository extends CrudRepository<Projection,Long> {
	List<Projection> findAllByStartOfProjectionBetween(Timestamp firstDate, Timestamp secondDate);

	Optional<Projection> findFirstByStartOfProjectionBeforeAndHalls_HallId(Timestamp startOfProjection, Long hallId);

}
