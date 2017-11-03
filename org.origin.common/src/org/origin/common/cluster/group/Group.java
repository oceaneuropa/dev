package org.origin.common.cluster.group;

public interface Group {

	public String getName();

	public GroupMember[] getMembers();

	public void addMember(GroupMember member);

	public void removeMember(GroupMember member);

}
