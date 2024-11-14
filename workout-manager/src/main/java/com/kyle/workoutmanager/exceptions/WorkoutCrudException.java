package com.kyle.workoutmanager.exceptions;

import org.springframework.http.HttpStatus;

import lombok.Getter;

public class WorkoutCrudException extends RuntimeException {

    private static final long serialVersionUID = -5399827926223983510L;

    @Getter
    private final HttpStatus status;

    public WorkoutCrudException(String msg) {
	this(msg, null);
    }

    public WorkoutCrudException(String msg, HttpStatus status) {
	super(msg);
	this.status = status;
    }

}
