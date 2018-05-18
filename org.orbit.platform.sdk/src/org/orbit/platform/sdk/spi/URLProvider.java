package org.orbit.platform.sdk.spi;

public interface URLProvider {

	public static final String EXTENSION_TYPE_ID = "platform.url.provider";

	String getURL();

}
