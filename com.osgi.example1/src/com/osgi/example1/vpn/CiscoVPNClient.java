package com.osgi.example1.vpn;

/**
 * @see http://stackoverflow.com/questions/22855195/vpn-connect-using-java
 *
 */
public class CiscoVPNClient {

	private static final String COMMAND = "C:/Program Files/Cisco/Cisco AnyConnect Secure Mobility Client/vpncli";

	// private ExpectJ exp = new ExpectJ(10);

	public void connectToVPNViaCLI(String server, String uname, String pwd) {
		try {
			String command = COMMAND + " connect " + server;
			// Spawn sp = exp.spawn(command);
			// sp.expect("Username: ");
			// sp.send(uname + "\n");
			// sp.expect("Password: ");
			// sp.send(pwd + "\n");
			// sp.expect("accept? [y/n]: ");
			// sp.send("y" + "\n");
		} catch (Exception e) {
			// LOGGER.severe(e.getMessage());
			e.printStackTrace();
		}
	}

}
