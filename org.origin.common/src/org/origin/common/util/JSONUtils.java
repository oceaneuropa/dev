package org.origin.common.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;

public final class JSONUtils {

	protected static final ObjectMapper JSON_MAPPER = new ObjectMapper();
	protected static final ObjectReader JSON_READER;
	protected static final ObjectWriter JSON_WRITER;

	static {
		JSON_MAPPER.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		JSON_MAPPER.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		JSON_MAPPER.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
		JSON_MAPPER.enable(SerializationFeature.INDENT_OUTPUT);
		JSON_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		JSON_READER = JSON_MAPPER.reader();
		JSON_WRITER = JSON_MAPPER.writer();
	}

	private JSONUtils() {
	}

	/**
	 * A Jackson {@code ObjectMapper}.
	 *
	 * @return JSON object mapper
	 */
	public static ObjectMapper getJsonMapper() {
		return JSON_MAPPER;
	}

	/**
	 * A Jackson JSON reader.
	 *
	 * @param type
	 *            the type to read
	 * @return JSON object reader
	 */
	public static ObjectReader getJsonReader(Class<?> type) {
		return JSON_READER.forType(type);
	}

	/**
	 * A Jackson JSON writer. Pretty print is on.
	 *
	 * @return JSON object writer
	 */
	public static ObjectWriter getJsonWriter() {
		return JSON_WRITER;
	}

	/**
	 * A Jackson JSON writer. Pretty print is off.
	 *
	 * @return JSON object writer with indentation disabled
	 */
	public static ObjectWriter getJsonWriterNoIdent() {
		return JSON_WRITER.without(SerializationFeature.INDENT_OUTPUT);
	}

}
