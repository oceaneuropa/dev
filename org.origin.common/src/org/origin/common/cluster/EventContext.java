package org.origin.common.cluster;

public interface EventContext {

	public static final String PROP_CLUSTER_NAME = "cluster_name";

	Object get(String propName);

}
