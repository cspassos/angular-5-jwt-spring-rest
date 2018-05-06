package com.cspassos.helpdesk.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.cspassos.helpdesk.entity.Ticket;

public interface TicketRepository extends MongoRepository<Ticket, String>{
	
	//Page porque vai ser utilizado paginação
	//findByUserIdOrderByDateDesc encontrar o usuario ordenado pela data
	Page<Ticket> findByUserIdOrderByDateDesc(Pageable pages, String userId);
	
	Page<Ticket> findByTitleIgnoreCaseContainingAndStatusAndPriorityOrderByDateDesc(
			String title, String status, String priority, Pageable pages);
	
	Page<Ticket> findByTitleIgnoreCaseContainingAndStatusAndPriorityAndUserIdOrderByDateDesc(
			String title, String status, String priority, Pageable pages);
	
	Page<Ticket> findByTitleIgnoreCaseContainingAndStatusAndPriorityAndAssingnedUserIdOrderByDateDesc(
			String title, String status, String priority, Pageable pages);

	Page<Ticket> findByNumber(Integer number, Pageable pages);
}

