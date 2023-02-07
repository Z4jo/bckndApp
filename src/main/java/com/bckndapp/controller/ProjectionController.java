package com.bckndapp.controller;

import com.bckndapp.entity.Projection;
import com.bckndapp.service.ProjectionService;
import com.bckndapp.service.ValidationService;
import com.bckndapp.validation.ValidationProjection;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/projection")
@AllArgsConstructor
public class ProjectionController {
	private ProjectionService projectionService;
	private ValidationService validationService;

	@PostMapping("/staff/create/{date}")
	public ResponseEntity createProjection(@Validated(value = ValidationProjection.WhenCreatingProjection.class)
	                     @RequestBody Projection projection,BindingResult bindingResult, @PathVariable String date){
		if (bindingResult.hasErrors()) return validationService.mapBindingResultToErrorResponse(bindingResult);
		return projectionService.createProjection(projection,date);
	}

	@GetMapping("/private/all/free/seats/{hallNumber}")
	public ResponseEntity getAllFreeSeatsForProjection(@Validated(ValidationProjection.WhenNotCreatingProjection.class)
	                                                       @RequestBody Projection projection,BindingResult bindingResult, @PathVariable char hallNumber){
		if(bindingResult.hasErrors())return validationService.mapBindingResultToErrorResponse(bindingResult);
		return projectionService.getAllFreeSeatsForProjection(projection,hallNumber);
	}

	@GetMapping("/public/all")
	public ResponseEntity getAllProjections(){
		return projectionService.getAllProjections();
	}

	@GetMapping("/staff/all/free/halls/{beginning}/{end}")
	public ResponseEntity getAllFreeHallsByDate(@PathVariable String beginning,@PathVariable String end){
		return  projectionService.getFreeHallsByDate(beginning,end);
	}
}
