package org.nb.mgm.client.api.impl;

import static org.nb.mgm.client.api.ManagementConstants.ERROR_CODE_ENTITY_MULTIPLE_ENTITIES_FOUND;

import java.io.File;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.nb.home.client.api.HomeAgent;
import org.nb.home.client.api.HomeAgentFactory;
import org.nb.mgm.client.api.IHome;
import org.nb.mgm.client.api.IMachine;
import org.nb.mgm.client.api.IMetaSector;
import org.nb.mgm.client.api.IMetaSpace;
import org.nb.mgm.client.api.IProject;
import org.nb.mgm.client.api.IProjectHome;
import org.nb.mgm.client.api.IProjectNode;
import org.nb.mgm.client.api.ISoftware;
import org.nb.mgm.client.api.ManagementClient;
import org.nb.mgm.client.api.ManagementConstants;
import org.nb.mgm.client.api.ManagementFactory;
import org.nb.mgm.client.ws.HomeWSClient;
import org.nb.mgm.client.ws.MachineWSClient;
import org.nb.mgm.client.ws.MetaSectorWSClient;
import org.nb.mgm.client.ws.MetaSpaceWSClient;
import org.nb.mgm.client.ws.ProjectHomeWSClient;
import org.nb.mgm.client.ws.ProjectNodeWSClient;
import org.nb.mgm.client.ws.ProjectSoftwareWSClient;
import org.nb.mgm.client.ws.ProjectWSClient;
import org.nb.mgm.model.dto.Action;
import org.nb.mgm.model.dto.HomeDTO;
import org.nb.mgm.model.dto.MachineDTO;
import org.nb.mgm.model.dto.MetaSectorDTO;
import org.nb.mgm.model.dto.MetaSpaceDTO;
import org.nb.mgm.model.dto.ProjectDTO;
import org.nb.mgm.model.dto.ProjectHomeDTO;
import org.nb.mgm.model.dto.ProjectNodeDTO;
import org.nb.mgm.model.dto.SoftwareDTO;
import org.origin.common.adapter.AdaptorSupport;
import org.origin.common.rest.client.ClientConfiguration;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.model.StatusDTO;

public class ManagementClientImpl implements ManagementClient {

	protected ClientConfiguration clientConfig;

	protected MachineWSClient machineClient;
	protected HomeWSClient homeClient;
	protected MetaSectorWSClient metaSectorClient;
	protected MetaSpaceWSClient metaSpaceClient;
	protected ProjectWSClient projectClient;
	protected ProjectHomeWSClient projectHomeClient;
	protected ProjectNodeWSClient projectNodeClient;
	protected ProjectSoftwareWSClient projectSoftwareClient;
	protected Map<String, HomeAgent> homeAgentMap;

	protected AdaptorSupport adaptorSupport = new AdaptorSupport();

	/**
	 * 
	 * @param url
	 * @param contextRoot
	 * @param username
	 * @param password
	 */
	public ManagementClientImpl(String url, String contextRoot, String username, String password) {
		this.clientConfig = ClientConfiguration.get(url, contextRoot, username);
		this.clientConfig.setPassword(password);

		this.machineClient = new MachineWSClient(this.clientConfig);
		this.homeClient = new HomeWSClient(this.clientConfig);
		this.metaSectorClient = new MetaSectorWSClient(this.clientConfig);
		this.metaSpaceClient = new MetaSpaceWSClient(this.clientConfig);
		this.projectClient = new ProjectWSClient(this.clientConfig);
		this.projectHomeClient = new ProjectHomeWSClient(this.clientConfig);
		this.projectNodeClient = new ProjectNodeWSClient(this.clientConfig);
		this.projectSoftwareClient = new ProjectSoftwareWSClient(this.clientConfig);

		this.homeAgentMap = new HashMap<String, HomeAgent>();
	}

	// ------------------------------------------------------------------------------------------
	// Machine
	// ------------------------------------------------------------------------------------------
	@Override
	public List<IMachine> getMachines() throws ClientException {
		return getMachines(null);
	}

	@Override
	public List<IMachine> getMachines(Map<String, ?> properties) throws ClientException {
		checkClient(this.machineClient);

		List<IMachine> machines = new ArrayList<IMachine>();

		List<MachineDTO> machineDTOs = this.machineClient.getMachines(properties);
		for (MachineDTO machineDTO : machineDTOs) {
			IMachine machine = ManagementFactory.createMachine(this, machineDTO);
			machines.add(machine);
		}
		return machines;
	}

	@Override
	public IMachine getMachine(String machineId) throws ClientException {
		checkClient(this.machineClient);
		checkMachineId(machineId);

		IMachine machine = null;

		MachineDTO machineDTO = this.machineClient.getMachine(machineId);
		if (machineDTO != null) {
			machine = ManagementFactory.createMachine(this, machineDTO);
		}
		return machine;
	}

	@Override
	public IMachine addMachine(String name, String ipAddress, String description) throws ClientException {
		checkClient(this.machineClient);

		IMachine machine = null;

		MachineDTO newMachineRequest = new MachineDTO();
		newMachineRequest.setName(name);
		newMachineRequest.setIpAddress(ipAddress);
		newMachineRequest.setDescription(description);

		MachineDTO newMachineDTO = this.machineClient.addMachine(newMachineRequest);
		if (newMachineDTO != null) {
			machine = ManagementFactory.createMachine(this, newMachineDTO);
		}
		return machine;
	}

	@Override
	public boolean updateMachine(MachineDTO updateMachineRequest) throws ClientException {
		checkClient(this.machineClient);

		StatusDTO status = this.machineClient.updateMachine(updateMachineRequest);
		return (status != null && status.success()) ? true : false;
	}

	@Override
	public boolean removeMachine(String machineId) throws ClientException {
		checkClient(this.machineClient);
		checkMachineId(machineId);

		StatusDTO status = this.machineClient.deleteMachine(machineId);
		return (status != null && status.success()) ? true : false;
	}

	@Override
	public Map<String, Object> getMachineProperties(String machineId, boolean useJsonString) throws ClientException {
		checkClient(this.machineClient);
		checkMachineId(machineId);

		Map<String, Object> properties = this.machineClient.getProperties(machineId, true);
		if (properties == null) {
			properties = new HashMap<String, Object>();
		}
		return properties;
	}

	@Override
	public boolean setMachineProperties(String machineId, Map<String, Object> properties) throws ClientException {
		checkClient(this.machineClient);
		checkMachineId(machineId);

		StatusDTO status = this.machineClient.setProperties(machineId, properties);
		return (status != null && status.success()) ? true : false;
	}

	@Override
	public boolean removeMachineProperties(String machineId, List<String> propertyNames) throws ClientException {
		checkClient(this.machineClient);
		checkMachineId(machineId);

		StatusDTO status = this.machineClient.removeProperties(machineId, propertyNames);
		return (status != null && status.success()) ? true : false;
	}

	// ------------------------------------------------------------------------------------------
	// Home
	// ------------------------------------------------------------------------------------------
	@Override
	public List<IHome> getHomes(String machineId) throws ClientException {
		return getHomes(machineId, null);
	}

	@Override
	public List<IHome> getHomes(String machineId, Properties properties) throws ClientException {
		checkClient(this.homeClient);
		checkMachineId(machineId);

		List<IHome> homes = new ArrayList<IHome>();

		IMachine machine = getMachine(machineId);
		if (machine != null) {
			List<HomeDTO> homeDTOs = this.homeClient.getHomes(machineId, properties);
			for (HomeDTO homeDTO : homeDTOs) {
				IHome home = ManagementFactory.createHome(this, machine, homeDTO);
				homes.add(home);
			}
		}
		return homes;
	}

	@Override
	public IHome getHome(String machineId, String homeId) throws ClientException {
		checkClient(this.homeClient);
		checkMachineId(machineId);
		checkHomeId(homeId);

		IHome home = null;

		IMachine machine = getMachine(machineId);
		if (machine != null) {
			HomeDTO homeDTO = this.homeClient.getHome(machineId, homeId);
			if (homeDTO != null) {
				home = ManagementFactory.createHome(this, machine, homeDTO);
			}
		}
		return home;
	}

	@Override
	public IHome getHome(String homeId) throws ClientException {
		checkClient(this.homeClient);
		checkHomeId(homeId);

		IHome home = null;
		String machineId = getContainerMachineId(homeId);
		if (machineId != null) {
			home = getHome(machineId, homeId);
		}
		return home;
	}

	@Override
	public IHome addHome(String machineId, String name, String description) throws ClientException {
		checkClient(this.homeClient);
		checkMachineId(machineId);

		IHome home = null;
		IMachine machine = getMachine(machineId);
		if (machine != null) {
			HomeDTO newHomeRequest = new HomeDTO();
			newHomeRequest.setName(name);
			newHomeRequest.setDescription(description);

			HomeDTO newHomeDTO = this.homeClient.addHome(machineId, newHomeRequest);
			if (newHomeDTO != null) {
				home = ManagementFactory.createHome(this, machine, newHomeDTO);
			}
		}
		return home;
	}

	@Override
	public boolean updateHome(String machineId, HomeDTO updateHomeRequest) throws ClientException {
		checkClient(this.homeClient);
		checkMachineId(machineId);

		StatusDTO status = this.homeClient.updateHome(machineId, updateHomeRequest);
		return (status != null && status.success()) ? true : false;
	}

	@Override
	public boolean removeHome(String machineId, String homeId) throws ClientException {
		checkClient(this.homeClient);
		checkMachineId(machineId);
		checkHomeId(homeId);

		StatusDTO status = this.homeClient.deleteHome(machineId, homeId);
		return (status != null && status.success()) ? true : false;
	}

	@Override
	public boolean removeHome(String homeId) throws ClientException {
		checkClient(this.homeClient);
		checkHomeId(homeId);

		String machineId = getContainerMachineId(homeId);
		if (machineId != null) {
			return removeHome(machineId, homeId);
		}
		return false;
	}

	@Override
	public Map<String, Object> getHomeProperties(String machineId, String homeId, boolean useJsonString) throws ClientException {
		checkClient(this.homeClient);
		checkMachineId(machineId);
		checkHomeId(homeId);

		Map<String, Object> properties = this.homeClient.getProperties(machineId, homeId, true);
		if (properties == null) {
			properties = new HashMap<String, Object>();
		}
		return properties;
	}

	@Override
	public boolean setHomeProperties(String machineId, String homeId, Map<String, Object> properties) throws ClientException {
		checkClient(this.homeClient);
		checkMachineId(machineId);
		checkHomeId(homeId);

		StatusDTO status = this.homeClient.setProperties(machineId, homeId, properties);
		return (status != null && status.success()) ? true : false;
	}

	@Override
	public boolean removeHomeProperties(String machineId, String homeId, List<String> propertyNames) throws ClientException {
		checkClient(this.homeClient);
		checkMachineId(machineId);
		checkHomeId(homeId);

		StatusDTO status = this.homeClient.removeProperties(machineId, homeId, propertyNames);
		return (status != null && status.success()) ? true : false;
	}

	/**
	 * Get container Machine Id of a Home.
	 * 
	 * @param homeId
	 * @return
	 * @throws ClientException
	 */
	protected String getContainerMachineId(String homeId) throws ClientException {
		String machineId = null;
		int matchedHomeCount = 0;
		List<IMachine> machines = getMachines();
		for (IMachine machine : machines) {
			String currMachineId = machine.getId();

			List<HomeDTO> homeDTOs = this.homeClient.getHomes(currMachineId, null);
			for (HomeDTO homeDTO : homeDTOs) {
				String currHomeId = homeDTO.getId();

				if (homeId.equals(currHomeId)) {
					if (machineId == null) {
						machineId = currMachineId;
					}
					matchedHomeCount++;
				}
			}
		}
		if (matchedHomeCount > 1) {
			throw new ClientException(ERROR_CODE_ENTITY_MULTIPLE_ENTITIES_FOUND, "Multiple Homes with specified homeId are found.");
		}
		return machineId;
	}

	// @Override
	// public void connect(IHome home) throws ClientException {
	// HomeAgent homeAgent = getHomeAgent(home);
	// if (homeAgent.isConnected()) {
	// System.err.println("Home [" + home.getName() + "] has already been connected.");
	// return;
	// }
	//
	// homeAgent.connect();
	// }
	//
	// @Override
	// public void disconnect(IHome home) throws ClientException {
	// HomeAgent homeAgent = getHomeAgent(home);
	// if (!homeAgent.isConnected()) {
	// System.err.println("Home [" + home.getName() + "] has readly been disconnected.");
	// return;
	// }
	// homeAgent.disconnect();
	// }
	//
	// @Override
	// public boolean isConnected(IHome home) throws ClientException {
	// HomeAgent homeAgent = getHomeAgent(home);
	// return homeAgent.isConnected();
	// }
	//
	// @Override
	// public boolean isHomeAgentActive(IHome home) throws ClientException {
	// HomeAgent homeAgent = getHomeAgent(home);
	// return homeAgent.isHomeAgentActive();
	// }

	/**
	 * 
	 * @param projectHomeId
	 * @return
	 * @throws ClientException
	 */
	public HomeAgent getHomeAgent(String projectHomeId) throws ClientException {
		HomeAgent agent = null;
		IProjectHome projectHome = getProjectHome(projectHomeId);
		if (projectHome == null) {
			throw new ClientException(500, "ProjectHome is not found.");
		}
		IHome home = projectHome.getDeploymentHome();
		if (home == null) {
			throw new ClientException(500, String.format("ProjectHome [%s] is not configured with deployment Home.", projectHome.getName()));
		}
		agent = getHomeAgent(home);
		return agent;
	}

	/**
	 * 
	 * @param home
	 * @return
	 */
	public synchronized HomeAgent getHomeAgent(IHome home) throws ClientException {
		ClientConfiguration clientConfig = home.getClientConfiguration();
		if (clientConfig == null) {
			throw new ClientException(500, String.format("Home [%s] does not have properties for client configuration.", home.getName()));
		}
		String configId = clientConfig.getId();
		HomeAgent homeAgent = this.homeAgentMap.get(configId);
		if (homeAgent == null) {
			homeAgent = HomeAgentFactory.createHomeAgent(this, clientConfig);
			this.homeAgentMap.put(configId, homeAgent);
		}
		return homeAgent;
	}

	// ------------------------------------------------------------------------------------------
	// MetaSector
	// ------------------------------------------------------------------------------------------
	@Override
	public List<IMetaSector> getMetaSectors() throws ClientException {
		return getMetaSectors(null);
	}

	@Override
	public List<IMetaSector> getMetaSectors(Properties properties) throws ClientException {
		checkClient(this.metaSectorClient);

		List<IMetaSector> metaSectors = new ArrayList<IMetaSector>();

		List<MetaSectorDTO> machineDTOs = this.metaSectorClient.getMetaSectors(properties);
		for (MetaSectorDTO machineDTO : machineDTOs) {
			IMetaSector metaSector = ManagementFactory.createMetaSector(this, machineDTO);
			metaSectors.add(metaSector);
		}
		return metaSectors;
	}

	@Override
	public IMetaSector getMetaSector(String metaSectorId) throws ClientException {
		checkClient(this.metaSectorClient);

		IMetaSector metaSector = null;

		MetaSectorDTO metaSectorDTO = this.metaSectorClient.getMetaSector(metaSectorId);
		if (metaSectorDTO != null) {
			metaSector = ManagementFactory.createMetaSector(this, metaSectorDTO);
		}
		return metaSector;
	}

	@Override
	public IMetaSector addMetaSector(String name, String description) throws ClientException {
		checkClient(this.metaSectorClient);

		IMetaSector metaSector = null;

		MetaSectorDTO newMetaSectorRequest = new MetaSectorDTO();
		newMetaSectorRequest.setName(name);
		newMetaSectorRequest.setDescription(description);

		MetaSectorDTO newMetaSectorDTO = this.metaSectorClient.addMetaSector(newMetaSectorRequest);
		if (newMetaSectorDTO != null) {
			metaSector = ManagementFactory.createMetaSector(this, newMetaSectorDTO);
		}
		return metaSector;
	}

	@Override
	public boolean updateMetaSector(MetaSectorDTO updateMetaSectorRequest) throws ClientException {
		checkClient(this.metaSectorClient);

		StatusDTO status = this.metaSectorClient.updateMetaSector(updateMetaSectorRequest);
		return (status != null && status.success()) ? true : false;
	}

	@Override
	public boolean deleteMetaSector(String metaSectorId) throws ClientException {
		checkClient(this.metaSectorClient);

		StatusDTO status = this.metaSectorClient.deleteMetaSector(metaSectorId);
		return (status != null && status.success()) ? true : false;
	}

	// ------------------------------------------------------------------------------------------
	// MetaSpace
	// ------------------------------------------------------------------------------------------
	@Override
	public List<IMetaSpace> getMetaSpaces(String metaSectorId) throws ClientException {
		return getMetaSpaces(metaSectorId, null);
	}

	@Override
	public List<IMetaSpace> getMetaSpaces(String metaSectorId, Properties properties) throws ClientException {
		checkClient(this.metaSpaceClient);

		List<IMetaSpace> metaSpaces = new ArrayList<IMetaSpace>();

		IMetaSector metaSector = getMetaSector(metaSectorId);
		if (metaSector != null) {
			List<MetaSpaceDTO> metaSpaceDTOs = this.metaSpaceClient.getMetaSpaces(metaSectorId, properties);
			for (MetaSpaceDTO metaSpaceDTO : metaSpaceDTOs) {
				IMetaSpace metaSpace = ManagementFactory.createMetaSpace(this, metaSector, metaSpaceDTO);
				metaSpaces.add(metaSpace);
			}
		}
		return metaSpaces;
	}

	@Override
	public IMetaSpace getMetaSpace(String metaSectorId, String metaSpaceId) throws ClientException {
		checkClient(this.metaSpaceClient);
		checkMetaSectorId(metaSectorId);
		checkMetaSpaceId(metaSpaceId);

		IMetaSpace metaSpace = null;

		IMetaSector metaSector = getMetaSector(metaSectorId);
		if (metaSector != null) {
			MetaSpaceDTO metaSpaceDTO = this.metaSpaceClient.getMetaSpace(metaSectorId, metaSpaceId);
			if (metaSpaceDTO != null) {
				metaSpace = ManagementFactory.createMetaSpace(this, metaSector, metaSpaceDTO);
			}
		}
		return metaSpace;
	}

	@Override
	public IMetaSpace getMetaSpace(String metaSpaceId) throws ClientException {
		checkClient(this.metaSpaceClient);
		checkMetaSpaceId(metaSpaceId);

		IMetaSpace metaSpace = null;

		String metaSectorId = null;
		int matchedMetaSpaceCount = 0;
		List<IMetaSector> metaSectors = getMetaSectors();
		for (IMetaSector metaSector : metaSectors) {
			String currMetaSectorId = metaSector.getId();

			List<MetaSpaceDTO> metaSpaceDTOs = this.metaSpaceClient.getMetaSpaces(currMetaSectorId);
			for (MetaSpaceDTO metaSpaceDTO : metaSpaceDTOs) {
				String currMetaSpaceId = metaSpaceDTO.getId();

				if (metaSpaceId.equals(currMetaSpaceId)) {
					if (metaSectorId == null) {
						metaSectorId = currMetaSpaceId;
					}
					matchedMetaSpaceCount++;
				}
			}
		}
		if (matchedMetaSpaceCount > 1) {
			throw new ClientException(ERROR_CODE_ENTITY_MULTIPLE_ENTITIES_FOUND, "Multiple MetaSpaces with specified metaSpaceId are found.");
		}

		if (metaSectorId != null) {
			metaSpace = getMetaSpace(metaSectorId, metaSpaceId);
		}
		return metaSpace;
	}

	@Override
	public IMetaSpace addMetaSpace(String metaSectorId, String name, String description) throws ClientException {
		checkClient(this.metaSpaceClient);
		checkMetaSectorId(metaSectorId);

		IMetaSpace metaSpace = null;

		IMetaSector metaSector = getMetaSector(metaSectorId);
		if (metaSector != null) {
			MetaSpaceDTO newMetaSpaceRequest = new MetaSpaceDTO();
			newMetaSpaceRequest.setName(name);
			newMetaSpaceRequest.setDescription(description);

			MetaSpaceDTO newMetaSpaceDTO = this.metaSpaceClient.addMetaSpace(metaSectorId, newMetaSpaceRequest);
			if (newMetaSpaceDTO != null) {
				metaSpace = ManagementFactory.createMetaSpace(this, metaSector, newMetaSpaceDTO);
			}
		}
		return metaSpace;
	}

	@Override
	public boolean updateMetaSpace(String metaSectorId, MetaSpaceDTO updateMetaSpaceRequest) throws ClientException {
		checkClient(this.metaSpaceClient);
		checkMetaSectorId(metaSectorId);

		StatusDTO status = this.metaSpaceClient.updateMetaSpace(metaSectorId, updateMetaSpaceRequest);
		return (status != null && status.success()) ? true : false;
	}

	@Override
	public boolean deleteMetaSpace(String metaSectorId, String metaSpaceId) throws ClientException {
		checkClient(this.metaSpaceClient);
		checkMetaSectorId(metaSectorId);
		checkMetaSpaceId(metaSpaceId);

		StatusDTO status = this.metaSpaceClient.deleteMetaSpace(metaSectorId, metaSpaceId);
		return (status != null && status.success()) ? true : false;
	}

	@Override
	public boolean deleteMetaSpace(String metaSpaceId) throws ClientException {
		checkClient(this.metaSpaceClient);
		checkMetaSpaceId(metaSpaceId);

		String metaSectorId = null;
		int matchedMetaSpaceCount = 0;
		List<IMetaSector> metaSectors = getMetaSectors();
		for (IMetaSector metaSector : metaSectors) {
			String currMetaSectorId = metaSector.getId();

			List<MetaSpaceDTO> metaSpaceDTOs = this.metaSpaceClient.getMetaSpaces(currMetaSectorId);
			for (MetaSpaceDTO metaSpaceDTO : metaSpaceDTOs) {
				String currMetaSpaceId = metaSpaceDTO.getId();

				if (metaSpaceId.equals(currMetaSpaceId)) {
					if (metaSectorId == null) {
						metaSectorId = currMetaSpaceId;
					}
					matchedMetaSpaceCount++;
				}
			}
		}
		if (matchedMetaSpaceCount > 1) {
			throw new ClientException(ERROR_CODE_ENTITY_MULTIPLE_ENTITIES_FOUND, "Multiple MetaSpaces with specified metaSpaceId are found.");
		}

		if (metaSectorId != null) {
			return deleteMetaSpace(metaSectorId, metaSpaceId);
		}
		return false;
	}

	// ------------------------------------------------------------------------------------------
	// Project
	// ------------------------------------------------------------------------------------------
	@Override
	public List<IProject> getProjects() throws ClientException {
		checkClient(this.projectClient);

		List<IProject> projects = new ArrayList<IProject>();

		List<ProjectDTO> projectDTOs = this.projectClient.getProjects();
		for (ProjectDTO projectDTO : projectDTOs) {
			IProject project = ManagementFactory.createProject(this, projectDTO);
			projects.add(project);
		}
		return projects;
	}

	@Override
	public IProject getProject(String projectId) throws ClientException {
		checkClient(this.projectClient);
		checkProjectId(projectId);

		IProject project = null;

		ProjectDTO projectDTO = this.projectClient.getProject(projectId);
		if (projectDTO != null) {
			project = ManagementFactory.createProject(this, projectDTO);
		}
		return project;
	}

	@Override
	public IProject addProject(String projectId, String name, String description) throws ClientException {
		checkClient(this.projectClient);
		checkProjectId(projectId);

		IProject project = null;

		ProjectDTO newProjectRequest = new ProjectDTO();
		newProjectRequest.setId(projectId);
		newProjectRequest.setName(name);
		newProjectRequest.setDescription(description);

		ProjectDTO newProjectDTO = this.projectClient.addProject(newProjectRequest);
		if (newProjectDTO != null) {
			project = ManagementFactory.createProject(this, newProjectDTO);
		}
		return project;
	}

	@Override
	public boolean updateProject(ProjectDTO updateProjectRequest) throws ClientException {
		checkClient(this.projectClient);

		StatusDTO status = this.projectClient.updateProject(updateProjectRequest);
		return (status != null && status.success()) ? true : false;
	}

	@Override
	public boolean deleteProject(String projectId) throws ClientException {
		checkClient(this.projectClient);
		checkProjectId(projectId);

		StatusDTO status = this.projectClient.deleteProject(projectId);
		return (status != null && status.success()) ? true : false;
	}

	// ------------------------------------------------------------------------------------------
	// ProjectHome
	// ------------------------------------------------------------------------------------------
	@Override
	public List<IProjectHome> getProjectHomes(String projectId) throws ClientException {
		checkClient(this.projectHomeClient);
		checkProjectId(projectId);

		List<IProjectHome> projectHomes = new ArrayList<IProjectHome>();

		IProject project = getProject(projectId);
		if (project != null) {
			List<ProjectHomeDTO> projectHomeDTOs = this.projectHomeClient.getProjectHomes(projectId);
			for (ProjectHomeDTO projectHomeDTO : projectHomeDTOs) {
				IProjectHome projectHome = ManagementFactory.createProjectHome(this, project, projectHomeDTO);
				projectHomes.add(projectHome);
			}
		}
		return projectHomes;
	}

	@Override
	public IProjectHome getProjectHome(String projectId, String projectHomeId) throws ClientException {
		checkClient(this.projectHomeClient);
		checkProjectId(projectId);
		checkProjectHomeId(projectHomeId);

		IProjectHome projectHome = null;

		IProject project = getProject(projectId);
		if (project != null) {
			ProjectHomeDTO projectHomeDTO = this.projectHomeClient.getProjectHome(projectId, projectHomeId);
			if (projectHomeDTO != null) {
				projectHome = ManagementFactory.createProjectHome(this, project, projectHomeDTO);
			}
		}
		return projectHome;
	}

	@Override
	public IProjectHome getProjectHome(String projectHomeId) throws ClientException {
		checkClient(this.projectHomeClient);
		checkProjectHomeId(projectHomeId);

		IProjectHome projectHome = null;
		String projectId = getContainerProjectIdOfProjectHome(projectHomeId);
		if (projectId != null) {
			projectHome = getProjectHome(projectId, projectHomeId);
		}
		return projectHome;
	}

	@Override
	public IProjectHome addProjectHome(String projectId, String name, String description) throws ClientException {
		checkClient(this.projectHomeClient);
		checkProjectId(projectId);

		IProjectHome projectHome = null;

		IProject project = getProject(projectId);
		if (project != null) {
			ProjectHomeDTO newProjectHomeRequest = new ProjectHomeDTO();
			newProjectHomeRequest.setName(name);
			newProjectHomeRequest.setDescription(description);

			ProjectHomeDTO newProjectHomeDTO = this.projectHomeClient.addProjectHome(projectId, newProjectHomeRequest);
			if (newProjectHomeDTO != null) {
				projectHome = ManagementFactory.createProjectHome(this, project, newProjectHomeDTO);
			}
		}
		return projectHome;
	}

	@Override
	public boolean updateProjectHome(String projectId, ProjectHomeDTO updateProjectHomeRequest) throws ClientException {
		checkClient(this.projectHomeClient);
		checkProjectId(projectId);

		StatusDTO status = this.projectHomeClient.updateProjectHome(projectId, updateProjectHomeRequest);
		return (status != null && status.success()) ? true : false;
	}

	@Override
	public boolean deleteProjectHome(String projectId, String projectHomeId) throws ClientException {
		checkClient(this.projectHomeClient);
		checkProjectId(projectId);
		checkProjectHomeId(projectHomeId);

		StatusDTO status = this.projectHomeClient.deleteProjectHome(projectId, projectHomeId);
		return (status != null && status.success()) ? true : false;
	}

	@Override
	public boolean deleteProjectHome(String projectHomeId) throws ClientException {
		checkClient(this.projectHomeClient);
		checkProjectHomeId(projectHomeId);

		String projectId = getContainerProjectIdOfProjectHome(projectHomeId);
		if (projectId != null) {
			return deleteProjectHome(projectId, projectHomeId);
		}
		return false;
	}

	@Override
	public IHome getProjectDeploymentHome(String projectHomeId) throws ClientException {
		checkClient(this.projectHomeClient);
		checkProjectHomeId(projectHomeId);

		IHome home = null;
		String projectId = getContainerProjectIdOfProjectHome(projectHomeId);
		if (projectId != null) {
			boolean hasAttribute = projectHomeClient.hasProjectHomeAttribute(projectId, projectHomeId, "homeId");
			if (hasAttribute) {
				String homeId = this.projectHomeClient.getProjectHomeAttribute(projectId, projectHomeId, "homeId", String.class);
				if (homeId != null && !homeId.isEmpty()) {
					home = getHome(homeId);
				}
			}
		}
		return home;
	}

	@Override
	public boolean setProjectDeploymentHome(String projectHomeId, String homeId) throws ClientException {
		checkClient(this.projectHomeClient);
		checkProjectHomeId(projectHomeId);
		checkHomeId(homeId);

		String projectId = getContainerProjectIdOfProjectHome(projectHomeId);
		if (projectId != null) {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("homeId", homeId);
			Action action = new Action("set_project_deployment_home", parameters);

			StatusDTO status = this.projectHomeClient.sendAction(projectId, projectHomeId, action);
			return (status != null && status.success()) ? true : false;
		}
		return false;
	}

	@Override
	public boolean removeProjectDeploymentHome(String projectHomeId, String homeId) throws ClientException {
		checkClient(this.projectHomeClient);
		checkProjectHomeId(projectHomeId);
		checkHomeId(homeId);

		String projectId = getContainerProjectIdOfProjectHome(projectHomeId);
		if (projectId != null) {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("homeId", homeId);
			Action action = new Action("remove_project_deployment_home", parameters);

			StatusDTO status = this.projectHomeClient.sendAction(projectId, projectHomeId, action);
			return (status != null && status.success()) ? true : false;
		}
		return false;
	}

	/**
	 * Get container Project id of a ProjectHome.
	 * 
	 * @param projectHomeId
	 * @return
	 * @throws ClientException
	 */
	protected String getContainerProjectIdOfProjectHome(String projectHomeId) throws ClientException {
		String projectId = null;
		int matchedProjectHomeCounts = 0;
		List<IProject> projects = getProjects();
		for (IProject project : projects) {
			String currProjectId = project.getId();

			List<ProjectHomeDTO> projectHomeDTOs = this.projectHomeClient.getProjectHomes(currProjectId);
			for (ProjectHomeDTO projectHomeDTO : projectHomeDTOs) {
				String currProjectHomeId = projectHomeDTO.getId();

				if (projectHomeId.equals(currProjectHomeId)) {
					if (projectId == null) {
						projectId = currProjectId;
					}
					matchedProjectHomeCounts++;
				}
			}
		}
		if (matchedProjectHomeCounts > 1) {
			throw new ClientException(ERROR_CODE_ENTITY_MULTIPLE_ENTITIES_FOUND, "Multiple ProjectHomes with specified projectHomeId are found.");
		}
		return projectId;
	}

	// ------------------------------------------------------------------------------------------
	// ProjectNode
	// ------------------------------------------------------------------------------------------
	@Override
	public List<IProjectNode> getProjectNodes(String projectId, String projectHomeId) throws ClientException {
		checkClient(this.projectNodeClient);
		checkProjectId(projectId);
		checkProjectHomeId(projectHomeId);

		List<IProjectNode> projectNodes = new ArrayList<IProjectNode>();

		IProject project = getProject(projectId);
		IProjectHome projectHome = getProjectHome(projectId, projectHomeId);

		if (project != null && projectHome != null) {
			List<ProjectNodeDTO> projectNodeDTOs = this.projectNodeClient.getProjectNodes(projectId, projectHomeId);
			for (ProjectNodeDTO projectNodeDTO : projectNodeDTOs) {
				IProjectNode projectNode = ManagementFactory.createProjectNode(this, project, projectHome, projectNodeDTO);
				projectNodes.add(projectNode);
			}
		}
		return projectNodes;
	}

	@Override
	public IProjectNode getProjectNode(String projectId, String projectHomeId, String projectNodeId) throws ClientException {
		checkClient(this.projectNodeClient);
		checkProjectId(projectId);
		checkProjectHomeId(projectHomeId);
		checkProjectNodeId(projectNodeId);

		IProjectNode projectNode = null;

		IProject project = getProject(projectId);
		IProjectHome projectHome = getProjectHome(projectId, projectHomeId);
		if (project != null && projectHome != null) {
			ProjectNodeDTO projectNodeDTO = this.projectNodeClient.getProjectNode(projectId, projectHomeId, projectNodeId);
			if (projectNodeDTO != null) {
				projectNode = ManagementFactory.createProjectNode(this, project, projectHome, projectNodeDTO);
			}
		}

		return projectNode;
	}

	@Override
	public IProjectNode getProjectNode(String projectHomeId, String projectNodeId) throws ClientException {
		checkClient(this.projectNodeClient);
		checkProjectHomeId(projectHomeId);
		checkProjectNodeId(projectNodeId);

		IProjectNode projectNode = null;

		String projectId = getContainerProjectIdOfProjectHome(projectHomeId);
		if (projectId != null) {
			IProject project = getProject(projectId);
			IProjectHome projectHome = getProjectHome(projectId, projectHomeId);
			if (project != null && projectHome != null) {
				ProjectNodeDTO projectNodeDTO = this.projectNodeClient.getProjectNode(projectId, projectHomeId, projectNodeId);
				if (projectNodeDTO != null) {
					projectNode = ManagementFactory.createProjectNode(this, project, projectHome, projectNodeDTO);
				}
			}
		}
		return projectNode;
	}

	@Override
	public IProjectNode getProjectNode(String projectNodeId) throws ClientException {
		checkClient(this.projectHomeClient);
		checkClient(this.projectNodeClient);
		checkProjectNodeId(projectNodeId);

		IProjectNode projectNode = null;

		String projectId = null;
		String projectHomeId = null;
		int matchedNodeCounts = 0;
		List<IProject> projects = getProjects();
		for (IProject project : projects) {
			String currProjectId = project.getId();

			List<ProjectHomeDTO> projectHomeDTOs = this.projectHomeClient.getProjectHomes(currProjectId);
			for (ProjectHomeDTO projectHomeDTO : projectHomeDTOs) {
				String currProjectHomeId = projectHomeDTO.getId();

				List<ProjectNodeDTO> projectNodeDTOs = this.projectNodeClient.getProjectNodes(currProjectId, currProjectHomeId);
				for (ProjectNodeDTO projectNodeDTO : projectNodeDTOs) {
					String currProjectNodeId = projectNodeDTO.getId();

					if (projectNodeId.equals(currProjectNodeId)) {
						if (projectHomeId == null) {
							projectId = currProjectId;
							projectHomeId = currProjectHomeId;
						}
						matchedNodeCounts++;
					}
				}
			}
		}
		if (matchedNodeCounts > 1) {
			throw new ClientException(ERROR_CODE_ENTITY_MULTIPLE_ENTITIES_FOUND, "Multiple Nodes with specified projectNodeId are found.");
		}

		if (projectId != null && projectHomeId != null) {
			projectNode = getProjectNode(projectId, projectHomeId, projectNodeId);
		}
		return projectNode;
	}

	@Override
	public IProjectNode addProjectNode(String projectId, String projectHomeId, String projectNodeId, String name, String description) throws ClientException {
		checkClient(this.projectNodeClient);
		checkProjectId(projectId);
		checkProjectHomeId(projectHomeId);

		IProjectNode projectNode = null;

		IProject project = getProject(projectId);
		IProjectHome projectHome = getProjectHome(projectId, projectHomeId);

		if (project != null && projectHome != null) {
			ProjectNodeDTO newProjectNodeRequest = new ProjectNodeDTO();
			newProjectNodeRequest.setId(projectNodeId);
			newProjectNodeRequest.setName(name);
			newProjectNodeRequest.setDescription(description);

			ProjectNodeDTO newProjectNodeDTO = this.projectNodeClient.addProjectNode(projectId, projectHomeId, newProjectNodeRequest);
			if (newProjectNodeDTO != null) {
				projectNode = ManagementFactory.createProjectNode(this, project, projectHome, newProjectNodeDTO);
			}
		}
		return projectNode;
	}

	@Override
	public boolean updateProjectNode(String projectId, String projectHomeId, ProjectNodeDTO updateProjectNodeRequest) throws ClientException {
		checkClient(this.projectNodeClient);
		checkProjectId(projectId);
		checkProjectHomeId(projectHomeId);

		StatusDTO status = this.projectNodeClient.updateProjectNode(projectId, projectHomeId, updateProjectNodeRequest);
		return (status != null && status.success()) ? true : false;
	}

	@Override
	public boolean deleteProjectNode(String projectId, String projectHomeId, String projectNodeId) throws ClientException {
		checkClient(this.projectNodeClient);
		checkProjectId(projectId);
		checkProjectHomeId(projectHomeId);
		checkProjectNodeId(projectNodeId);

		StatusDTO status = this.projectNodeClient.deleteProjectNode(projectId, projectHomeId, projectNodeId);
		return (status != null && status.success()) ? true : false;
	}

	@Override
	public boolean deleteProjectNode(String projectHomeId, String projectNodeId) throws ClientException {
		checkClient(this.projectNodeClient);
		checkProjectHomeId(projectHomeId);
		checkProjectNodeId(projectNodeId);

		String projectId = getContainerProjectIdOfProjectHome(projectHomeId);
		if (projectId == null || projectId.trim().isEmpty()) {
			throw new ClientException(ManagementConstants.ERROR_CODE_ENTITY_EMPTY_ID, "projectId cannot be found.", null);
		}

		StatusDTO status = this.projectNodeClient.deleteProjectNode(projectId, projectHomeId, projectNodeId);
		return (status != null && status.success()) ? true : false;
	}

	@Override
	public List<ISoftware> getProjectNodeSoftware(String projectId, String projectHomeId, String projectNodeId) throws ClientException {
		checkClient(this.projectNodeClient);
		checkProjectId(projectId);
		checkProjectHomeId(projectHomeId);
		checkProjectNodeId(projectNodeId);

		List<ISoftware> softwareList = new ArrayList<ISoftware>();

		IProject project = getProject(projectId);
		if (project != null) {
			List<SoftwareDTO> softwareDTOs = this.projectNodeClient.getProjectNodeSoftware(projectId, projectHomeId, projectNodeId);
			for (SoftwareDTO softwareDTO : softwareDTOs) {
				ISoftware software = ManagementFactory.createSoftware(this, project, softwareDTO);
				softwareList.add(software);
			}
		}
		return softwareList;
	}

	@Override
	public boolean installProjectNodeSoftware(String projectId, String projectHomeId, String projectNodeId, String softwareId) throws ClientException {
		checkClient(this.projectNodeClient);
		checkProjectId(projectId);
		checkProjectHomeId(projectHomeId);
		checkProjectNodeId(projectNodeId);
		checkSoftwareId(softwareId);

		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("softwareId", softwareId);
		Action action = new Action("install_project_node_software", parameters);

		StatusDTO status = this.projectNodeClient.sendAction(projectId, projectHomeId, projectNodeId, action);
		return (status != null && status.success()) ? true : false;
	}

	@Override
	public boolean uninstallProjectNodeSoftware(String projectId, String projectHomeId, String projectNodeId, String softwareId) throws ClientException {
		checkClient(this.projectNodeClient);
		checkProjectId(projectId);
		checkProjectHomeId(projectHomeId);
		checkProjectNodeId(projectNodeId);
		checkSoftwareId(softwareId);

		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("softwareId", softwareId);
		Action action = new Action("uninstall_project_node_software", parameters);

		StatusDTO status = this.projectNodeClient.sendAction(projectId, projectHomeId, projectNodeId, action);
		return (status != null && status.success()) ? true : false;
	}

	// ------------------------------------------------------------------------------------------
	// ProjectSoftware
	// ------------------------------------------------------------------------------------------
	@Override
	public List<ISoftware> getProjectSoftwareList(String projectId) throws ClientException {
		checkClient(this.projectSoftwareClient);
		checkProjectId(projectId);

		List<ISoftware> softwareList = new ArrayList<ISoftware>();

		IProject project = getProject(projectId);
		if (project != null) {
			List<SoftwareDTO> softwareDTOs = this.projectSoftwareClient.getProjectSoftware(projectId);
			for (SoftwareDTO softwareDTO : softwareDTOs) {
				ISoftware software = ManagementFactory.createSoftware(this, project, softwareDTO);
				softwareList.add(software);
			}
		}
		return softwareList;
	}

	@Override
	public ISoftware getProjectSoftware(String projectId, String softwareId) throws ClientException {
		checkClient(this.projectSoftwareClient);
		checkProjectId(projectId);
		checkSoftwareId(softwareId);

		ISoftware software = null;
		IProject project = getProject(projectId);
		if (project != null) {
			SoftwareDTO softwareDTO = this.projectSoftwareClient.getProjectSoftware(projectId, softwareId);
			if (softwareDTO != null) {
				software = ManagementFactory.createSoftware(this, project, softwareDTO);
			}
		}
		return software;
	}

	@Override
	public ISoftware getProjectSoftware(String softwareId) throws ClientException {
		checkClient(this.projectSoftwareClient);
		checkProjectHomeId(softwareId);

		ISoftware software = null;
		String projectId = getContainerProjectIdOfProjectSoftware(softwareId);
		if (projectId != null) {
			software = getProjectSoftware(projectId, softwareId);
		}
		return software;
	}

	@Override
	public ISoftware addProjectSoftware(String projectId, String type, String name, String version, String description) throws ClientException {
		checkClient(this.projectSoftwareClient);
		checkProjectId(projectId);

		ISoftware software = null;

		IProject project = getProject(projectId);
		if (project != null) {
			SoftwareDTO newSoftwareRequest = new SoftwareDTO();
			newSoftwareRequest.setType(type);
			newSoftwareRequest.setName(name);
			newSoftwareRequest.setVersion(version);
			newSoftwareRequest.setDescription(description);

			SoftwareDTO newSoftwareDTO = this.projectSoftwareClient.addProjectSoftware(projectId, newSoftwareRequest);
			if (newSoftwareDTO != null) {
				software = ManagementFactory.createSoftware(this, project, newSoftwareDTO);
			}
		}
		return software;
	}

	/**
	 * Update Software in a Project.
	 * 
	 * @param projectId
	 * @param updateSoftwareRequest
	 * @return
	 * @throws ClientException
	 */
	public boolean updateProjectSoftware(String projectId, SoftwareDTO updateSoftwareRequest) throws ClientException {
		checkClient(this.projectSoftwareClient);
		checkProjectId(projectId);

		StatusDTO status = this.projectSoftwareClient.updateProjectSoftware(projectId, updateSoftwareRequest);
		return (status != null && status.success()) ? true : false;
	}

	@Override
	public boolean deleteProjectSoftware(String projectId, String softwareId) throws ClientException {
		checkClient(this.projectSoftwareClient);
		checkProjectId(projectId);
		checkSoftwareId(softwareId);

		StatusDTO status = this.projectSoftwareClient.deleteProjectSoftware(projectId, softwareId);
		return (status != null && status.success()) ? true : false;
	}

	@Override
	public boolean deleteProjectSoftware(String softwareId) throws ClientException {
		checkClient(this.projectSoftwareClient);
		checkSoftwareId(softwareId);

		String projectId = getContainerProjectIdOfProjectSoftware(softwareId);
		if (projectId != null) {
			return deleteProjectSoftware(projectId, softwareId);
		}
		return false;
	}

	@Override
	public boolean uploadProjectSoftwareFile(String projectId, String softwareId, File srcFile) throws ClientException {
		checkClient(this.projectSoftwareClient);
		checkProjectId(projectId);
		checkSoftwareId(softwareId);

		StatusDTO status = this.projectSoftwareClient.uploadProjectSoftwareFile(projectId, softwareId, srcFile);
		return (status != null && status.success()) ? true : false;
	}

	@Override
	public boolean downloadProjectSoftwareToFile(String projectId, String softwareId, File destFile) throws ClientException {
		checkClient(this.projectSoftwareClient);
		checkProjectId(projectId);
		checkSoftwareId(softwareId);

		return this.projectSoftwareClient.downloadProjectSoftwareToFile(projectId, softwareId, destFile);
	}

	@Override
	public boolean downloadProjectSoftwareToOutputStream(String projectId, String softwareId, OutputStream output) throws ClientException {
		checkClient(this.projectSoftwareClient);
		checkProjectId(projectId);
		checkSoftwareId(softwareId);

		return this.projectSoftwareClient.downloadProjectSoftwareToOutputStream(projectId, softwareId, output);
	}

	/**
	 * 
	 * @param softwareId
	 * @return
	 * @throws ClientException
	 */
	protected String getContainerProjectIdOfProjectSoftware(String softwareId) throws ClientException {
		String projectId = null;
		int matchedSoftwareCounts = 0;
		List<IProject> projects = getProjects();
		for (IProject project : projects) {
			String currProjectId = project.getId();

			List<SoftwareDTO> softwareDTOs = this.projectSoftwareClient.getProjectSoftware(currProjectId);
			for (SoftwareDTO softwareDTO : softwareDTOs) {
				String currSoftwareId = softwareDTO.getId();

				if (softwareId.equals(currSoftwareId)) {
					if (projectId == null) {
						projectId = currProjectId;
					}
					matchedSoftwareCounts++;
				}
			}
		}
		if (matchedSoftwareCounts > 1) {
			throw new ClientException(ERROR_CODE_ENTITY_MULTIPLE_ENTITIES_FOUND, "Multiple Software with specified softwareId are found.");
		}
		return projectId;
	}

	// ------------------------------------------------------------------------------------------
	// Check WS Client
	// ------------------------------------------------------------------------------------------
	/**
	 * 
	 * @param machineClient
	 * @throws ClientException
	 */
	protected void checkClient(MachineWSClient machineClient) throws ClientException {
		if (machineClient == null) {
			throw new ClientException(ManagementConstants.ERROR_CODE_WS_CLIENT_NOT_FOUND, "MachineClient is not found.", null);
		}
	}

	/**
	 * 
	 * @param homeClient
	 * @throws ClientException
	 */
	protected void checkClient(HomeWSClient homeClient) throws ClientException {
		if (homeClient == null) {
			throw new ClientException(ManagementConstants.ERROR_CODE_WS_CLIENT_NOT_FOUND, "HomeClient is not found.", null);
		}
	}

	/**
	 * 
	 * @param metaSectorClient
	 * @throws ClientException
	 */
	protected void checkClient(MetaSectorWSClient metaSectorClient) throws ClientException {
		if (metaSectorClient == null) {
			throw new ClientException(ManagementConstants.ERROR_CODE_WS_CLIENT_NOT_FOUND, "MetaSectorClient is not found.", null);
		}
	}

	/**
	 * 
	 * @param metaSpaceClient
	 * @throws ClientException
	 */
	protected void checkClient(MetaSpaceWSClient metaSpaceClient) throws ClientException {
		if (metaSpaceClient == null) {
			throw new ClientException(ManagementConstants.ERROR_CODE_WS_CLIENT_NOT_FOUND, "MetaSpaceClient is not found.", null);
		}
	}

	/**
	 * 
	 * @param projectClient
	 * @throws ClientException
	 */
	protected void checkClient(ProjectWSClient projectClient) throws ClientException {
		if (projectClient == null) {
			throw new ClientException(ManagementConstants.ERROR_CODE_WS_CLIENT_NOT_FOUND, "ProjectClient is not found.", null);
		}
	}

	/**
	 * 
	 * @param projectHomeClient
	 * @return
	 * @throws ClientException
	 */
	protected void checkClient(ProjectHomeWSClient projectHomeClient) throws ClientException {
		if (projectHomeClient == null) {
			throw new ClientException(ManagementConstants.ERROR_CODE_WS_CLIENT_NOT_FOUND, "ProjectHomeClient is not found.", null);
		}
	}

	/**
	 * 
	 * @param projectNodeClient
	 * @throws ClientException
	 */
	protected void checkClient(ProjectNodeWSClient projectNodeClient) throws ClientException {
		if (projectNodeClient == null) {
			throw new ClientException(ManagementConstants.ERROR_CODE_WS_CLIENT_NOT_FOUND, "ProjectNodeClient is not found.", null);
		}
	}

	/**
	 * 
	 * @param projectSoftwareClient
	 * @throws ClientException
	 */
	protected void checkClient(ProjectSoftwareWSClient projectSoftwareClient) throws ClientException {
		if (projectSoftwareClient == null) {
			throw new ClientException(ManagementConstants.ERROR_CODE_WS_CLIENT_NOT_FOUND, "ProjectSoftwareClient is not found.", null);
		}
	}

	// ------------------------------------------------------------------------------------------
	// Check IDs
	// ------------------------------------------------------------------------------------------
	/**
	 * 
	 * @param machineId
	 * @throws ClientException
	 */
	protected void checkMachineId(String machineId) throws ClientException {
		if (machineId == null || machineId.trim().isEmpty()) {
			throw new ClientException(ManagementConstants.ERROR_CODE_ENTITY_EMPTY_ID, "machineId is empty.", null);
		}
	}

	/**
	 * 
	 * @param homeId
	 * @throws ClientException
	 */
	protected void checkHomeId(String homeId) throws ClientException {
		if (homeId == null || homeId.trim().isEmpty()) {
			throw new ClientException(ManagementConstants.ERROR_CODE_ENTITY_EMPTY_ID, "homeId is empty.", null);
		}
	}

	/**
	 * 
	 * @param metaSectorId
	 * @throws ClientException
	 */
	protected void checkMetaSectorId(String metaSectorId) throws ClientException {
		if (metaSectorId == null || metaSectorId.trim().isEmpty()) {
			throw new ClientException(ManagementConstants.ERROR_CODE_ENTITY_EMPTY_ID, "metaSectorId is empty.", null);
		}
	}

	/**
	 * 
	 * @param metaSpaceId
	 * @throws ClientException
	 */
	protected void checkMetaSpaceId(String metaSpaceId) throws ClientException {
		if (metaSpaceId == null || metaSpaceId.trim().isEmpty()) {
			throw new ClientException(ManagementConstants.ERROR_CODE_ENTITY_EMPTY_ID, "metaSpaceId is empty.", null);
		}
	}

	/**
	 * 
	 * @param projectHomeId
	 * @throws ClientException
	 */
	protected void checkProjectId(String projectId) throws ClientException {
		if (projectId == null || projectId.trim().isEmpty()) {
			throw new ClientException(ManagementConstants.ERROR_CODE_ENTITY_EMPTY_ID, "projectId is empty.", null);
		}
	}

	/**
	 * 
	 * @param projectHomeId
	 * @throws ClientException
	 */
	protected void checkProjectHomeId(String projectHomeId) throws ClientException {
		if (projectHomeId == null || projectHomeId.trim().isEmpty()) {
			throw new ClientException(ManagementConstants.ERROR_CODE_ENTITY_EMPTY_ID, "projectHomeId is empty.", null);
		}
	}

	/**
	 * 
	 * @param projectNodeId
	 * @throws ClientException
	 */
	protected void checkProjectNodeId(String projectNodeId) throws ClientException {
		if (projectNodeId == null || projectNodeId.trim().isEmpty()) {
			throw new ClientException(ManagementConstants.ERROR_CODE_ENTITY_EMPTY_ID, "projectNodeId is empty.", null);
		}
	}

	/**
	 * 
	 * @param softwareId
	 * @throws ClientException
	 */
	protected void checkSoftwareId(String softwareId) throws ClientException {
		if (softwareId == null || softwareId.trim().isEmpty()) {
			throw new ClientException(ManagementConstants.ERROR_CODE_ENTITY_EMPTY_ID, "softwareId is empty.", null);
		}
	}

	/** implement IAdaptable interface */
	@SuppressWarnings("unchecked")
	@Override
	public <T> T getAdapter(Class<T> adapter) {
		if (MachineWSClient.class.equals(adapter)) {
			return (T) this.machineClient;

		} else if (HomeWSClient.class.equals(adapter)) {
			return (T) this.homeClient;

		} else if (MetaSectorWSClient.class.equals(adapter)) {
			return (T) this.metaSectorClient;

		} else if (MetaSpaceWSClient.class.equals(adapter)) {
			return (T) this.metaSpaceClient;

		} else if (ProjectWSClient.class.equals(adapter)) {
			return (T) this.projectClient;

		} else if (ProjectHomeWSClient.class.equals(adapter)) {
			return (T) this.projectHomeClient;

		} else if (ProjectNodeWSClient.class.equals(adapter)) {
			return (T) this.projectNodeClient;

		} else if (ProjectSoftwareWSClient.class.equals(adapter)) {
			return (T) this.projectSoftwareClient;
		}

		T result = this.adaptorSupport.getAdapter(adapter);
		if (result != null) {
			return result;
		}
		return null;
	}

	@Override
	public <T> void adapt(Class<T> clazz, T object) {
		adaptorSupport.adapt(clazz, object);
	}

}
