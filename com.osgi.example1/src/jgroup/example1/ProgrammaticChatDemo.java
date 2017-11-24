package jgroup.example1;

import java.net.InetAddress;

import org.jgroups.Address;
import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;
import org.jgroups.View;
import org.jgroups.protocols.BARRIER;
import org.jgroups.protocols.FD_ALL;
import org.jgroups.protocols.FD_SOCK;
import org.jgroups.protocols.FRAG2;
import org.jgroups.protocols.MERGE3;
import org.jgroups.protocols.MFC;
import org.jgroups.protocols.PING;
import org.jgroups.protocols.UDP;
import org.jgroups.protocols.UFC;
import org.jgroups.protocols.UNICAST2;
import org.jgroups.protocols.VERIFY_SUSPECT;
import org.jgroups.protocols.pbcast.GMS;
import org.jgroups.protocols.pbcast.NAKACK;
import org.jgroups.protocols.pbcast.STABLE;
import org.jgroups.stack.ProtocolStack;
import org.jgroups.util.Util;

@SuppressWarnings("deprecation")
public class ProgrammaticChatDemo {

	@SuppressWarnings("resource")
	public static void main(String[] args) throws Exception {
		// JChannel is created (1). The false argument tells the channel not to create a ProtocolStack.
		JChannel channel = new JChannel(false); // (1)

		// This is needed because we will create one ourselves later and set it in the channel (2).
		ProtocolStack stack = new ProtocolStack(); // (2)
		channel.setProtocolStack(stack);

		// Next, all protocols are added to the stack (3).
		stack.addProtocol(new UDP().setValue("bind_addr", InetAddress.getByName("192.168.1.5"))) //
				.addProtocol(new PING()).addProtocol(new MERGE3()) //
				.addProtocol(new FD_SOCK()) //
				.addProtocol(new FD_ALL().setValue("timeout", 12000).setValue("interval", 3000)) //
				.addProtocol(new VERIFY_SUSPECT()) //
				.addProtocol(new BARRIER()) //
				.addProtocol(new NAKACK()) //
				.addProtocol(new UNICAST2()) //
				.addProtocol(new STABLE()) //
				.addProtocol(new GMS()) //
				.addProtocol(new UFC()) //
				.addProtocol(new MFC()) //
				.addProtocol(new FRAG2()); // (3)

		// Once the stack is configured, we call ProtocolStack.init() to link all protocols correctly and to call init() in every protocol instance (4).
		stack.init(); // (4)

		channel.setReceiver(new ReceiverAdapter() {
			public void viewAccepted(View new_view) {
				System.out.println("view: " + new_view);
			}

			public void receive(Message msg) {
				Address sender = msg.getSrc();
				System.out.println(msg.getObject() + " [" + sender + "]");
			}
		});

		channel.connect("ChatCluster");

		for (;;) {
			String line = Util.readStringFromStdin(": ");
			channel.send(null, line);
		}
	}

}
