/**
 * 
 */
package com.crud.challenger.dto;

import java.io.Serializable;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 
 */
@Data
@EqualsAndHashCode(of = {"cityCode", "countryCode", "number"})
public class PhoneDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@NotBlank(message = "El número de teléfono no puede estar vacío")
	@Size(max = 20, message = "El número de teléfono no puede tener más de 20 caracteres")
	private String number;
	
	@NotBlank(message = "El código de ciudad no puede estar vacío")
	@Size(max = 3, message = "El código de ciudad no puede tener más de 3 caracteres")
	private String cityCode;
	
	@NotBlank(message = "El código de país no puede estar vacío")
	@Size(max = 3, message = "El código de país no puede tener más de 3 caracteres")
	private String countryCode;
	

}
