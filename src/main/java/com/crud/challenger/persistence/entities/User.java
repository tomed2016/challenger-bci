/**
 * 
 */
package com.crud.challenger.persistence.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
public class User implements UserDetails {
	  
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.UUID)
	@Column(name = "user_uuid", updatable = false, nullable = false)
	private UUID userUuid;
	
	@Column(name = "name", nullable = false, length = 50)
	private String name;

	@Column(name = "username", unique=true, nullable = false, length = 12)
	private String userName;

	@NotNull
	@Column(name = "email", unique = true, nullable = false, length=100)
	private String email;
	
	@Column(name = "password", nullable = false, length = 60)
	private String password;
	
	@OneToMany(mappedBy = "user")
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

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return new ArrayList<GrantedAuthority>();
	}

	@Override
	public String getUsername() {
		return this.getName();
	}

}