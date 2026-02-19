package com.autolog.dto;

import jakarta.validation.constraints.*;

public class ProfileUpdateDTO {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 50)
    private String username;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Debe introducir un email válido")
    @Size(max = 100)
    private String email;
    
    public ProfileUpdateDTO(String username, String email) {
        this.username = username;
        this.email = email;
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

}
