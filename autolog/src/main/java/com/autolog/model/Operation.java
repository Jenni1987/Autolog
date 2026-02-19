package com.autolog.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "OPERATION_AUTOLOG")
public class Operation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "IDOPERATION_OPE")
	private Integer id;

	@NotNull(message = "El tipo de operación es obligatorio")
	@Column(name = "TYPE_OPE", nullable = false)
	@Enumerated(EnumType.STRING)
	private OperationType type;

	@NotNull(message = "La fecha es obligatoria")
	@Column(name = "DATE_OPE", nullable = false)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@PastOrPresent(message = "La fecha no puede ser futura")
	private LocalDate date;

	@PositiveOrZero(message = "El coste debe ser cero o positivo")
	@Column(name = "COST_OPE")
	private BigDecimal cost;

	@NotBlank(message = "El lugar es obligatorio")
	@Column(name = "PLACE_OPE", nullable = false)
	private String place;

	@NotBlank(message = "La descripción es obligatoria")
	@Column(name = "DESCRIPTION_OPE", nullable = false)
	private String description;

	@ManyToOne
	@JoinColumn(name = "IDVEHICLE_OPE", nullable = false)
	private Vehicle vehicle;

	@OneToMany(mappedBy = "operation", cascade = CascadeType.ALL)
	private Set<Document> documents = new HashSet<>();

	public enum OperationType {
		MANTENIMIENTO, REPARACION, INSPECCION
	}

	// Getters y Setters
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public OperationType getType() {
		return type;
	}

	public void setType(OperationType type) {
		this.type = type;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public BigDecimal getCost() {
		return cost;
	}

	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Vehicle getVehicle() {
		return vehicle;
	}

	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}

	public Set<Document> getDocuments() {
		return documents;
	}

	public void setDocuments(Set<Document> documents) {
		this.documents = documents;
	}
}
