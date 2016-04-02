package org.nb.mgm.client;

import org.nb.mgm.client.impl.HomeImpl;
import org.nb.mgm.client.impl.MachineImpl;
import org.nb.mgm.client.impl.ManagementImpl;
import org.nb.mgm.client.impl.MetaSectorImpl;
import org.nb.mgm.client.impl.MetaSpaceImpl;

import osgi.mgm.ws.dto.HomeDTO;
import osgi.mgm.ws.dto.MachineDTO;
import osgi.mgm.ws.dto.MetaSectorDTO;
import osgi.mgm.ws.dto.MetaSpaceDTO;

public class MgmFactory {

	/**
	 * Create Management client.
	 * 
	 * @param url
	 * @param username
	 * @param password
	 * @return
	 */
	public static Management createManagement(String url, String username, String password) {
		return new ManagementImpl(url, "mgm/v1", username, password);
	}

	/**
	 * Create Machine client.
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
	 * Create Home client.
	 * 
	 * @param machine
	 * @param homeDTO
	 * @return
	 */
	public static Home createHome(Machine machine, HomeDTO homeDTO) {
		HomeImpl home = new HomeImpl(homeDTO);
		home.setMachine(machine);
		return home;
	}

	/**
	 * Create MetaSector client.
	 * 
	 * @param management
	 * @param metaSectorDTO
	 * @return
	 */
	public static MetaSector createMetaSector(Management management, MetaSectorDTO metaSectorDTO) {
		MetaSectorImpl metaSector = new MetaSectorImpl(metaSectorDTO);
		metaSector.setManagement(management);
		return metaSector;
	}

	/**
	 * Create MetaSpace client.
	 * 
	 * @param metaSector
	 * @param metaSpaceDTO
	 * @return
	 */
	public static MetaSpace createMetaSpace(MetaSector metaSector, MetaSpaceDTO metaSpaceDTO) {
		MetaSpaceImpl metaSpace = new MetaSpaceImpl(metaSpaceDTO);
		metaSpace.setMetaSector(metaSector);
		return metaSpace;
	}

}
