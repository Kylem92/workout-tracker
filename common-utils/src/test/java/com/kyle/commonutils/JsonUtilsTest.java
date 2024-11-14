package com.kyle.commonutils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import com.kyle.commonutils.testhelper.DummyClass;

class JsonUtilsTest {

    @Test
    void testObjectToJSON_JSONObject() {
	// given
	JSONObject obj = new JSONObject("{\"value\" : 1,\"text\" : \"something\"}");
	boolean formatOutput = false;
	// when
	String actual = JsonUtils.objectToJSON(obj, formatOutput);
	// then
	assertEquals("{\"text\":\"something\",\"value\":1}", actual);
    }

    @Test
    void testObjectToJSON_JSONObject_formatted() {
	// given
	JSONObject obj = new JSONObject("{\"value\" : 1,\"text\" : \"something\"}");
	boolean formatOutput = true;
	// when
	String actual = JsonUtils.objectToJSON(obj, formatOutput);
	// then
	assertEquals("{\n" + "  \"text\": \"something\",\n" + "  \"value\": 1\n" + "}", actual);
    }

    @Test
    void testObjectToJSON_JSONArray() {
	// given
	JSONArray obj = new JSONArray("[{\"value\" : 1,\"text\" : \"something\"}, {\"second\":\"item\"}]");
	boolean formatOutput = false;
	// when
	String actual = JsonUtils.objectToJSON(obj, formatOutput);
	// then
	assertEquals("[{\"text\":\"something\",\"value\":1},{\"second\":\"item\"}]", actual);
    }

    @Test
    void testObjectToJSON_JSONArray_formatted() {
	// given
	JSONArray obj = new JSONArray("[{\"value\" : 1,\"text\" : \"something\"}, {\"second\":\"item\"}]");
	boolean formatOutput = true;
	// when
	String actual = JsonUtils.objectToJSON(obj, formatOutput);
	// then
	assertEquals("[\n" + "  {\n" + "    \"text\": \"something\",\n" + "    \"value\": 1\n" + "  },\n" + "  {"
		+ "\"second\": \"item\"" + "}\n]", actual);
    }

    @Test
    void testObjectToJSON_notFormatted() {
	// given
	DummyClass obj = getDummyValue();
	boolean formatOutput = false;
	// when
	String actual = JsonUtils.objectToJSON(obj, formatOutput);
	// then
	assertEquals("{\"name\":\"test\",\"number\":1}", actual);
    }

    @Test
    void testObjectToJSON_formatted() {
	// given
	DummyClass obj = getDummyValue();
	boolean formatOutput = true;
	// when
	String actual = JsonUtils.objectToJSON(obj, formatOutput);
	// then
	assertEquals("{\n" + "  \"name\" : \"test\",\n" + "  \"number\" : 1\n" + "}", actual);
    }

    @Test
    void testObjectToJSON_null() {
	// given
	DummyClass obj = null;
	boolean formatOutput = false;
	// when
	String actual = JsonUtils.objectToJSON(obj, formatOutput);
	// then
	assertEquals("{}", actual);
    }

    @Test
    void testObjectToJSON_serializationProblem() {
	// given
	Object obj = new Object();
	boolean formatOutput = true;
	// when
	String actual = JsonUtils.objectToJSON(obj, formatOutput);
	// then
	assertEquals(
		"JSON serialization failuire: No serializer found for class java.lang.Object and no properties discovered to create BeanSerializer (to avoid exception, disable SerializationFeature.FAIL_ON_EMPTY_BEANS)",
		actual);
    }

    @Test
    void testObjectFromJSON_null() throws IOException {
	// given
	String json = null;
	Class<DummyClass> clazz = DummyClass.class;
	// when
	Object actual = JsonUtils.objectFromJSON(json, clazz);
	// then
	assertNull(actual);
    }

    @Test
    void testObjectFromJSON_() throws IOException {
	// given
	String json = "{\"name\":\"test\",\"number\":1}";
	Class<DummyClass> clazz = DummyClass.class;
	// when
	DummyClass actual = JsonUtils.objectFromJSON(json, clazz);
	// then
	assertNotNull(actual);
	assertEquals("test", actual.getName());
	assertEquals(1, actual.getNumber().intValue());
    }

    protected DummyClass getDummyValue() {
	return new DummyClass("test", 1);
    }
}
