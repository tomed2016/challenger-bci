/**
 * 
 */
package com.crud.challenger.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 */
@Getter
@Setter
public class RegisteredUser implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String name;
	private String userName;
	private String jwtToken;

}
