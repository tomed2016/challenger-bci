/**
 * 
 */
package com.crud.challenger.persistence.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

/**
 * 
 */
@Data
@Entity
@Table(name = "users")
@NamedQuery(name = "User.findByEmail", query = "SELECT u FROM User u WHERE u.email = :email")
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

	@Column(name = "email", unique = true, nullable = false, length=100)
	private String email;
	
	@Column(name = "password", nullable = false, length = 60)
	private String password;
	
	@OneToMany(mappedBy = "user", cascade= CascadeType.ALL, fetch = FetchType.LAZY)
	private Phone[] phones;
	
	@Column(name = "jwt_token", length = 500)
	private String jwtToken;

	
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
	
	public Boolean isActive() {
		return this.active == UserStatus.ACTIVE;
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