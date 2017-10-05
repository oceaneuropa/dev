package org.origin.common.group;

public interface Group {

	public String getName();

	public GroupMember[] getMembers();

	public void addMember(GroupMember member);

	public void removeMember(GroupMember member);

}
