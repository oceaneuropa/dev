package org.orbit.platform.sdk.extension;

public interface IProgramExtensionProcessor {

	String getTypeId();

	IProgramExtension[] getExtensions();

	IProgramExtension getExtension(String extensionId);

}
