package com.bckndapp.service;

import com.bckndapp.entity.Projection;
import com.bckndapp.entity.Seat;
import com.bckndapp.entity.Ticket;
import com.bckndapp.entity.User;
import com.bckndapp.repository.ProjectionRepository;
import com.bckndapp.repository.SeatRepository;
import com.bckndapp.repository.TicketRepository;
import com.bckndapp.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.naming.directory.SearchResult;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TicketService {
	private SeatRepository seatRepository;
	private UserRepository userRepository;
	private ProjectionRepository projectionRepository;
	private TicketRepository ticketRepository;
	private ProjectionService projectionService;

	public ResponseEntity buyTicket(Ticket ticket) {

		User user;
		Projection projection;
		Seat seat;
		try{
			user = userRepository.findById(ticket.getUser().getUser_id()).get();
			projection = projectionRepository.findById(ticket.getProjection().getProjection_id()).get();
			seat= seatRepository.findById(ticket.getSeat().getSeat_id()).get();
		}catch (NoSuchElementException e){
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad data in json projection or user doesnt exist");
		}
		ticket.setProjection(projection);
		ticket.setUser(user);
		if(!checkForReservation((List<Seat>) seat.getHall().getSeats(),ticket,projection)){
			return ResponseEntity.status(HttpStatus.CONFLICT).body("ticket was already bought");
		}
		ticket.setSeat(seat);
		user.getTickets().add(ticket);
		userRepository.save(user);
		return ResponseEntity.ok("ticket is successfully added");
	}

	private boolean checkForReservation(List<Seat> seats,Ticket ticket,Projection projection) {
		List<Ticket>allTicketsForProjection=ticketRepository.findAllByProjection(projection);
		List<Seat>freeSeats=projectionService.getFreeSeatsForHall(seats,allTicketsForProjection);
		freeSeats = freeSeats.stream().filter(freeSeat->
				freeSeat.getSeat_id().equals(ticket.getSeat().getSeat_id()))
				.collect(Collectors.toList());
		return freeSeats.size() > 0;
	}
}

