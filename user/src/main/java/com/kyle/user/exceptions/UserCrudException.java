package com.kyle.user.exceptions;

import org.springframework.http.HttpStatus;

import lombok.Getter;

public class UserCrudException extends RuntimeException {

	private static final long serialVersionUID = -5399827926223983510L;
	
	@Getter
	private final HttpStatus status;

	public UserCrudException(String msg) {
		    this(msg, null);
		  }

	public UserCrudException(String msg, HttpStatus status) {
		super(msg);
		this.status = status;
	}

}
