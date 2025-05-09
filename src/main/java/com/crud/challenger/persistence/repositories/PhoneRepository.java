/**
 * 
 */
package com.crud.challenger.persistence.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.crud.challenger.persistence.entities.Phone;

/**
 * 
 * @author Tomás González
 * @date 2025-05-08
 * @apiNote Repositorio para la gestión de teléfonos. 
 */
@Repository
public interface PhoneRepository extends JpaRepository<Phone, UUID> {
	

}
