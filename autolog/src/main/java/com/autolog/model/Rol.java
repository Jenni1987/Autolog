package com.autolog.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "ROL_AUTOLOG")
public class Rol {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "IDROL_ROA")
	private Integer id;

	@Column(name = "ROLCODE_ROA", nullable = false, unique = true)
	private String code;

	@Column(name = "ROLNAME_ROA", nullable = false)
	private String name;
	
	@Column(name = "S_ACTIVEROW_ROA")
	private Boolean activeRow = true;

	@ManyToMany(mappedBy = "roles")
	private Set<User> users = new HashSet<>();

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	public Boolean getActiveRow() {
		return activeRow;
	}

	public void setActiveRow(Boolean activeRow) {
		this.activeRow = activeRow;
	}
	
}