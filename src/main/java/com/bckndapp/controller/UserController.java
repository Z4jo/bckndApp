package com.bckndapp.controller;

import com.bckndapp.entity.User;
import com.bckndapp.service.UserService;
import com.bckndapp.service.ValidationService;
import com.bckndapp.validation.ValidationUser;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequestMapping(value = "/user")
@RestController
@AllArgsConstructor
public class UserController {
	private UserService userService;
	private ValidationService validationService;

	@PostMapping(value = "/public/register")
	public ResponseEntity register(@Validated(ValidationUser.WhenCreatingUser.class) @RequestBody User user, BindingResult bindingResult){
		if(bindingResult.hasErrors()) return validationService.mapBindingResultToErrorResponse(bindingResult);
		return userService.registerUser(user);
	}

	@GetMapping("/private/login")
	public ResponseEntity login(){
		return ResponseEntity.ok("ok");
	}

	@PutMapping("/admin/change/role")
	public ResponseEntity changeRole(@Validated(ValidationUser.WhenUpdatingUserUser.class) @RequestBody User user, BindingResult bindingResult){
		if(bindingResult.hasErrors()) return validationService.mapBindingResultToErrorResponse(bindingResult);
		return userService.changeRole(user);
	}


}
