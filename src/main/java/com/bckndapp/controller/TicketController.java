package com.bckndapp.controller;

import com.bckndapp.entity.Ticket;
import com.bckndapp.service.TicketService;
import com.bckndapp.service.ValidationService;
import com.bckndapp.validation.ValidationTicket;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;



@RequestMapping("/ticket")
@AllArgsConstructor
@RestController
public class TicketController {
	private TicketService ticketService;
	private ValidationService validationService;
	@PostMapping("/private/buy")
	public ResponseEntity buyTicket(@Validated(ValidationTicket.WhenCreatingTicket.class) @RequestBody Ticket ticket, BindingResult bindingResult){
		if (bindingResult.hasErrors())
			return validationService.mapBindingResultToErrorResponse(bindingResult);

		return ticketService.buyTicket(ticket);

	}



}
