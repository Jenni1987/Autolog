package com.autolog.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "DOCUMENT_AUTOLOG")
public class Document {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "IDDOCUMENT_DOA")
	private Integer id;

	@NotBlank(message = "El nombre del archivo es obligatorio")
	@Column(name = "FILENAME_DOA", nullable = false)
	private String filename;

	@NotBlank(message = "La ruta del archivo es obligatoria")
	@Column(name = "FILEPATH_DOA", nullable = false)
	private String filepath;

	@Column(name = "TYPE_DOA")
	private String type;

	@ManyToOne
	@JoinColumn(name = "IDOPERATION_DOA", nullable = false)
	private Operation operation;

	// Getters y Setters
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getFilepath() {
		return filepath;
	}

	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Operation getOperation() {
		return operation;
	}

	public void setOperation(Operation operation) {
		this.operation = operation;
	}
}
