package com.bckndapp.service;

import com.bckndapp.entity.Film;
import com.bckndapp.repository.FilmRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
@AllArgsConstructor
public class FilmService {

	private FilmRepository filmRepository;

	public ResponseEntity addFilmToRegistry(Film film) {
		if(!doesFilmExistsByName(film.getFilmName())){
			filmRepository.save(film);
			return ResponseEntity.status(HttpStatus.CREATED).body("film was added to database");
		}
		return ResponseEntity.status(HttpStatus.CONFLICT).body("film is already in database");
	}

	private boolean doesFilmExistsByName(String filmName) {
		Optional<Film> optionalFilm = filmRepository.findByFilmName(filmName);
		return optionalFilm.isPresent();
	}

	protected boolean doesFilmExistsById(Long filmId) {
		if(filmId == null) return false;
		Optional<Film> optionalFilm = filmRepository.findById(filmId);
		return optionalFilm.isPresent();
	}

	public ResponseEntity getAllFilms() {
		return ResponseEntity.status(200).body(filmRepository.findAll());
	}

	public ResponseEntity deleteFilms(ArrayList<Film> filmsToDelete) {
		for(Film film : filmsToDelete){
			if(!doesFilmExistsById(film.getFilm_id())){
				return ResponseEntity.status(HttpStatus.CONFLICT).body("film with this id:"+film.getFilm_id()
						+" is not in the database");
			}
		}
		filmRepository.deleteAll(filmsToDelete);
		return ResponseEntity.ok("All films deleted");
	}
}
