package org.orbit.service.program;

public interface ProgramTypeProcessor {

	String getProgramTypeId();

	ProgramProvider[] getProgramProviders();

	ProgramProvider getProgramProvider(String programProviderId);

}
