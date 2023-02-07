package com.bckndapp.entity;

import com.bckndapp.validation.ValidationTicket;
import com.bckndapp.validation.ValidationUser;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "user_in_app")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@NotNull(groups = {ValidationUser.WhenNotCreatingUser.class,
			ValidationTicket.WhenCreatingTicket.class},
			message = "user_id cannot be null")
	private Long user_id;

	@Enumerated(value = EnumType.STRING)
	@Column(nullable = false)
	@NotNull(groups = ValidationUser.WhenUpdatingUserUser.class,message = "role cannot be null")
	private Role role;
	@Column(nullable = false,unique = true)
	@NotEmpty(groups = {ValidationUser.WhenCreatingUser.class, ValidationUser.WhenUpdatingUserUser.class},message = "username cannot be empty")
	private String username;
	@Column(nullable = false)
	@NotEmpty(groups = ValidationUser.WhenCreatingUser.class,message = "password cannot be empty")
	private String password;

	@OneToMany(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.MERGE)
	private Collection<Ticket> tickets;

}
