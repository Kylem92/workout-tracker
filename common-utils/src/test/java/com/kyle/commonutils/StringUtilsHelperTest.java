package com.kyle.commonutils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import com.kyle.commonutils.testhelper.DummyClass;

class StringUtilsHelperTest {

    @Test
    void testHashValueIfNeeded_null() {
	// given
	String value = null;
	// when
	String actual = StringUtilsHelper.hashValueIfNeeded(value);
	// then
	assertThat(actual).isBlank();
    }

    @Test
    void testHashValueIfNeeded_blank() {
	// given
	String value = "";
	// when
	String actual = StringUtilsHelper.hashValueIfNeeded(value);
	// then
	assertThat(actual).isBlank();
    }

    @Test
    void testHashValueIfNeeded_unhashed() {
	// given
	String value = "dev@arvoia.com";
	// when
	String actual = StringUtilsHelper.hashValueIfNeeded(value);
	// then
	assertThat(actual).isEqualTo("618f42138ef241e50ae50f9f8d6b9a68f84921cd5203148d9e200503b8935776");
    }

    @Test
    void testHashValueIfNeeded_hashed() {
	// given
	String value = "618f42138ef241e50ae50f9f8d6b9a68f84921cd5203148d9e200503b8935776";
	// when
	String actual = StringUtilsHelper.hashValueIfNeeded(value);
	// then
	assertThat(actual).isEqualTo("618f42138ef241e50ae50f9f8d6b9a68f84921cd5203148d9e200503b8935776");
    }

    @Test
    void testRandomUuidChars() {
	// given
	// when
	String actual = StringUtilsHelper.generateUuid(12);
	// then
	assertThat(actual).hasSize(12);
    }

    @Test
    void testObjectToJSON_JSONObject() {
	// given
	JSONObject obj = new JSONObject("{\"value\" : 1,\"text\" : \"something\"}");
	boolean formatOutput = false;
	// when
	String actual = StringUtilsHelper.objectToJSON(obj, formatOutput);
	// then
	assertEquals("{\"text\":\"something\",\"value\":1}", actual);
    }

    @Test
    void testObjectToJSON_JSONObject_formatted() {
	// given
	JSONObject obj = new JSONObject("{\"value\" : 1,\"text\" : \"something\"}");
	boolean formatOutput = true;
	// when
	String actual = StringUtilsHelper.objectToJSON(obj, formatOutput);
	// then
	assertEquals("{\n" + "  \"text\": \"something\",\n" + "  \"value\": 1\n" + "}", actual);
    }

    @Test
    void testObjectToJSON_JSONArray() {
	// given
	JSONArray obj = new JSONArray("[{\"value\" : 1,\"text\" : \"something\"}, {\"second\":\"item\"}]");
	boolean formatOutput = false;
	// when
	String actual = StringUtilsHelper.objectToJSON(obj, formatOutput);
	// then
	assertEquals("[{\"text\":\"something\",\"value\":1},{\"second\":\"item\"}]", actual);
    }

    @Test
    void testObjectToJSON_JSONArray_formatted() {
	// given
	JSONArray obj = new JSONArray("[{\"value\" : 1,\"text\" : \"something\"}, {\"second\":\"item\"}]");
	boolean formatOutput = true;
	// when
	String actual = StringUtilsHelper.objectToJSON(obj, formatOutput);
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
	String actual = StringUtilsHelper.objectToJSON(obj, formatOutput);
	// then
	assertEquals("{\"name\":\"test\",\"number\":1}", actual);
    }

    @Test
    void testObjectToJSON_formatted() {
	// given
	DummyClass obj = getDummyValue();
	boolean formatOutput = true;
	// when
	String actual = StringUtilsHelper.objectToJSON(obj, formatOutput);
	// then
	assertEquals("{\n" + "  \"name\" : \"test\",\n" + "  \"number\" : 1\n" + "}", actual);
    }

    @Test
    void testObjectToJSON_null() {
	// given
	DummyClass obj = null;
	boolean formatOutput = false;
	// when
	String actual = StringUtilsHelper.objectToJSON(obj, formatOutput);
	// then
	assertEquals("{}", actual);
    }

    @Test
    void testObjectToJSON_serializationProblem() {
	// given
	Object obj = new Object();
	boolean formatOutput = true;
	// when
	String actual = StringUtilsHelper.objectToJSON(obj, formatOutput);
	// then
	assertEquals(
		"JSON serialization failuire: No serializer found for class java.lang.Object and no properties discovered to create BeanSerializer (to avoid exception, disable SerializationFeature.FAIL_ON_EMPTY_BEANS)",
		actual);
    }

    protected DummyClass getDummyValue() {
	return new DummyClass("test", 1);
    }
}
