package org.nb.mgm.client.api;

import org.nb.mgm.client.api.impl.HomeImpl;
import org.nb.mgm.client.api.impl.MachineImpl;
import org.nb.mgm.client.api.impl.ManagementImpl;
import org.nb.mgm.client.api.impl.MetaSectorImpl;
import org.nb.mgm.client.api.impl.MetaSpaceImpl;
import org.nb.mgm.client.api.impl.ProjectHomeImpl;
import org.nb.mgm.client.api.impl.ProjectImpl;
import org.nb.mgm.client.api.impl.ProjectNodeImpl;
import org.nb.mgm.model.dto.HomeDTO;
import org.nb.mgm.model.dto.MachineDTO;
import org.nb.mgm.model.dto.MetaSectorDTO;
import org.nb.mgm.model.dto.MetaSpaceDTO;
import org.nb.mgm.model.dto.ProjectDTO;
import org.nb.mgm.model.dto.ProjectHomeDTO;
import org.nb.mgm.model.dto.ProjectNodeDTO;

public class ManagementFactory {

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
	public static IMachine createMachine(Management management, MachineDTO machineDTO) {
		return new MachineImpl(management, machineDTO);
	}

	/**
	 * Create Home client.
	 * 
	 * @param management
	 * @param machine
	 * @param homeDTO
	 * @return
	 */
	public static IHome createHome(Management management, IMachine machine, HomeDTO homeDTO) {
		return new HomeImpl(management, machine, homeDTO);
	}

	/**
	 * Create MetaSector client.
	 * 
	 * @param management
	 * @param metaSectorDTO
	 * @return
	 */
	public static IMetaSector createMetaSector(Management management, MetaSectorDTO metaSectorDTO) {
		return new MetaSectorImpl(management, metaSectorDTO);
	}

	/**
	 * Create MetaSpace client.
	 * 
	 * @param management
	 * @param metaSector
	 * @param metaSpaceDTO
	 * @return
	 */
	public static IMetaSpace createMetaSpace(Management management, IMetaSector metaSector, MetaSpaceDTO metaSpaceDTO) {
		return new MetaSpaceImpl(management, metaSector, metaSpaceDTO);
	}

	/**
	 * Create Project client.
	 * 
	 * @param management
	 * @param projectDTO
	 * @return
	 */
	public static IProject createProject(Management management, ProjectDTO projectDTO) {
		return new ProjectImpl(management, projectDTO);
	}

	/**
	 * Create ProjectHome client.
	 * 
	 * @param management
	 * @param project
	 * @param projectHomeDTO
	 * @return
	 */
	public static IProjectHome createProjectHome(Management management, IProject project, ProjectHomeDTO projectHomeDTO) {
		return new ProjectHomeImpl(management, project, projectHomeDTO);
	}

	/**
	 * Create ProjectNode client.
	 * 
	 * @param management
	 * @param project
	 * @param projectHome
	 * @param projectNodeDTO
	 * @return
	 */
	public static IProjectNode createProjectNode(Management management, IProject project, IProjectHome projectHome, ProjectNodeDTO projectNodeDTO) {
		return new ProjectNodeImpl(management, project, projectHome, projectNodeDTO);
	}

}
