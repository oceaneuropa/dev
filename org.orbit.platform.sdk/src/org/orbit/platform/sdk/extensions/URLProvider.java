package org.orbit.platform.sdk.extensions;

public interface URLProvider {

	public static final String EXTENSION_TYPE_ID = "platform.url.provider";

	String getURL();

}
