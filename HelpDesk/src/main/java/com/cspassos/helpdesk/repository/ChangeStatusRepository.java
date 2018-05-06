package com.cspassos.helpdesk.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.cspassos.helpdesk.entity.ChangeStatus;

public interface ChangeStatusRepository extends MongoRepository<ChangeStatus, String>{

	//buscar o id dp ticket
	Iterable<ChangeStatus> findByTicketIdOrderByDateChangeStatusDesc(String ticketId);
}
