package com.bckndapp.entity;

import com.bckndapp.validation.ValidationTicket;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "seat")
public class Seat implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "seat_id")
	@NotNull(groups = ValidationTicket.WhenCreatingTicket.class,message = "seat_id cannot be null")
	private Long seat_id;

	@Column(nullable = false,name = "row")
	private char row;
	@Column(nullable = false,name = "numberofseat")
	private int  numberOfSeat;


	@JsonIgnore
	@ManyToOne
	@JoinColumn(name="hall_id")
	private Hall hall;
}
