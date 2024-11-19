package com.kyle.commonutils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JsonUtils {

    private static final int INDENT = 2;

    private static final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

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

    public static <T> T objectFromJSON(String json, Class<T> clazz) throws IOException {
	return StringUtils.isBlank(json) ? null : mapper.readValue(json, clazz);
    }

    public static <T> List<T> objectListFromJSON(String json, Class<T> clazz) throws IOException, JSONException {
	List<T> ret = new ArrayList<>();
	if (StringUtils.isNotBlank(json)) {
	    JSONArray array = new JSONArray(json);
	    int len = array.length();
	    for (int idx = 0; idx < len; idx++) {
		ret.add(mapper.readValue(array.get(idx).toString(), clazz));
	    }
	}
	return ret;
    }
}
