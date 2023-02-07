package com.bckndapp.repository;

import com.bckndapp.entity.Film;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface FilmRepository extends CrudRepository<Film,Long> {
	Optional<Film> findByFilmName(String filmName);
}
