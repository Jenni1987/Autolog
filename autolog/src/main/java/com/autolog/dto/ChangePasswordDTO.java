package com.autolog.dto;

import jakarta.validation.constraints.*;

public class ChangePasswordDTO {

    @NotBlank(message = "Debe introducir la contraseña actual")
    private String oldPassword;

    @NotBlank(message = "Debe introducir una nueva contraseña")
    @Pattern(
        regexp = "^(?=.*[A-Z])(?=.*\\d).{8,}$",
        message = "Mín 8 caracteres, 1 número y 1 mayúscula"
    )
    private String newPassword;

    @NotBlank(message = "Debe confirmar la nueva contraseña")
    private String confirmPassword;

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

}
