package com.autolog.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "VEHICLE_AUTOLOG")
public class Vehicle {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "IDVEHICLE_VEA")
	private Integer id;

	@NotNull(message = "El tipo de vehículo es obligatorio")
	@Column(name = "TYPE_VEA", nullable = false)
	@Enumerated(EnumType.STRING)
	private VehicleType type;

	@NotBlank(message = "La marca es obligatoria")
	@Column(name = "BRAND_VEA", nullable = false)
	private String brand;

	@NotBlank(message = "El modelo es obligatorio")
	@Column(name = "MODEL_VEA", nullable = false)
	private String model;

	@NotBlank(message = "La matrícula es obligatoria")
	@Pattern(
		    regexp = "^[A-Z]{1,2}[0-9]{1,5}[A-Z]{1,2}$|^[0-9]{4}[BCDFGHJKLMNPRSTVWXYZ]{3}$",
		    message = "Formato inválido. Debe ser 1234BCD o AB123CD"
		)
	@Column(name = "PLATE_VEA", nullable = false, unique = true)
	private String plate;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@PastOrPresent(message = "La fecha no puede ser futura")
	@Column(name = "REGISTRATIONDATE_VEA")
	private LocalDate registrationDate;

	@ManyToOne
	@JoinColumn(name = "IDUSER_VEA", nullable = false)
	private User user;

	@OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL)
	private List<Operation> operations;

	public enum VehicleType {
		COCHE, FURGON, MOTO, CAMION
	}

	// Getters y Setters
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public VehicleType getType() {
		return type;
	}

	public void setType(VehicleType type) {
		this.type = type;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getPlate() {
		return plate;
	}

	public void setPlate(String plate) {
		this.plate = plate;
	}

	public LocalDate getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(LocalDate registrationDate) {
		this.registrationDate = registrationDate;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<Operation> getOperations() {
		return operations;
	}

	public void setOperations(List<Operation> operations) {
		this.operations = operations;
	}
}
