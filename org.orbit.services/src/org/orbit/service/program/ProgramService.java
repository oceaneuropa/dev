package org.orbit.service.program;

public interface ProgramService {

	// OSGi service property names
	public static final String PROP_PROGRAM_TYPE_ID = "programTypeId";
	public static final String PROP_PROGRAM_PROVIDER_ID = "programProviderId";
	public static ProgramProvider[] EMPTY_ARRAY = new ProgramProvider[] {};

	ProgramProvider[] getProgramProviders(String programTypeId);

	ProgramProvider getProgramProvider(String programTypeId, String programProviderId);

}
