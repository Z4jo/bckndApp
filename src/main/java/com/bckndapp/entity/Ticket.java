package com.bckndapp.entity;


import com.bckndapp.validation.ValidationTicket;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Ticket implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ticket_id")
	private Long ticket_id;

	@Column(nullable = false,name = "price")
	@NotNull(groups = ValidationTicket.WhenCreatingTicket.class, message = "price cannot be null")
	private int price;

	@ManyToOne
	@JoinColumn(name = "tickets",nullable = false)
	@NotNull(groups = ValidationTicket.WhenCreatingTicket.class, message = "user cannot be null")
	@Valid
	private User user;

	@OneToOne(cascade = CascadeType.MERGE, optional = false, orphanRemoval = true)
	@JoinColumn(name = "projection_id", nullable = false)
	@NotNull(groups = ValidationTicket.WhenCreatingTicket.class,message = "projection cannot be null")
	@Valid
	private Projection projection;

	@OneToOne(cascade = CascadeType.MERGE, optional = false, orphanRemoval = true)
	@JoinColumn(name = "seat_id", nullable = false,unique = true)
	@NotNull(groups = ValidationTicket.WhenCreatingTicket.class,message = "seat cannot be null")
	@Valid
	private Seat seat;

}
