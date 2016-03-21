package org.nb.mgm.client;

import org.nb.mgm.client.impl.HomeImpl;
import org.nb.mgm.client.impl.MachineImpl;
import org.nb.mgm.client.impl.ManagementImpl;

import osgi.mgm.ws.dto.HomeDTO;
import osgi.mgm.ws.dto.MachineDTO;

public class MgmFactory {

	/**
	 * Create Management client API.
	 * 
	 * @param url
	 * @param username
	 * @param password
	 * @return
	 */
	public static Management createManagement(String url, String username, String password) {
		return new ManagementImpl(url, username, password);
	}

	/**
	 * Create Management client API.
	 * 
	 * @param url
	 * @param contextRoot
	 * @param username
	 * @param password
	 * @return
	 */
	public static Management createManagement(String url, String contextRoot, String username, String password) {
		return new ManagementImpl(url, contextRoot, username, password);
	}

	/**
	 * Create Machine client API.
	 * 
	 * @param management
	 * @param machineDTO
	 * @return
	 */
	public static Machine createMachine(Management management, MachineDTO machineDTO) {
		MachineImpl machine = new MachineImpl(machineDTO);
		machine.setManagement(management);
		return machine;
	}

	/**
	 * Create Home client API.
	 * 
	 * @param management
	 * @param homeDTO
	 * @return
	 */
	public static Home createHome(Machine machine, HomeDTO homeDTO) {
		HomeImpl home = new HomeImpl(homeDTO);
		home.setMachine(machine);
		return home;
	}

}
