package com.bckndapp.entity;

import com.bckndapp.validation.ValidationProjection;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Entity
public class Hall implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "hall_id")
	@NotNull(groups = ValidationProjection.WhenCreatingProjection.class,message = "hall_id cannot be null")
	private Long hallId;

	@Column(unique = true,nullable = false, name = "hallnumber")
	private char hallNumber;
	@Column(nullable = false,name = "capacity")
	private int capacity;

	@OneToMany(orphanRemoval = true,cascade = CascadeType.MERGE,mappedBy = "hall")
	private Collection<Seat> seats;

	@JsonIgnore
	@ManyToMany(mappedBy = "halls")
	private Collection<Projection> projections = new ArrayList<>();

}
