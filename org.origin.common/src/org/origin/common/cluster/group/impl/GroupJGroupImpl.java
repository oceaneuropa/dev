package org.origin.common.cluster.group.impl;

import java.io.DataInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.jgroups.Address;
import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;
import org.jgroups.View;
import org.jgroups.ViewId;
import org.jgroups.util.Util;
import org.origin.common.cluster.group.Group;
import org.origin.common.cluster.group.GroupMember;

public class GroupJGroupImpl implements Group {

	protected String name;
	protected JChannel channel;
	protected List<GroupMember> members = new ArrayList<GroupMember>();

	/**
	 * 
	 * @param name
	 */
	public GroupJGroupImpl(String name) {
		this.name = name;
	}

	public void connect() throws Exception {
		if (this.channel != null) {
			if (this.channel.isConnected()) {
				System.out.println("Group '" + this.name + "' is connected.");
				return;
			} else if (this.channel.isConnecting()) {
				System.out.println("Group '" + this.name + "' is connecting.");
				return;
			}
		}

		this.channel = new JChannel();
		this.channel.setReceiver(new ReceiverAdapter() {
			@Override
			public void viewAccepted(View new_view) {
				System.out.println(getLabel() + ".ChannelReceiver.viewAccepted(View new_view)");
				System.out.println("\tview = " + new_view);

				// group member changed
				Address creator = new_view.getCreator();
				Address coord = new_view.getCoord();
				ViewId viewId = new_view.getViewId();
				System.out.println("\tcreator = " + creator);
				System.out.println("\tcoord = " + coord);
				System.out.println("\tviewId = " + viewId);
			}

			@Override
			public void receive(Message msg) {
				System.out.println(getLabel() + ".ChannelReceiver.receive(Message msg)");
				System.out.println("\tmsg = " + msg);

				Address srcAddress = msg.getSrc();
				Address destAddress = msg.getDest();

				String srcAddressStr = (srcAddress != null) ? srcAddress.toString() : null;
				String destAddressStr = (destAddress != null) ? destAddress.toString() : null;

				String line = "(" + srcAddressStr + " -> " + destAddressStr + ") " + msg.getObject();
				System.out.println(line);
				// synchronized (state) {
				// state.add(line);
				// }
			}

			@Override
			public void getState(OutputStream output) throws Exception {
				System.out.println("getLabel(). ChannelReceiver.getState(OutputStream output)");

				// synchronized (state) {
				// Util.objectToStream(state, new DataOutputStream(output));
				// }
			}

			@Override
			@SuppressWarnings("unchecked")
			public void setState(InputStream input) throws Exception {
				System.out.println("getLabel(). ChannelReceiver.setState(InputStream input)");

				List<String> list = (List<String>) Util.objectFromStream(new DataInputStream(input));
				// synchronized (state) {
				// state.clear();
				// state.addAll(list);
				// }
				System.out.println("received state (" + list.size() + " messages in chat history):");
				for (String str : list) {
					System.out.println(str);
				}
			}
		});
		this.channel.connect(this.name);
		this.channel.getState(null, 10000);
	}

	public void close() {
		if (this.channel != null) {
			this.channel.close();
			this.channel = null;
		}
	}

	@Override
	public String getName() {
		return this.name;
	}

	protected String getLabel() {
		return "Group '" + getName() + "'";
	}

	@Override
	public GroupMember[] getMembers() {
		return this.members.toArray(new GroupMember[this.members.size()]);
	}

	@Override
	public void addMember(GroupMember member) {

	}

	@Override
	public void removeMember(GroupMember member) {

	}

}
