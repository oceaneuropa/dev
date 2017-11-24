package jgroup.example1;

import java.util.List;

import org.jgroups.Address;
import org.jgroups.Channel;
import org.jgroups.JChannel;
import org.jgroups.View;
import org.jgroups.ViewId;

public class JGroupHelper {

	public static JGroupHelper INSTANCE = new JGroupHelper();

	/**
	 * 
	 * @param channel
	 * @param prefix
	 */
	public void print(Channel channel, String prefix) {
		if (channel == null) {
			System.out.println("channel is null.");
			return;
		}
		if (prefix == null) {
			prefix = "";
		}

		String name = channel.getName();
		String groupName = channel.getClusterName();
		Address address = channel.getAddress();
		// String properties = channel.getProperties();
		String state = channel.getState();
		View view = channel.getView();

		System.out.println();
		System.out.println(prefix + "------------------------------------------------------------------------------------------------------");
		System.out.println(prefix + "name = " + name);
		System.out.println(prefix + "groupName = " + groupName);
		System.out.println(prefix + "address = " + address + " [" + address.getClass().getName() + "]");
		// System.out.println(prefix + "properties = " + properties);
		System.out.println(prefix + "state = " + state);
		System.out.println(prefix + "view = " + view);
		System.out.println(prefix + "------------------------------------------------------------------------------------------------------");
		// System.out.println();
	}

	/**
	 * 
	 * @param channel
	 * @param prefix
	 */
	public void print(JChannel channel, String prefix) {
		if (channel == null) {
			System.out.println("channel is null.");
			return;
		}
		if (prefix == null) {
			prefix = "";
		}

		String name = channel.getName();
		// Method getAddress() returns the address of the channel.
		Address address = channel.getAddress();
		String addressStr = channel.getAddressAsString();
		String addressUUID = channel.getAddressAsUUID();
		// Method getClusterName() returns the name of the cluster which the member joined.
		String groupName = channel.getClusterName();
		// String properties = channel.getProperties();
		String state = channel.getState();
		int timerThreads = channel.getTimerThreads();
		// Get the current view of a channel
		View view = channel.getView();
		String viewStr = channel.getViewAsString();

		System.out.println();
		System.out.println(prefix + "------------------------------------------------------------------------------------------------------");
		System.out.println(prefix + "name = " + name);
		System.out.println(prefix + "groupName = " + groupName);
		System.out.println(prefix + "address = " + address + " [" + address.getClass().getName() + "]");
		System.out.println(prefix + "addressStr = " + addressStr);
		System.out.println(prefix + "addressUUID = " + addressUUID);
		// System.out.println(prefix + "properties = " + properties);
		System.out.println(prefix + "state = " + state);
		System.out.println(prefix + "timerThreads = " + timerThreads);
		System.out.println(prefix + "view = " + view);
		System.out.println(prefix + "viewStr = " + viewStr);
		System.out.println(prefix + "------------------------------------------------------------------------------------------------------");
		// System.out.println();
	}

	/**
	 * 
	 * @param view
	 * @param prefix
	 */
	public void print(View view, String prefix) {
		if (view == null) {
			System.out.println("view is null.");
			return;
		}
		if (prefix == null) {
			prefix = "";
		}

		Address creator = view.getCreator();
		Address coord = view.getCoord();
		ViewId viewId = view.getViewId();
		List<Address> members = view.getMembers();
		Address[] membersRaw = view.getMembersRaw();

		System.out.println();
		System.out.println(prefix + "------------------------------------------------------------------------------------------------------");
		System.out.println(prefix + "view = " + view);
		System.out.println(prefix + "view.getCreator() = " + creator);
		System.out.println(prefix + "view.getCoord() = " + coord);
		System.out.println(prefix + "view.getViewId() = " + viewId);

		System.out.println();
		System.out.println(prefix + "view.getMembers() =");
		for (Address member : members) {
			System.out.println(prefix + "\tmember (Address) = " + member + " [" + member.getClass().getName() + "]");
		}

		System.out.println();
		System.out.println(prefix + "view.getMembersRaw() =");
		for (Address memberRaw : membersRaw) {
			System.out.println(prefix + "\tmemberRaw (Address) = " + memberRaw + " [" + memberRaw.getClass().getName() + "]");
		}

		System.out.println(prefix + "------------------------------------------------------------------------------------------------------");
		// System.out.println();
	}

}
