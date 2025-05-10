/**
 * 
 */
package com.crud.challenger.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 */
@Getter
@Setter
public class RegisteredUser implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private UUID userUuid;
	private String name;
	private String userName;
	private String jwtToken;
	private String email;
	private Date createdAt;
	private Date updatedAt;
	private Date lastLogin;
	private Boolean isActive;

}
