package com.cspassos.helpdesk.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.cspassos.helpdesk.entity.User;

public interface UserRepository extends MongoRepository<User, String>{

	User findByEmail(String email);

}
