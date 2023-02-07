package com.bckndapp.controller;

import com.bckndapp.entity.Film;
import com.bckndapp.service.FilmService;
import com.bckndapp.service.ValidationService;
import com.bckndapp.validation.ValidationFilm;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RequestMapping(value = "/film")
@RestController
@AllArgsConstructor
public class FilmController {
	private FilmService filmService;
	private ValidationService validationService;
	@PostMapping(value = "/staff/add/to/registry")
	public ResponseEntity addFilmToRegistry(@Validated(ValidationFilm.WhenCreatingFilm.class)
	                                            @RequestBody Film film, BindingResult bindingResult){
		if(bindingResult.hasErrors()) return validationService.mapBindingResultToErrorResponse(bindingResult);
		return filmService.addFilmToRegistry(film);
	}

	@GetMapping(value = "/public/all")
	public ResponseEntity getAllFilms(){
		return filmService.getAllFilms();
	}

	@DeleteMapping("/staff/delete/films")
	public ResponseEntity deleteFilms(@RequestBody ArrayList<Film> filmsToDelete){
		return filmService.deleteFilms(filmsToDelete);
	}

}
