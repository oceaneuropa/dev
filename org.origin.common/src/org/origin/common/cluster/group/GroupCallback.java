package org.origin.common.cluster.group;

public interface GroupCallback {

	void joined(Group group);

	void left(Group group);

}
