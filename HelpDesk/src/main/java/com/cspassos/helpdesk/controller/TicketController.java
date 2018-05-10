package com.cspassos.helpdesk.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cspassos.helpdesk.entity.ChangeStatus;
import com.cspassos.helpdesk.entity.Ticket;
import com.cspassos.helpdesk.entity.User;
import com.cspassos.helpdesk.enums.StatusEnum;
import com.cspassos.helpdesk.response.Response;
import com.cspassos.helpdesk.security.jwt.JwtTokenUtil;
import com.cspassos.helpdesk.service.TicketService;
import com.cspassos.helpdesk.service.UserService;

@RestController
@RequestMapping("/api/ticket")
@CrossOrigin(origins = "*")
public class TicketController {

	@Autowired
	private TicketService ticketService;
	
	@Autowired
	protected JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	private UserService userService;
	
	//Incluir ticket
	@PostMapping()//Quem pode criar os tickets é a penas o CUSTOMER = cliente
	@PreAuthorize("hasAnyRole('CUSTOMER')")
	public ResponseEntity<Response<Ticket>> createOrUpdate(HttpServletRequest request, @RequestBody Ticket ticket, BindingResult result){
		Response<Ticket> response = new Response<Ticket>();
		
		try {
			
			validateCreateTicket(ticket, result);
			
			if(result.hasErrors()) {
				result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
				return ResponseEntity.badRequest().body(response); 
			}
			ticket.setStatus(StatusEnum.getStatus("New"));
			ticket.setUser(userFromRequest(request));
			ticket.setDate(new Date());
			ticket.setNumber(generateNumber());
			Ticket ticketPersisted = (Ticket) ticketService.createOrUpdate(ticket);
			response.setData(ticketPersisted);
			
		} catch (Exception e) {
			response.getErrors().add(e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
		
		return ResponseEntity.ok(response);
	}
	
	//validar ticket
	private void validateCreateTicket(Ticket ticket, BindingResult result) {
		if(ticket.getTitle() == null) {
			result.addError(new ObjectError("Ticket", "Title não informado"));
			return;
		}
	}
	
//	usuario da solicitação
	private User userFromRequest(HttpServletRequest request) {
		String token = request.getHeader("Authorization");
		String email = jwtTokenUtil.getUsernameFromToken(token);
		return userService.findByEmail(email);
	}
	
	//Gerar numero
	private Integer generateNumber() {
		Random random = new Random();
		return random.nextInt(9999);
	}
	
	//Anterar um ticket
	@PutMapping()//Quem pode criar os tickets é a penas o CUSTOMER = cliente
	@PreAuthorize("hasAnyRole('CUSTOMER')")
	public ResponseEntity<Response<Ticket>> update(HttpServletRequest request, @RequestBody Ticket ticket, BindingResult result){
		
		Response<Ticket> response = new Response<Ticket>();
		
		try {
		validateUpdateTicket(ticket, result);
		
		if(result.hasErrors()) {
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response); 
		}
			
		Ticket ticketCurrent = ticketService.findById(ticket.getId());
		ticket.setStatus(ticketCurrent.getStatus());
		ticket.setUser(ticketCurrent.getUser());
		ticket.setDate(ticketCurrent.getDate());
		ticket.setNumber(ticketCurrent.getNumber());
		
		if(ticketCurrent.getAssingnedUser() != null) {
			ticket.setAssingnedUser(ticketCurrent.getAssingnedUser());
		}
		
		Ticket ticketPersisted = (Ticket) ticketService.createOrUpdate(ticket);
		response.setData(ticketPersisted);
		
		} catch (Exception e) {
			response.getErrors().add(e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
		
		return ResponseEntity.ok(response);
	}

	private void validateUpdateTicket(Ticket ticket, BindingResult result) {
		if(ticket.getId() == null) {
			result.addError(new ObjectError("Ticket", "ID não informado"));
			return;
		}
		if(ticket.getTitle() == null) {
			result.addError(new ObjectError("Ticket", "Title não informado"));
			return;
		}
	}
	
	//pesquisa por id
	@GetMapping(value = "{id}")
	@PreAuthorize("hasAnyRole('CUSTOMER','TECHNICIAN')")
	public ResponseEntity<Response<Ticket>> findById(@PathVariable("id") String id){
		
		Response<Ticket> response = new Response<Ticket>();
		Ticket ticket = ticketService.findById(id);
		
		if(ticket == null) {
			response.getErrors().add("Registro esta nulo: +id");
			return ResponseEntity.badRequest().body(response);
		}
		
		List<ChangeStatus> changes = new ArrayList<ChangeStatus>();
		Iterable<ChangeStatus> changesCurrent = ticketService.listChangeStatus(ticket.getId());
		
		for (Iterator<ChangeStatus> iterator = changesCurrent.iterator(); iterator.hasNext();) {
			ChangeStatus changeStatus = (ChangeStatus) iterator.next();
			changeStatus.setTicket(null);
			changes.add(changeStatus);
		}
		
		ticket.setChanges(changes);
		response.setData(ticket);
		return ResponseEntity.ok(response);
	}
	
	
	@DeleteMapping(value = "{id}")
	@PreAuthorize("hasAnyRole('CUSTOMER')")
	public ResponseEntity<Response<String>> delete(@PathVariable("id") String id){
		
		Response<String> response = new Response<String>();
		Ticket ticket = ticketService.findById(id);
		
		if(ticket == null) {
			response.getErrors().add("Registro esta nulo: +id");
			return ResponseEntity.badRequest().body(response);
		}
		ticketService.delete(id);
		return ResponseEntity.ok(new Response<String>());
	}
}
