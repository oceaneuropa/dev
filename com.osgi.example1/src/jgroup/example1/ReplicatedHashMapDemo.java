package jgroup.example1;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.List;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.jgroups.Address;
import org.jgroups.JChannel;
import org.jgroups.View;
import org.jgroups.blocks.ReplicatedHashMap;

/**
 * Uses the ReplicatedHashMap building block, which subclasses java.util.HashMap and overrides the methods that modify the hashmap (e.g. put()). Those methods
 * are multicast to the group, whereas read-only methods such as get() use the local copy. A ReplicatedtHashMap is created given the name of a group; all
 * hashmaps with the same name find each other and form a group.
 * 
 * @see ReplicatedHashMapDemo
 * 
 */
public class ReplicatedHashMapDemo extends Frame {

	private static final long serialVersionUID = -7721336490581395988L;

	protected ReplicatedHashMap<String, Float> map = null;
	protected JButton get = new JButton("Get");
	protected JButton set = new JButton("Set");
	protected JButton quit = new JButton("Quit");
	protected JButton get_all = new JButton("All");
	protected JButton delete = new JButton("Delete");
	protected JLabel stock = new JLabel("Key");
	protected JLabel value = new JLabel("Value");
	protected JLabel err_msg = new JLabel("Error");
	protected JTextField stock_field = new JTextField();
	protected JTextField value_field = new JTextField();
	protected List listbox = new List();
	protected Font default_font = new Font("Helvetica", Font.PLAIN, 12);

	public ReplicatedHashMapDemo() {
		super();
		WindowListener windowListener = new WindowListener() {
			@Override
			public void windowActivated(WindowEvent e) {
			}

			@Override
			public void windowClosed(WindowEvent e) {
			}

			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}

			@Override
			public void windowDeactivated(WindowEvent e) {
			}

			@Override
			public void windowDeiconified(WindowEvent e) {
			}

			@Override
			public void windowIconified(WindowEvent e) {
			}

			@Override
			public void windowOpened(WindowEvent e) {
			}
		};
		// addWindowListener(this);
		addWindowListener(windowListener);
	}

	private void showMsg(String msg) {
		err_msg.setText(msg);
		err_msg.setVisible(true);
	}

	private void clearMsg() {
		err_msg.setVisible(false);
	}

	private void removeItem() {
		int index = listbox.getSelectedIndex();
		if (index == -1) {
			showMsg("No item selected in listbox to be deleted !");
			return;
		}
		String s = listbox.getSelectedItem();
		String key = s.substring(0, s.indexOf(':', 0));
		if (key != null)
			map.remove(key);
	}

	private void showAll() {
		if (listbox.getItemCount() > 0)
			listbox.removeAll();
		if (map.isEmpty())
			return;
		clearMsg();
		String key;
		Float val;

		for (Map.Entry<String, Float> entry : map.entrySet()) {
			key = entry.getKey();
			val = entry.getValue();
			if (val == null)
				continue;
			listbox.add(key + ": " + val.toString());
		}
	}

	/**
	 * 
	 * @param channel
	 * @throws Exception
	 */
	public void start(JChannel channel) throws Exception {
		ReplicatedHashMap.Notification<Object, Object> listener = new ReplicatedHashMap.Notification<Object, Object>() {
			@Override
			public void entrySet(Object key, Object value) {
				showAll();
			}

			@Override
			public void entryRemoved(Object key) {
				showAll();
			}

			@Override
			public void contentsSet(Map<Object, Object> m) {
				System.out.println("new contents: " + m);
			}

			@Override
			public void contentsCleared() {
				System.out.println("contents cleared");
			}

			@Override
			public void viewChange(View view, java.util.List<Address> new_mbrs, java.util.List<Address> old_mbrs) {
				System.out.println("** view: " + view);
				_setTitle();
			}
		};

		map = new ReplicatedHashMap<>(channel);
		// map.addNotifier(this);
		map.addNotifier(listener);
		map.start(10000);

		setLayout(null);
		setSize(400, 300);
		setFont(default_font);

		stock.setBounds(new Rectangle(10, 30, 60, 30));
		value.setBounds(new Rectangle(10, 60, 60, 30));
		stock_field.setBounds(new Rectangle(100, 30, 100, 30));
		value_field.setBounds(new Rectangle(100, 60, 100, 30));
		listbox.setBounds(new Rectangle(210, 30, 150, 160));
		err_msg.setBounds(new Rectangle(10, 200, 350, 30));
		err_msg.setFont(new Font("Helvetica", Font.ITALIC, 12));
		err_msg.setForeground(Color.red);
		err_msg.setVisible(false);
		get.setBounds(new Rectangle(10, 250, 60, 30));
		set.setBounds(new Rectangle(80, 250, 60, 30));
		quit.setBounds(new Rectangle(150, 250, 60, 30));
		get_all.setBounds(new Rectangle(220, 250, 60, 30));
		delete.setBounds(new Rectangle(290, 250, 80, 30));

		ActionListener actionListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				doActionPerformed(e);
			}
		};
		// get.addActionListener(this);
		// set.addActionListener(this);
		// quit.addActionListener(this);
		// get_all.addActionListener(this);
		// delete.addActionListener(this);
		get.addActionListener(actionListener);
		set.addActionListener(actionListener);
		quit.addActionListener(actionListener);
		get_all.addActionListener(actionListener);
		delete.addActionListener(actionListener);

		add(stock);
		add(value);
		add(stock_field);
		add(value_field);
		add(err_msg);
		add(get);
		add(set);
		add(quit);
		add(get_all);
		add(delete);
		add(listbox);
		_setTitle();
		showAll();
		setVisible(true);
	}

	private void _setTitle() {
		int num = map.getChannel().getView().size();
		setTitle("ReplicatedHashMapDemo: " + num + " server(s)");
	}

	/**
	 * 
	 * @param e
	 */
	public void doActionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		try {
			switch (command) {
			case "Get": {
				String stock_name = stock_field.getText();
				if (stock_name == null || stock_name.isEmpty()) {
					showMsg("Key is empty !");
					return;
				}
				showMsg("Looking up value for " + stock_name + ':');
				Float val = map.get(stock_name);
				if (val != null) {
					value_field.setText(val.toString());
					clearMsg();
				} else {
					value_field.setText("");
					showMsg("Value for " + stock_name + " not found");
				}
				break;
			}
			case "Set":
				String stock_name = stock_field.getText();
				String stock_val = value_field.getText();
				if (stock_name == null || stock_val == null || stock_name.isEmpty() || stock_val.isEmpty()) {
					showMsg("Both key and value have to be present to create a new entry");
					return;
				}
				Float val = new Float(stock_val);
				map.put(stock_name, val);
				showMsg("Key " + stock_name + " set to " + val);
				break;
			case "All":
				showAll();
				break;
			case "Quit":
				setVisible(false);
				System.exit(0);
			case "Delete":
				removeItem();
				break;
			default:
				System.out.println("Unknown action");
				break;
			}
		} catch (Exception ex) {
			value_field.setText("");
			showMsg(ex.toString());
		}
	}

	public static void main(String args[]) {
		String cluster_name = System.getProperty("cluster.name", "n/a");
		String user_name = System.getProperty("user.name", "n/a");
		System.out.println("cluster_name = " + cluster_name);
		System.out.println("user_name = " + user_name);

		try {
			JChannel channel = new JChannel();
			channel.setName(user_name);
			channel.connect(cluster_name);

			ReplicatedHashMapDemo client = new ReplicatedHashMapDemo();
			client.start(channel);

		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

}

// static void help() {
// System.out.println("ReplicatedHashMapDemo [-help] [-props <properties>]");
// }

// public static void main(String args[]) {
// ReplicatedHashMapDemo client = new ReplicatedHashMapDemo();
// JChannel channel;
// String props = "udp.xml";
//
// try {
// for (int i = 0; i < args.length; i++) {
// String arg = args[i];
// if ("-props".equals(arg)) {
// props = args[++i];
// continue;
// }
// help();
// return;
// }
// } catch (Exception e) {
// help();
// return;
// }
// try {
// channel = new JChannel(props);
// channel.connect("ReplicatedHashMapDemo-Cluster");
// client.start(channel);
// } catch (Throwable t) {
// t.printStackTrace();
// }
// }
