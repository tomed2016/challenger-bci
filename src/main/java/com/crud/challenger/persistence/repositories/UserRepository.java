package com.crud.challenger.persistence.repositories;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.crud.challenger.persistence.entities.User;

@Repository
public interface UserRepository extends CrudRepository<User, UUID> {

}
