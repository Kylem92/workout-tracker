package com.kyle.commonutils;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class CustomStringUtilsTest {

    @Test
    void testHashValueIfNeeded_null() {
	// given
	String value = null;
	// when
	String actual = CustomStringUtils.hashValueIfNeeded(value);
	// then
	assertThat(actual).isBlank();
    }

    @Test
    void testHashValueIfNeeded_blank() {
	// given
	String value = "";
	// when
	String actual = CustomStringUtils.hashValueIfNeeded(value);
	// then
	assertThat(actual).isBlank();
    }

    @Test
    void testHashValueIfNeeded_unhashed() {
	// given
	String value = "dev@arvoia.com";
	// when
	String actual = CustomStringUtils.hashValueIfNeeded(value);
	// then
	assertThat(actual).isEqualTo("618f42138ef241e50ae50f9f8d6b9a68f84921cd5203148d9e200503b8935776");
    }

    @Test
    void testHashValueIfNeeded_hashed() {
	// given
	String value = "618f42138ef241e50ae50f9f8d6b9a68f84921cd5203148d9e200503b8935776";
	// when
	String actual = CustomStringUtils.hashValueIfNeeded(value);
	// then
	assertThat(actual).isEqualTo("618f42138ef241e50ae50f9f8d6b9a68f84921cd5203148d9e200503b8935776");
    }

    @Test
    void testRandomUuidChars() {
	// given
	// when
	String actual = CustomStringUtils.generateUuid(12);
	// then
	assertThat(actual).hasSize(12);
    }

}
