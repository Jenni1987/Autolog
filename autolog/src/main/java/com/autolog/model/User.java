package com.autolog.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

import com.autolog.validation.OnCreate;

@Entity
@Table(name = "USER_AUTOLOG")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "IDUSER_USA")
	private Integer id;

	@NotBlank(message = "El nombre de usuario es obligatorio")
	@Column(name = "USERNAME_USA", nullable = false, unique = true)
	private String username;

	@NotBlank(message = "El correo electrónico es obligatorio")
	@Email(message = "El correo electrónico no es válido")
	@Column(name = "EMAIL_USA", nullable = false, unique = true)
	private String email;

	@NotBlank(message = "La contraseña es obligatoria", groups = OnCreate.class)
	@Column(name = "PASSWORD_USA", nullable = false)
	private String password;

	@Column(name = "S_ACTIVEROW_USA")
	private Boolean activeRow = true;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private Set<Vehicle> vehicles = new HashSet<>();

	@ManyToMany
	@JoinTable(name = "USERROL_AUTOLOG", joinColumns = @JoinColumn(name = "IDUSER_URA"), inverseJoinColumns = @JoinColumn(name = "IDROL_URA"))
	private Set<Rol> roles = new HashSet<>();

	// Getters y Setters
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean getActiveRow() {
		return activeRow;
	}

	public void setActiveRow(Boolean activeRow) {
		this.activeRow = activeRow;
	}

	public Set<Vehicle> getVehicles() {
		return vehicles;
	}

	public void setVehicles(Set<Vehicle> vehicles) {
		this.vehicles = vehicles;
	}

	public Set<Rol> getRoles() {
		return roles;
	}

	public void setRoles(Set<Rol> roles) {
		this.roles = roles;
	}
}
