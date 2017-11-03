package org.origin.common.cluster;

public interface EventContext {

	public static final String PROP_CLUSTER_NAME = "cluster_name";
	public static final String PROP_SOURCE = "source";

	Object get(String propName);

}
