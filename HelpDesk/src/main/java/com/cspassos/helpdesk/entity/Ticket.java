package com.cspassos.helpdesk.entity;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.cspassos.helpdesk.enums.PriorityEnum;
import com.cspassos.helpdesk.enums.StatusEnum;

@Document
public class Ticket {

	@Id
	private String id;
	
	@DBRef(lazy = true) //faz uma referencia ao usuario
	private User user;
	
	private Date date;
	
	private String title;
	
	private Integer number;
	
	//Quando um tecnico aceitar esses usuario o tecnico vai ser atribuido a ele
	@DBRef(lazy = true)
	private User assingnedUser;

	private String description;
	
	private String image;
	
	private StatusEnum status;
	
	private PriorityEnum priority;
	
	@Transient
	private List<ChangeStatus> changes;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public User getAssingnedUser() {
		return assingnedUser;
	}

	public void setAssingnedUser(User assingnedUser) {
		this.assingnedUser = assingnedUser;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public StatusEnum getStatus() {
		return status;
	}

	public void setStatus(StatusEnum status) {
		this.status = status;
	}

	public PriorityEnum getPriority() {
		return priority;
	}

	public void setPriority(PriorityEnum priority) {
		this.priority = priority;
	}

	public List<ChangeStatus> getChanges() {
		return changes;
	}

	public void setChanges(List<ChangeStatus> changes) {
		this.changes = changes;
	}
	
}
