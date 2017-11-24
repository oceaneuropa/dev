package jgroup.example1;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;

import org.jgroups.Address;
import org.jgroups.Channel;
import org.jgroups.ChannelListener;
import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;
import org.jgroups.View;
import org.jgroups.util.Util;

/**
 * http://www.jgroups.org/tutorial/html/ch02.html
 * 
 * http://sourceforge.net/projects/javagroups/files/JGroups/
 *
 * -Djava.net.preferIPv4Stack=true -Duser.name=user11 -Dcluster.name=group1 -Djgroups.print_uuids=true
 * 
 * -Djava.net.preferIPv4Stack=true -Duser.name=user12 -Dcluster.name=group1 -Djgroups.print_uuids=true
 *
 * -Djava.net.preferIPv4Stack=true -Duser.name=user21 -Dcluster.name=group2 -Djgroups.print_uuids=true
 *
 * -Djava.net.preferIPv4Stack=true -Duser.name=user21 -Dcluster.name=group2 -Djgroups.print_uuids=true
 *
 *
 * http://jgroups.org/manual/
 *
 */
public class SimpleChatDemo {

	protected JChannel channel;
	protected String cluster_name = System.getProperty("cluster.name", "ChatCluster");
	protected String user_name = System.getProperty("user.name", "n/a");

	final List<String> state = new LinkedList<String>();

	public void start() throws Exception {
		this.channel = new JChannel();
		// this.channel.setName("Member[" + user_name + "]");
		// this.channel.setDiscardOwnMessages(true);

		this.channel.addChannelListener(new ChannelListener() {
			@Override
			public void channelClosed(Channel channel) {
				System.out.println();
				System.out.println("======================================================================================================");
				System.out.println(getClass().getName() + ".ChannelListener.channelClosed(Channel channel)");

				JGroupHelper.INSTANCE.print(channel, "\t\t");
			}

			@Override
			public void channelConnected(Channel channel) {
				System.out.println();
				System.out.println("======================================================================================================");
				System.out.println(getClass().getName() + ".ChannelListener.channelConnected(Channel channel)");

				JGroupHelper.INSTANCE.print(channel, "\t\t");
			}

			@Override
			public void channelDisconnected(Channel channel) {
				System.out.println();
				System.out.println("======================================================================================================");
				System.out.println(getClass().getName() + ".ChannelListener.channelDisconnected(Channel channel)");

				JGroupHelper.INSTANCE.print(channel, "\t\t");
			}
		});

		this.channel.setReceiver(new ReceiverAdapter() {
			@Override
			public void viewAccepted(View view) {
				System.out.println();
				System.out.println("======================================================================================================");
				System.out.println(getClass().getName() + ".ChannelReceiver.viewAccepted(View new_view)");

				JGroupHelper.INSTANCE.print(view, "\t\t");
				// JGroupHelper.INSTANCE.print(channel, "\t\t");
			}

			@Override
			public void receive(Message msg) {
				System.out.println();
				System.out.println("======================================================================================================");
				System.out.println(getClass().getName() + ".ChannelReceiver.receive(Message msg)");
				System.out.println("\tmsg = " + msg);

				Address srcAddress = msg.getSrc();
				Address destAddress = msg.getDest();

				String srcAddressStr = (srcAddress != null) ? srcAddress.toString() : null;
				String destAddressStr = (destAddress != null) ? destAddress.toString() : null;

				String line = "(" + srcAddressStr + " -> " + destAddressStr + ") " + msg.getObject();
				System.out.println(line);
				synchronized (state) {
					state.add(line);
				}
			}

			@Override
			public void getState(OutputStream output) throws Exception {
				System.out.println();
				System.out.println("======================================================================================================");
				System.out.println(getClass().getName() + ".ChannelReceiver.getState(OutputStream output)");

				synchronized (state) {
					Util.objectToStream(state, new DataOutputStream(output));
				}
			}

			@Override
			@SuppressWarnings("unchecked")
			public void setState(InputStream input) throws Exception {
				System.out.println();
				System.out.println("======================================================================================================");
				System.out.println(getClass().getName() + ".ChannelReceiver.setState(InputStream input)");

				List<String> list = (List<String>) Util.objectFromStream(new DataInputStream(input));
				synchronized (state) {
					state.clear();
					state.addAll(list);
				}
				System.out.println("received state (" + list.size() + " messages in chat history):");
				for (String str : list) {
					System.out.println(str);
				}
			}

			@Override
			public void block() {
				super.block();
			}

			@Override
			public void unblock() {
				super.unblock();
			}
		});

		// JGroupHelper.INSTANCE.print(this.channel, "BEFORE CONNECT ");
		this.channel.connect(cluster_name);
		this.channel.getState(null, 10000);
		// JGroupHelper.INSTANCE.print(this.channel, "AFTER CONNECT ");
		eventLoop();
		this.channel.close();
	}

	private void eventLoop() {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		while (true) {
			try {
				// System.out.print("> ");
				System.out.flush();
				String line = in.readLine().toLowerCase();
				if (line.startsWith("quit") || line.startsWith("exit")) {
					break;
				}
				line = "[" + user_name + "] " + line;

				Message msg = new Message(null, null, line);
				this.channel.send(msg);

			} catch (Exception e) {
			}
		}
	}

	public static void main(String[] args) throws Exception {
		SimpleChatDemo chat = new SimpleChatDemo();
		chat.start();
	}

}
