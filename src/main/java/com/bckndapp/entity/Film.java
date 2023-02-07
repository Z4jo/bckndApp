package com.bckndapp.entity;

import com.bckndapp.validation.ValidationFilm;
import com.bckndapp.validation.ValidationProjection;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Film implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "film_id")
	@NotNull(groups ={ValidationFilm.WhenDeletingFilm.class, ValidationProjection.WhenCreatingProjection.class},message = "film_id cannot be null")
	private Long film_id;

	@Column(unique = true,nullable = false,name = "filmname")
	@NotEmpty(groups = ValidationFilm.WhenCreatingFilm.class,message = "filmname cannot be empty")
	private String filmName;
	@Column(nullable = false, name = "lengthinminutes")
	@NotNull(groups = {ValidationFilm.WhenCreatingFilm.class},
			message = "length of a movie is required")
	private Integer lengthInMinutes;
	@Column(nullable = false, name = "is3D")
	@NotNull(groups = ValidationFilm.WhenCreatingFilm.class,message = "3D cannot be null")
	private Boolean is3D;
}
