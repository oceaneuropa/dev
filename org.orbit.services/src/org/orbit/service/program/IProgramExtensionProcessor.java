package org.orbit.service.program;

public interface IProgramExtensionProcessor {

	String getTypeId();

	IProgramExtension[] getProgramExtensions();

	IProgramExtension getProgramExtension(String extensionId);

}
