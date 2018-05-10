package com.cspassos.helpdesk.controller;

import java.util.Date;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}