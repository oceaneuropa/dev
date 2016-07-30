package org.nb.mgm.client.api;

import org.nb.mgm.client.api.impl.HomeImpl;
import org.nb.mgm.client.api.impl.MachineImpl;
import org.nb.mgm.client.api.impl.ManagementClientImpl;
import org.nb.mgm.client.api.impl.MetaSectorImpl;
import org.nb.mgm.client.api.impl.MetaSpaceImpl;
import org.nb.mgm.client.api.impl.ProjectHomeImpl;
import org.nb.mgm.client.api.impl.ProjectImpl;
import org.nb.mgm.client.api.impl.ProjectNodeImpl;
import org.nb.mgm.client.api.impl.SoftwareImpl;
import org.nb.mgm.model.dto.HomeDTO;
import org.nb.mgm.model.dto.MachineDTO;
import org.nb.mgm.model.dto.MetaSectorDTO;
import org.nb.mgm.model.dto.MetaSpaceDTO;
import org.nb.mgm.model.dto.ProjectDTO;
import org.nb.mgm.model.dto.ProjectHomeDTO;
import org.nb.mgm.model.dto.ProjectNodeDTO;
import org.nb.mgm.model.dto.SoftwareDTO;

public class ManagementFactory {

	/**
	 * Create Management client interface.
	 * 
	 * @param url
	 * @param username
	 * @param password
	 * @return
	 */
	public static ManagementClient createManagement(String url, String username, String password) {
		return new ManagementClientImpl(url, "mgm/v1", username, password);
	}

	/**
	 * Create Machine client interface.
	 * 
	 * @param management
	 * @param machineDTO
	 * @return
	 */
	public static IMachine createMachine(ManagementClient management, MachineDTO machineDTO) {
		return new MachineImpl(management, machineDTO);
	}

	/**
	 * Create Home client interface.
	 * 
	 * @param management
	 * @param machine
	 * @param homeDTO
	 * @return
	 */
	public static IHome createHome(ManagementClient management, IMachine machine, HomeDTO homeDTO) {
		return new HomeImpl(management, machine, homeDTO);
	}

	/**
	 * Create MetaSector client interface.
	 * 
	 * @param management
	 * @param metaSectorDTO
	 * @return
	 */
	public static IMetaSector createMetaSector(ManagementClient management, MetaSectorDTO metaSectorDTO) {
		return new MetaSectorImpl(management, metaSectorDTO);
	}

	/**
	 * Create MetaSpace client interface.
	 * 
	 * @param management
	 * @param metaSector
	 * @param metaSpaceDTO
	 * @return
	 */
	public static IMetaSpace createMetaSpace(ManagementClient management, IMetaSector metaSector, MetaSpaceDTO metaSpaceDTO) {
		return new MetaSpaceImpl(management, metaSector, metaSpaceDTO);
	}

	/**
	 * Create Project client interface.
	 * 
	 * @param management
	 * @param projectDTO
	 * @return
	 */
	public static IProject createProject(ManagementClient management, ProjectDTO projectDTO) {
		return new ProjectImpl(management, projectDTO);
	}

	/**
	 * Create ProjectHome client interface.
	 * 
	 * @param management
	 * @param project
	 * @param projectHomeDTO
	 * @return
	 */
	public static IProjectHome createProjectHome(ManagementClient management, IProject project, ProjectHomeDTO projectHomeDTO) {
		return new ProjectHomeImpl(management, project, projectHomeDTO);
	}

	/**
	 * Create ProjectNode client interface.
	 * 
	 * @param management
	 * @param project
	 * @param projectHome
	 * @param projectNodeDTO
	 * @return
	 */
	public static IProjectNode createProjectNode(ManagementClient management, IProject project, IProjectHome projectHome, ProjectNodeDTO projectNodeDTO) {
		return new ProjectNodeImpl(management, project, projectHome, projectNodeDTO);
	}

	/**
	 * Create Software client interface.
	 * 
	 * @param management
	 * @param project
	 * @param softwareDTO
	 * @return
	 */
	public static ISoftware createSoftware(ManagementClient management, IProject project, SoftwareDTO softwareDTO) {
		return new SoftwareImpl(management, project, softwareDTO);
	}

}
