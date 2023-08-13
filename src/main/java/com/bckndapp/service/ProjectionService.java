package com.bckndapp.service;

import com.bckndapp.entity.*;
import com.bckndapp.repository.FilmRepository;
import com.bckndapp.repository.HallRepository;
import com.bckndapp.repository.ProjectionRepository;
import com.bckndapp.repository.TicketRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProjectionService {
	private FilmService filmService;
	private ProjectionRepository projectionRepository;
	private HallRepository hallRepository;
	private TicketRepository ticketRepository;
	private FilmRepository filmRepository;

	//date format:yyyy-MM-dd hh:mm
	public ResponseEntity createProjection(Projection projection, String date) {
		Film film;
		if(filmService.doesFilmExistsById(projection.getFilm().getFilm_id())){
			film=filmRepository.findById(projection.getFilm().getFilm_id()).get();
			try {
				projection.setStartOfProjection(parseDateToTimeStamp(date));
				projection.setEndOfProjection(addMinutes(projection.getStartOfProjection(),film.getLengthInMinutes()));
			}catch(ParseException e){
				e.printStackTrace();
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("bad string format");
			}
			if (isHallFree(projection.getStartOfProjection(),projection.getEndOfProjection(), (List<Hall>) projection.getHalls())){
				projectionRepository.save(projection);
				return ResponseEntity.status(HttpStatus.CREATED).body("Projection was added to database");
			}
			return ResponseEntity.status(HttpStatus.CONFLICT).body("One of the halls is reserved for other projection");
		}else{
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Film is not in the database");
		}

	}

	private Timestamp addMinutes(Timestamp startOfProjection, int lengthInMinutes) {
		long minuteInMilliseconds=60000;
		return new Timestamp(startOfProjection.getTime()+(lengthInMinutes*minuteInMilliseconds));
	}


	private Timestamp parseDateToTimeStamp(String date)  throws ParseException{
		var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		LocalDateTime dateTime = LocalDateTime.parse(date,formatter);
		return Timestamp.valueOf(dateTime);
	}

	public ResponseEntity getAllProjections() {
		return ResponseEntity.ok(projectionRepository.findAll());
	}

	public ResponseEntity getFreeHallsByDate(String beginning,String end) {
		Timestamp beginningDate;
		Timestamp endDate;
		ArrayList<Hall> allHalls= (ArrayList<Hall>) hallRepository.findAll();
		try {
			beginningDate= parseDateToTimeStamp(beginning);
			endDate=parseDateToTimeStamp(end);
		} catch (ParseException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("wrong date format");
		}
		List<Projection>allConflictProjections=getAllConflictProjections(allHalls,beginningDate,endDate);
		if(allConflictProjections.size()==0){
			return ResponseEntity.ok(allHalls);
		}
		ArrayList<Hall> freeHalls= getFreeHalls(allConflictProjections,allHalls);
		return ResponseEntity.ok(freeHalls);
	}


	private ArrayList<Hall> getFreeHalls(List<Projection> projections, ArrayList<Hall> allHalls) {
		for(Projection projection:projections){
			Collection<Hall> hallsInUse = projection.getHalls();
			for(Hall hallInUse:hallsInUse){
				allHalls.removeIf(hall -> hall.getHallId().equals(hallInUse.getHallId()));
			}
		}
		return allHalls;
	}

	private boolean isHallFree(Timestamp startOfMovie, Timestamp endOfMovie, List<Hall> halls){
		int initialSize = halls.size();
		List<Projection> allConflictProjections=getAllConflictProjections(halls,startOfMovie,endOfMovie);
		ArrayList<Hall> allHalls= (ArrayList<Hall>) hallRepository.findAll();

		if(allConflictProjections.size()==0) return true;
		List<Hall>freeHalls= getFreeHalls(allConflictProjections,allHalls);
		if (freeHalls.size()==0) return false;

		for (Hall freeHall: freeHalls){
			halls.removeIf(hall -> !freeHall.getHallId().equals(hall.getHallId()));
		}
		return initialSize == halls.size();
	}

	private List<Projection> getAllConflictProjections(List<Hall> halls, Timestamp startOfMovie,Timestamp endOfMovie) {
		List<Projection>conflictProjections	= projectionRepository.findAllByEndOfProjectionBetween(startOfMovie,endOfMovie);
		conflictProjections.addAll(projectionRepository.findAllByStartOfProjectionBetween(startOfMovie,endOfMovie));
		for(Hall hall: halls){

			Optional<Projection>optionalProjection =projectionRepository.findFirstByStartOfProjectionBeforeAndHalls_HallId(startOfMovie,hall.getHallId());
			if(optionalProjection.isPresent()){
				if(optionalProjection.get().getEndOfProjection().after(startOfMovie)){
					conflictProjections.add(optionalProjection.get());
				}
			}
		}
		return conflictProjections;
	}


	public ResponseEntity getAllFreeSeatsForProjection(Projection projection,char hallNumber) {
		Optional<Projection> optionalProjection = projectionRepository.findById(projection.getProjection_id());
		if(optionalProjection.isEmpty()){
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No projection with this id");
		}
		List<Ticket> tickets= ticketRepository.findAllByProjection(optionalProjection.get());
		Optional<Hall> optionalHall= hallRepository.findByHallNumber(hallNumber);

		if(optionalHall.isEmpty()||isHallInProjection(optionalProjection.get(),optionalHall.get())){
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("bad or non existent hall number: "+ hallNumber);
		}
		List<Seat> allSeatsForHall = (List<Seat>) optionalHall.get().getSeats();
		if(tickets.size()==0){
			return ResponseEntity.ok(allSeatsForHall);
		}
		List<Seat>freeSeats=getFreeSeatsForHall(allSeatsForHall,tickets);
		if(freeSeats.isEmpty()){
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body("no seats are available for this hall"+hallNumber);
		}
		return ResponseEntity.ok(freeSeats);
	}

	private boolean isHallInProjection(Projection projection, Hall hall) {
		List<Hall> halls = (List<Hall>) projection.getHalls();
		halls = halls.stream().filter(h->
				h.getHallId().equals(hall.getHallId())).collect(Collectors.toList());
		return  !(halls.size()>0);
	}

	protected List<Seat> getFreeSeatsForHall(List<Seat> allSeatsForHall, List<Ticket> tickets) {
		for(Ticket ticket:tickets) {
			allSeatsForHall.removeIf(seat -> seat.getSeat_id().equals(ticket.getSeat().getSeat_id()));
		}
		return allSeatsForHall;
	}
}
