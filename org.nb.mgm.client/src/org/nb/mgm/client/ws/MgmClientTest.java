package org.nb.mgm.client.ws;

import java.util.List;

import org.nb.mgm.client.util.ClientConfiguration;
import org.nb.mgm.client.util.ClientException;

import osgi.mgm.ws.mgm.dto.HomeDTO;
import osgi.mgm.ws.mgm.dto.MachineDTO;

public class MgmClientTest {

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		ClientConfiguration config = ClientConfiguration.get("http://localhost:8090", "/mgm", "admin");
		MachineClient client = new MachineClient(config);
		try {
			List<MachineDTO> machines = client.getMachines();
			for (MachineDTO machine : machines) {
				String machineId = machine.getId();
				String machineName = machine.getName();
				String machineDesc = machine.getDescription();
				String machineIp = machine.getIpAddress();

				List<HomeDTO> homes = machine.getHomes();
				for (HomeDTO home : homes) {
					String homeId = home.getId();
					String homeName = home.getName();
					String homeDesc = home.getDescription();
					String homeUrl = home.getUrl();
				}
			}
		} catch (ClientException e) {
			e.printStackTrace();
		}
	}

}
