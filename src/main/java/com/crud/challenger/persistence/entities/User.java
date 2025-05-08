/**
 * 
 */
package com.crud.challenger.persistence.entities;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 
 */
@Data
@Entity
@Table(name = "users")
public class User {
	  
	@Id
	@GeneratedValue(strategy=GenerationType.UUID)
	@Column(name = "uuid", updatable = false, nullable = false)
	private UUID uuid;
	
	@Column(name = "name", nullable = false, length = 50)
	private String name;
	
	@NotNull
	@Column(name = "email", unique = true, nullable = false, length=100)
	private String email;
	
	@Column(name = "password", nullable = false, length = 12)
	private String password;
	
	@JoinColumn(name = "phones_uuid", nullable = false)
	@OneToMany(cascade = CascadeType.ALL)
	private List<Phone> phones;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_at")
	private Date createdAt;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="updated_at")
	private Date updatedAt;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastLogin;
	
	@Enumerated(EnumType.STRING)
	private UserStatus active;
	
	public static enum UserStatus {
		ACTIVE,
		INACTIVE
	}

}