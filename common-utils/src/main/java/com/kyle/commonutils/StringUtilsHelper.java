package com.kyle.commonutils;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.RandomStringGenerator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class StringUtilsHelper {

    private static final char[][] UUID_CHARS = new char[][] { { 'A', 'Z' }, { '0', '9' } };
    private static final RandomStringGenerator builder = new RandomStringGenerator.Builder().withinRange(UUID_CHARS)
	    .build();
    private static final int INDENT = 2;

    public static String hashValueIfNeeded(String value) {
	if (StringUtils.isBlank(value) || value.matches("\\w{64}")) {
	    return value;
	}
	return DigestUtils.sha256Hex(value);
    }

    public static String generateUuid(int length) {
	return builder.generate(length);
    }

    public static String objectToJSON(Object obj, boolean formatOutput) {
	if (obj == null) {
	    return "{}";
	}
	try {
	    if (obj instanceof JSONObject jsonObject) {
		return formatOutput ? jsonObject.toString(INDENT) : jsonObject.toString();
	    }
	    if (obj instanceof JSONArray jsonArray) {
		return formatOutput ? jsonArray.toString(INDENT) : jsonArray.toString();
	    }
	    ObjectMapper mapper = new ObjectMapper();
	    mapper.registerModule(new JavaTimeModule());
	    if (!formatOutput) {
		mapper.disable(SerializationFeature.INDENT_OUTPUT);
		return mapper.writeValueAsString(obj);
	    } else {
		mapper.enable(SerializationFeature.INDENT_OUTPUT);
		return mapper.writeValueAsString(obj).replace("\r\n", "\n");
	    }
	} catch (JSONException | JsonProcessingException e) {
	    return "JSON serialization failuire: " + e.getMessage();
	}
    }
}
