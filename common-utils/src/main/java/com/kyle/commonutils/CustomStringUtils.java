package com.kyle.commonutils;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.RandomStringGenerator;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CustomStringUtils {

    private static final char[][] UUID_CHARS = new char[][] { { 'A', 'Z' }, { '0', '9' } };
    private static final RandomStringGenerator builder = new RandomStringGenerator.Builder().withinRange(UUID_CHARS)
	    .build();

    public static String hashValueIfNeeded(String value) {
	if (StringUtils.isBlank(value) || value.matches("\\w{64}")) {
	    return value;
	}
	return DigestUtils.sha256Hex(value);
    }

    public static String generateUuid(int length) {
	return builder.generate(length);
    }

}
