package com.bckndapp.entity;

import com.bckndapp.validation.ValidationProjection;
import com.bckndapp.validation.ValidationTicket;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Projection implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "projection_id")
	@NotNull(groups ={ValidationProjection.WhenNotCreatingProjection.class, ValidationTicket.WhenCreatingTicket.class},message = "id cannot be null")
	private Long projection_id;

	@Column(name = "startofprojection")
	private Timestamp startOfProjection;

	@Column(name = "endofprojection")
	private Timestamp endOfProjection;

	@OneToOne(orphanRemoval = true,optional = false,cascade = CascadeType.MERGE)
	@JoinColumn(name = "film_id")
	@NotNull(groups = ValidationProjection.WhenCreatingProjection.class,message = "film cannot be null")
	@Valid
	private Film film;


	@ManyToMany(cascade = CascadeType.MERGE)
	@JoinTable(name = "Projection_halls",
			joinColumns = @JoinColumn(name = "projection_id"),
			inverseJoinColumns = @JoinColumn(name = "halls_hall_id"))
	@NotEmpty(groups = ValidationProjection.WhenCreatingProjection.class,message = "hall cannot be null or empty")
	@Valid
	private Collection<Hall> halls = new ArrayList<>();


}
