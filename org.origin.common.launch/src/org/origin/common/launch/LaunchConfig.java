package org.origin.common.launch;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface LaunchConfig {

	public static String LAUNCHER_ID = "launcherId";

	// -------------------------------------------------
	// Access
	// -------------------------------------------------
	LaunchService getLaunchService();

	String getName();

	File getFile();

	boolean exists();

	void load() throws IOException;

	void save() throws IOException;

	boolean delete() throws IOException;

	LaunchConfig getCopy(String name);

	// -------------------------------------------------
	// Launch
	// -------------------------------------------------
	LaunchInstance launch() throws IOException;

	// -------------------------------------------------
	// Configuration data
	// -------------------------------------------------
	String getTypeId();

	boolean getAttribute(String attributeName, boolean defaultValue) throws IOException;

	int getAttribute(String attributeName, int defaultValue) throws IOException;

	List<String> getAttribute(String attributeName, List<String> defaultValue) throws IOException;

	Set<String> getAttribute(String attributeName, Set<String> defaultValue) throws IOException;

	Map<String, String> getAttribute(String attributeName, Map<String, String> defaultValue) throws IOException;

	String getAttribute(String attributeName, String defaultValue) throws IOException;

	Map<String, Object> getAttributes() throws IOException;

	void setAttribute(String attributeName, int value);

	void setAttribute(String attributeName, String value);

	void setAttribute(String attributeName, List<String> value);

	void setAttribute(String attributeName, Map<String, String> value);

	void setAttribute(String attributeName, Set<String> value);

	void setAttribute(String attributeName, boolean value);

}
