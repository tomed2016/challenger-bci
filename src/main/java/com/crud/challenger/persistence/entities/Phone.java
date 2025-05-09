/**
 * 
 */
package com.crud.challenger.persistence.entities;


import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 
 */
@Data
@Entity
@Table(name = "phones")
@EqualsAndHashCode(of = {"cityCode", "countryCode", "number"})	
public class Phone {
	@Id
	@GeneratedValue(strategy=GenerationType.UUID)
	@Column(name = "phone_uuid", updatable = false, nullable = false)
	private UUID phoneUuid;
	
	@Column(name = "phone_number", nullable = false, length = 20)
	private String number;
	
	@Column(name = "city_code", nullable = false, length = 3)
	private String cityCode;
	
	@Column(name = "country_code", nullable = false, length = 3)
	private String countryCode;
	
	@ManyToOne(optional = false)
    @JoinColumn(name="user_uuid", nullable=false, referencedColumnName="user_uuid")
	private User user;


}
