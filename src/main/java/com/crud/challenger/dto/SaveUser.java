package com.crud.challenger.dto;

import java.util.List;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(of = "name")
public class SaveUser implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	@NotNull(message = "El nombre no puede ser nulo")
	@NotBlank(message = "El nombre no puede estar vacío")
	@Size(max =50, message = "El nombre no puede tener más de 50 caracteres")
	private String name;
	
	@NotNull(message = "El userName no puede ser nulo")
	@NotBlank(message = "El userName no puede estar vacío")
	@Size(max =12, message = "El nombre no puede tener más de 50 caracteres")
	private String userName;
	
	
	@Email(message = "El email no es válido", regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")
	@Size(max = 100, message = "El email no puede tener más de 100 caracteres")
	private String email;
	
	@NotNull(message = "El password no puede ser nulo")
	@NotBlank(message = "El password no puede estar vacío")
	@Size(min = 8, max = 12, message = "El password debe tener entre 8 y 12 caracteres")
	private String password;
	
	@NotNull(message = "El password no puede ser nulo")
	@NotBlank(message = "El password no puede estar vacío")
	@Size(min = 8, max = 12, message = "El password debe tener entre 8 y 12 caracteres")
	private String passwordConfirm;

	@NotNull(message = "El rol no puede ser nulo. Al menos debe enviar uno")
	@NotBlank(message = "El rol no puede estar vacío. Al menos debe enviar uno")
	private List<SavePhone> phones;
	
	private String jwtToken;

}
