package org.origin.common.service;

public interface CLIAware {

	public static interface Command {
		String getName();

		Parameter[] getParameters();
	}

	public static interface Parameter {
		String getName();
	}

	// OSGi service property names
	public static final String PROP_NAME = "name";
	public static CLIAware[] EMPTY_ARRAY = new CLIAware[] {};

	String getName();

	Command[] getCommands();

}
