package com.kyle.user.utils;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class ResponseHelperTest {

    @Test
    void testGetResponseEntityOK() throws Exception {
	// given
	// when
	ResponseEntity<String> actual = ResponseHelper.getResponseEntityOK("TEST");
	// then
	assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.OK);
	assertThat(actual.getBody()).isEqualTo("TEST");
    }

    @Test
    void testGetResponseEntityWithError() throws Exception {
	// given
	HttpStatus status = HttpStatus.NOT_FOUND;
	// when
	ResponseEntity<Object> actual = ResponseHelper.getResponseEntityWithError(status);
	// then
	assertThat(actual.getStatusCode()).isEqualTo(status);
	assertThat(actual.getBody()).isNull();
    }

    @Test
    void testGetResponseEntityWithError_multipleStatus() throws Exception {
	// given
	HttpStatus status = null;
	HttpStatus status2 = HttpStatus.NOT_FOUND;
	// when
	ResponseEntity<Object> actual = ResponseHelper.getResponseEntityWithError(status, status2);
	// then
	assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	assertThat(actual.getBody()).isNull();
    }

    @Test
    void testGetResponseEntityWithError_defaultStatus() throws Exception {
	// given
	// when
	ResponseEntity<Object> actual = ResponseHelper.getResponseEntityWithError();
	// then
	assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
	assertThat(actual.getBody()).isNull();
    }

    @Test
    void testGetResponseEntityWithError_withReturnedObject() throws Exception {
	// given
	HttpStatus status = HttpStatus.NOT_FOUND;
	// when
	ResponseEntity<Object> actual = ResponseHelper.getResponseEntityWithError(new Object(), status);
	// then
	assertThat(actual.getStatusCode()).isEqualTo(status);
	assertThat(actual.getBody()).isNotNull();
    }
}
