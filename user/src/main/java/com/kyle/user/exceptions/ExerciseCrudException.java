package com.kyle.user.exceptions;

import org.springframework.http.HttpStatus;

import lombok.Getter;

public class ExerciseCrudException extends RuntimeException {

	private static final long serialVersionUID = -5399827926223983510L;
	
	@Getter
	private final HttpStatus status;

	public ExerciseCrudException(String msg) {
		    this(msg, null);
		  }

	public ExerciseCrudException(String msg, HttpStatus status) {
		super(msg);
		this.status = status;
	}

}
