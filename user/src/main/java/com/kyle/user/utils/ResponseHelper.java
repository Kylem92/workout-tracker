package com.kyle.user.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseHelper {

    private ResponseHelper() {
	// private constructor
    }

    public static <T> ResponseEntity<T> getResponseEntityOK(T body) {
	return new ResponseEntity<>(body, HttpStatus.OK);
    }

    public static <T> ResponseEntity<T> getResponseEntityWithError(HttpStatus... status) {
	return new ResponseEntity<>(firstStatus(status));
    }

    public static <T> ResponseEntity<T> getResponseEntityWithError(T body, HttpStatus... status) {
	return new ResponseEntity<>(body, firstStatus(status));
    }

    private static HttpStatus firstStatus(HttpStatus... status) {
	if (status != null) {
	    for (HttpStatus st : status) {
		if (st != null) {
		    return st;
		}
	    }
	}
	return HttpStatus.UNPROCESSABLE_ENTITY;
    }

}
