package org.nb.mgm.service.impl;

import static org.nb.mgm.model.ManagementConstants.ERROR_CODE_EMPTY_CLUSTER_ROOT;

import java.io.InputStream;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.nb.mgm.handler.ArtifactHandler;
import org.nb.mgm.handler.HomeHandler;
import org.nb.mgm.handler.MachineHandler;
import org.nb.mgm.handler.MetaSectorHandler;
import org.nb.mgm.handler.MetaSpaceHandler;
import org.nb.mgm.handler.ProjectHandler;
import org.nb.mgm.handler.ProjectHomeHandler;
import org.nb.mgm.handler.ProjectNodeHandler;
import org.nb.mgm.handler.ProjectSoftwareHandler;
import org.nb.mgm.model.exception.ManagementException;
import org.nb.mgm.model.query.ArtifactQuery;
import org.nb.mgm.model.query.HomeQuery;
import org.nb.mgm.model.query.MachineQuery;
import org.nb.mgm.model.query.MetaSectorQuery;
import org.nb.mgm.model.query.MetaSpaceQuery;
import org.nb.mgm.model.runtime.Artifact;
import org.nb.mgm.model.runtime.ClusterRoot;
import org.nb.mgm.model.runtime.Home;
import org.nb.mgm.model.runtime.Machine;
import org.nb.mgm.model.runtime.MetaSector;
import org.nb.mgm.model.runtime.MetaSpace;
import org.nb.mgm.model.runtime.Project;
import org.nb.mgm.model.runtime.ProjectHome;
import org.nb.mgm.model.runtime.ProjectNode;
import org.nb.mgm.model.runtime.Software;
import org.nb.mgm.model.util.ManagementPersistenceAdapter;
import org.nb.mgm.model.util.ManagementPersistenceFactory;
import org.nb.mgm.service.ManagementService;
import org.origin.common.util.PropertyUtil;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ManagementServiceImpl implements ManagementService {

	protected Logger logger = LoggerFactory.getLogger(getClass());
	protected BundleContext bundleContext;
	protected Map<Object, Object> props;
	protected ServiceRegistration<?> serviceReg;

	// adapter for loading/saving root data model
	protected ManagementPersistenceAdapter persistenceAdapter;

	// root data model
	protected ClusterRoot root;

	// data handlers
	protected MachineHandler machineHandler;
	protected HomeHandler homeHandler;
	protected MetaSectorHandler metaSectorHandler;
	protected MetaSpaceHandler metaSpaceHandler;
	protected ArtifactHandler artifactHandler;
	protected ProjectHandler projectHandler;
	protected ProjectHomeHandler projectHomeHandler;
	protected ProjectNodeHandler projectNodeHandler;
	protected ProjectSoftwareHandler projectSoftwareHandler;

	// read/write lock for data handlers
	protected ReadWriteLock machineRWLock = new ReentrantReadWriteLock();
	protected ReadWriteLock homeRWLock = new ReentrantReadWriteLock();
	protected ReadWriteLock metaSectorRWLock = new ReentrantReadWriteLock();
	protected ReadWriteLock metaSpaceRWLock = new ReentrantReadWriteLock();
	protected ReadWriteLock artifactRWLock = new ReentrantReadWriteLock();
	protected ReadWriteLock projectRWLock = new ReentrantReadWriteLock();
	protected ReadWriteLock projectHomeRWLock = new ReentrantReadWriteLock();
	protected ReadWriteLock projectNodeRWLock = new ReentrantReadWriteLock();
	protected ReadWriteLock projectSoftwareRWLock = new ReentrantReadWriteLock();

	protected boolean autoSave = false;

	/**
	 * 
	 * @param bundleContext
	 */
	public ManagementServiceImpl(BundleContext bundleContext) {
		this.bundleContext = bundleContext;

		// Load properties
		this.props = new Hashtable<Object, Object>();
		PropertyUtil.loadProperty(this.bundleContext, props, ManagementPersistenceAdapter.PERSISTENCE_TYPE);
		PropertyUtil.loadProperty(this.bundleContext, props, ManagementPersistenceAdapter.PERSISTENCE_LOCAL_DIR);
		PropertyUtil.loadProperty(this.bundleContext, props, ManagementPersistenceAdapter.PERSISTENCE_AUTOSAVE);

		// Get persistence adapter
		this.persistenceAdapter = ManagementPersistenceFactory.newInstance(props);

		// Create the handlers
		this.machineHandler = new MachineHandler(this);
		this.homeHandler = new HomeHandler(this);
		this.metaSectorHandler = new MetaSectorHandler(this);
		this.metaSpaceHandler = new MetaSpaceHandler(this);
		this.artifactHandler = new ArtifactHandler(this);
		this.projectHandler = new ProjectHandler(this);
		this.projectHomeHandler = new ProjectHomeHandler(this);
		this.projectNodeHandler = new ProjectNodeHandler(this);
		this.projectSoftwareHandler = new ProjectSoftwareHandler(this);
	}

	/**
	 * Start Management service
	 * 
	 * @throws ManagementException
	 */
	public void start() throws ManagementException {
		// 1. Load cluster data model
		this.root = this.persistenceAdapter.load();
		if (this.root == null) {
			throw new ManagementException(ERROR_CODE_EMPTY_CLUSTER_ROOT, "Cluster cannot be loaded.", null);
		}

		// 2. Register ManagementService
		Hashtable<String, Object> props = new Hashtable<String, Object>();
		this.serviceReg = this.bundleContext.registerService(ManagementService.class, this, props);
	}

	/**
	 * Stop Management service
	 * 
	 * @throws ManagementException
	 */
	public void stop() throws ManagementException {
		// Unregister ManagementService
		if (this.serviceReg != null) {
			this.serviceReg.unregister();
			this.serviceReg = null;
		}
	}

	// ------------------------------------------------------------------------------------------
	// Load/Save
	// ------------------------------------------------------------------------------------------
	public ClusterRoot getRoot() {
		return this.root;
	}

	public boolean isAutoSave() {
		return this.autoSave;
	}

	public void setAutoSave(boolean autoSave) {
		this.autoSave = autoSave;
	}

	public void save() {
		this.persistenceAdapter.save(this.root);
	}

	// ------------------------------------------------------------------------------------------
	// Machine
	// ------------------------------------------------------------------------------------------
	@Override
	public List<Machine> getMachines() throws ManagementException {
		this.machineRWLock.readLock().lock();
		try {
			return this.machineHandler.getMachines();
		} finally {
			this.machineRWLock.readLock().unlock();
		}
	}

	@Override
	public List<Machine> getMachines(MachineQuery query) throws ManagementException {
		this.machineRWLock.readLock().lock();
		try {
			return this.machineHandler.getMachines(query);
		} finally {
			this.machineRWLock.readLock().unlock();
		}
	}

	@Override
	public Machine getMachine(String machineId) throws ManagementException {
		this.machineRWLock.readLock().lock();
		try {
			return this.machineHandler.getMachine(machineId);
		} finally {
			this.machineRWLock.readLock().unlock();
		}
	}

	@Override
	public Machine addMachine(Machine newMachineRequest) throws ManagementException {
		this.machineRWLock.writeLock().lock();
		try {
			Machine newMachine = this.machineHandler.addMachine(newMachineRequest);
			if (isAutoSave()) {
				save();
			}
			return newMachine;
		} finally {
			this.machineRWLock.writeLock().unlock();
		}
	}

	@Override
	public void updateMachine(Machine machine) throws ManagementException {
		this.machineRWLock.writeLock().lock();
		try {
			this.machineHandler.updateMachine(machine);
			if (isAutoSave()) {
				save();
			}
		} finally {
			this.machineRWLock.writeLock().unlock();
		}
	}

	@Override
	public void deleteMachine(String machineId) throws ManagementException {
		this.machineRWLock.writeLock().lock();
		try {
			this.machineHandler.deleteMachine(machineId);
			if (isAutoSave()) {
				save();
			}
		} finally {
			this.machineRWLock.writeLock().unlock();
		}
	}

	@Override
	public Map<String, Object> getMachineProperties(String machineId) throws ManagementException {
		this.machineRWLock.readLock().lock();
		try {
			return this.machineHandler.getProperties(machineId);
		} finally {
			this.machineRWLock.readLock().unlock();
		}
	}

	@Override
	public boolean setMachineProperties(String machineId, Map<String, Object> properties) throws ManagementException {
		this.machineRWLock.writeLock().lock();
		try {
			this.machineHandler.setProperties(machineId, properties);
			if (isAutoSave()) {
				save();
			}
			return true;
		} finally {
			this.machineRWLock.writeLock().unlock();
		}
	}

	@Override
	public boolean removeMachineProperties(String machineId, List<String> propNames) throws ManagementException {
		this.machineRWLock.writeLock().lock();
		try {
			this.machineHandler.removeProperties(machineId, propNames);
			if (isAutoSave()) {
				save();
			}
			return true;
		} finally {
			this.machineRWLock.writeLock().unlock();
		}
	}

	// ------------------------------------------------------------------------------------------
	// Home
	// ------------------------------------------------------------------------------------------
	@Override
	public List<Home> getHomes(String machineId) throws ManagementException {
		this.homeRWLock.readLock().lock();
		try {
			return this.homeHandler.getHomes(machineId);
		} finally {
			this.homeRWLock.readLock().unlock();
		}
	}

	@Override
	public List<Home> getHomes(String machineId, HomeQuery query) throws ManagementException {
		this.homeRWLock.readLock().lock();
		try {
			return this.homeHandler.getHomes(machineId, query);
		} finally {
			this.homeRWLock.readLock().unlock();
		}
	}

	@Override
	public Home getHome(String homeId) throws ManagementException {
		this.homeRWLock.readLock().lock();
		try {
			return this.homeHandler.getHome(homeId);
		} finally {
			this.homeRWLock.readLock().unlock();
		}
	}

	@Override
	public Home addHome(String machineId, Home newHomeRequest) throws ManagementException {
		this.homeRWLock.writeLock().lock();
		try {
			Home newHome = this.homeHandler.addHome(machineId, newHomeRequest);
			if (isAutoSave()) {
				save();
			}
			return newHome;
		} finally {
			this.homeRWLock.writeLock().unlock();
		}
	}

	@Override
	public void updateHome(Home home) throws ManagementException {
		this.homeRWLock.writeLock().lock();
		try {
			this.homeHandler.updateHome(home);
			if (isAutoSave()) {
				save();
			}
		} finally {
			this.homeRWLock.writeLock().unlock();
		}
	}

	@Override
	public void deleteHome(String homeId) throws ManagementException {
		this.homeRWLock.writeLock().lock();
		try {
			// TODO:
			// 1. Send "deleteHome" command to remote Home:
			// (1) Stop all Spaces in the Home (Stop all applications on each Node; Uninstall all applications from each Node; Stop all Nodes;)
			// (2) Delete all Nodes folder in each Space
			// (3) Delete all Spaces folder in the Home
			// (4) Delete all Sectors folder in the Home (Delete all deployed applications from each Sector folder;)

			// 2. Delete Home related meta data:
			// (1) Delete all MetaNodes from the Home meta data.
			// (2) Delete all MetaSpaces from the Home meta data.
			// (3) Delete all MetaSectors (Delete all deployed applications from each MetaSector;) from the Home meta data.

			// 3. Delete Home meta data
			this.homeHandler.deleteHome(homeId);

			// 4. Save cluster model
			if (isAutoSave()) {
				save();
			}
		} finally {
			this.homeRWLock.writeLock().unlock();
		}
	}

	@Override
	public Map<String, Object> getHomeProperties(String homeId) throws ManagementException {
		this.homeRWLock.readLock().lock();
		try {
			return this.homeHandler.getProperties(homeId);
		} finally {
			this.homeRWLock.readLock().unlock();
		}
	}

	@Override
	public boolean setHomeProperties(String homeId, Map<String, Object> properties) throws ManagementException {
		this.homeRWLock.writeLock().lock();
		try {
			this.homeHandler.setProperties(homeId, properties);
			if (isAutoSave()) {
				save();
			}
			return true;
		} finally {
			this.homeRWLock.writeLock().unlock();
		}
	}

	@Override
	public boolean removeHomeProperties(String homeId, List<String> propNames) throws ManagementException {
		this.homeRWLock.writeLock().lock();
		try {
			this.homeHandler.removeProperties(homeId, propNames);
			if (isAutoSave()) {
				save();
			}
			return true;
		} finally {
			this.homeRWLock.writeLock().unlock();
		}
	}

	// ------------------------------------------------------------------------------------------
	// MetaSector
	// ------------------------------------------------------------------------------------------
	@Override
	public List<MetaSector> getMetaSectors() throws ManagementException {
		this.metaSectorRWLock.readLock().lock();
		try {
			return this.metaSectorHandler.getMetaSectors();
		} finally {
			this.metaSectorRWLock.readLock().unlock();
		}
	}

	@Override
	public List<MetaSector> getMetaSectors(MetaSectorQuery query) throws ManagementException {
		this.metaSectorRWLock.readLock().lock();
		try {
			return this.metaSectorHandler.getMetaSectors(query);
		} finally {
			this.metaSectorRWLock.readLock().unlock();
		}
	}

	@Override
	public MetaSector getMetaSector(String metaSectorId) throws ManagementException {
		this.metaSectorRWLock.readLock().lock();
		try {
			return this.metaSectorHandler.getMetaSector(metaSectorId);
		} finally {
			this.metaSectorRWLock.readLock().unlock();
		}
	}

	@Override
	public void addMetaSector(MetaSector metaSector) throws ManagementException {
		this.metaSectorRWLock.writeLock().lock();
		try {
			this.metaSectorHandler.addMetaSector(metaSector);
			if (isAutoSave()) {
				save();
			}
		} finally {
			this.metaSectorRWLock.writeLock().unlock();
		}
	}

	@Override
	public void updateMetaSector(MetaSector metaSector) throws ManagementException {
		this.metaSectorRWLock.writeLock().lock();
		try {
			this.metaSectorHandler.updateMetaSector(metaSector);
			if (isAutoSave()) {
				save();
			}
		} finally {
			this.metaSectorRWLock.writeLock().unlock();
		}
	}

	@Override
	public void deleteMetaSector(String metaSectorId) throws ManagementException {
		this.metaSectorRWLock.writeLock().lock();
		try {
			this.metaSectorHandler.deleteMetaSector(metaSectorId);
			if (isAutoSave()) {
				save();
			}
		} finally {
			this.metaSectorRWLock.writeLock().unlock();
		}
	}

	// ------------------------------------------------------------------------------------------
	// MetaSpace
	// ------------------------------------------------------------------------------------------
	@Override
	public List<MetaSpace> getMetaSpaces(String metaSectorId) throws ManagementException {
		this.metaSpaceRWLock.readLock().lock();
		try {
			return this.metaSpaceHandler.getMetaSpaces(metaSectorId);
		} finally {
			this.metaSpaceRWLock.readLock().unlock();
		}
	}

	@Override
	public List<MetaSpace> getMetaSpaces(String metaSectorId, MetaSpaceQuery query) throws ManagementException {
		this.metaSpaceRWLock.readLock().lock();
		try {
			return this.metaSpaceHandler.getMetaSpaces(metaSectorId, query);
		} finally {
			this.metaSpaceRWLock.readLock().unlock();
		}
	}

	@Override
	public MetaSpace getMetaSpace(String metaSpaceId) throws ManagementException {
		this.metaSpaceRWLock.readLock().lock();
		try {
			return this.metaSpaceHandler.getMetaSpace(metaSpaceId);
		} finally {
			this.metaSpaceRWLock.readLock().unlock();
		}
	}

	@Override
	public void addMetaSpace(String metaSectorId, MetaSpace metaSpace) throws ManagementException {
		this.metaSpaceRWLock.writeLock().lock();
		try {
			this.metaSpaceHandler.addMetaSpace(metaSectorId, metaSpace);
			if (isAutoSave()) {
				save();
			}
		} finally {
			this.metaSpaceRWLock.writeLock().unlock();
		}
	}

	@Override
	public void updateMetaSpace(MetaSpace metaSpace) throws ManagementException {
		this.metaSpaceRWLock.writeLock().lock();
		try {
			this.metaSpaceHandler.updateMetaSpace(metaSpace);
			if (isAutoSave()) {
				save();
			}
		} finally {
			this.metaSpaceRWLock.writeLock().unlock();
		}
	}

	@Override
	public void deleteMetaSpace(String metaSpaceId) throws ManagementException {
		this.metaSpaceRWLock.writeLock().lock();
		try {
			this.metaSpaceHandler.deleteMetaSpace(metaSpaceId);
			if (isAutoSave()) {
				save();
			}
		} finally {
			this.metaSpaceRWLock.writeLock().unlock();
		}
	}

	// ------------------------------------------------------------------------------------------
	// Artifact
	// ------------------------------------------------------------------------------------------
	@Override
	public List<Artifact> getArtifacts(String metaSectorId) throws ManagementException {
		this.artifactRWLock.readLock().lock();
		try {
			return this.artifactHandler.getArtifacts(metaSectorId);
		} finally {
			this.artifactRWLock.readLock().unlock();
		}
	}

	@Override
	public List<Artifact> getArtifacts(String metaSectorId, ArtifactQuery query) throws ManagementException {
		this.artifactRWLock.readLock().lock();
		try {
			return this.artifactHandler.getArtifacts(metaSectorId, query);
		} finally {
			this.artifactRWLock.readLock().unlock();
		}
	}

	@Override
	public Artifact getArtifact(String artifactId) throws ManagementException {
		this.artifactRWLock.readLock().lock();
		try {
			return this.artifactHandler.getArtifact(artifactId);
		} finally {
			this.artifactRWLock.readLock().unlock();
		}
	}

	@Override
	public void addArtifact(String metaSectorId, Artifact artifact) throws ManagementException {
		this.artifactRWLock.writeLock().lock();
		try {
			this.artifactHandler.addArtifact(metaSectorId, artifact);
			if (isAutoSave()) {
				save();
			}
		} finally {
			this.artifactRWLock.writeLock().unlock();
		}
	}

	@Override
	public void updateArtifact(Artifact artifact) throws ManagementException {
		this.artifactRWLock.writeLock().lock();
		try {
			this.artifactHandler.updateArtifact(artifact);
			if (isAutoSave()) {
				save();
			}
		} finally {
			this.artifactRWLock.writeLock().unlock();
		}
	}

	@Override
	public void deleteArtifact(String artifactId) throws ManagementException {
		this.artifactRWLock.writeLock().lock();
		try {
			this.artifactHandler.deleteArtifact(artifactId);
			if (isAutoSave()) {
				save();
			}
		} finally {
			this.artifactRWLock.writeLock().unlock();
		}
	}

	// ------------------------------------------------------------------------------------------
	// Projects
	// ------------------------------------------------------------------------------------------
	@Override
	public List<Project> getProjects() throws ManagementException {
		this.projectRWLock.readLock().lock();
		try {
			return this.projectHandler.getProjects();
		} finally {
			this.projectRWLock.readLock().unlock();
		}
	}

	@Override
	public Project getProject(String projectId) throws ManagementException {
		this.projectRWLock.readLock().lock();
		try {
			return this.projectHandler.getProject(projectId);
		} finally {
			this.projectRWLock.readLock().unlock();
		}
	}

	@Override
	public Project addProject(Project newProjectRequest) throws ManagementException {
		this.projectRWLock.writeLock().lock();
		try {
			Project newProject = this.projectHandler.addProject(newProjectRequest);
			if (isAutoSave()) {
				save();
			}
			return newProject;
		} finally {
			this.projectRWLock.writeLock().unlock();
		}
	}

	@Override
	public void updateProject(Project project) throws ManagementException {
		this.projectRWLock.writeLock().lock();
		try {
			this.projectHandler.updateProject(project);
			if (isAutoSave()) {
				save();
			}
		} finally {
			this.projectRWLock.writeLock().unlock();
		}
	}

	@Override
	public boolean deleteProject(String projectId) throws ManagementException {
		this.projectRWLock.writeLock().lock();
		try {
			boolean succeed = this.projectHandler.deleteProject(projectId);
			if (isAutoSave()) {
				save();
			}
			return succeed;
		} finally {
			this.projectRWLock.writeLock().unlock();
		}
	}

	// ------------------------------------------------------------------------------------------
	// ProjectHome
	// ------------------------------------------------------------------------------------------
	@Override
	public List<ProjectHome> getProjectHomes(String projectId) throws ManagementException {
		this.projectHomeRWLock.readLock().lock();
		try {
			return this.projectHomeHandler.getProjectHomes(projectId);
		} finally {
			this.projectHomeRWLock.readLock().unlock();
		}
	}

	@Override
	public ProjectHome getProjectHome(String projectId, String projectHomeId) throws ManagementException {
		this.projectHomeRWLock.readLock().lock();
		try {
			return this.projectHomeHandler.getProjectHome(projectId, projectHomeId);
		} finally {
			this.projectHomeRWLock.readLock().unlock();
		}
	}

	@Override
	public ProjectHome addProjectHome(String projectId, ProjectHome projectHome) throws ManagementException {
		this.projectHomeRWLock.writeLock().lock();
		try {
			ProjectHome newProjectHome = this.projectHomeHandler.addProjectHome(projectId, projectHome);
			if (isAutoSave()) {
				save();
			}
			return newProjectHome;
		} finally {
			this.projectHomeRWLock.writeLock().unlock();
		}
	}

	@Override
	public void updateProjectHome(String projectId, ProjectHome projectHome) throws ManagementException {
		this.projectHomeRWLock.writeLock().lock();
		try {
			this.projectHomeHandler.updateProjectHome(projectId, projectHome);
			if (isAutoSave()) {
				save();
			}
		} finally {
			this.projectHomeRWLock.writeLock().unlock();
		}
	}

	@Override
	public boolean deleteProjectHome(String projectId, String projectHomeId) throws ManagementException {
		this.projectHomeRWLock.writeLock().lock();
		try {
			// TODO:
			// 1. Send command to remote Home (if a remote Home is configured):
			// (1) Stop all nodes that belongs to the project in the remote home
			// (2) Delete all nodes that belongs to the project in the remote home

			// 3. Delete ProjectHome
			boolean succeed = this.projectHomeHandler.deleteProjectHome(projectId, projectHomeId);

			// 4. Save cluster model
			if (isAutoSave()) {
				save();
			}

			return succeed;
		} finally {
			this.projectHomeRWLock.writeLock().unlock();
		}
	}

	@Override
	public boolean setProjectDeploymentHome(String projectId, String projectHomeId, String homeId) throws ManagementException {
		this.projectHomeRWLock.writeLock().lock();
		try {
			// TODO:
			// remove existing deployment Home.

			// Set Project's deployment Home
			boolean succeed = this.projectHomeHandler.setDeploymentHome(projectId, projectHomeId, homeId);

			// Save cluster model
			if (isAutoSave()) {
				save();
			}

			return succeed;
		} finally {
			this.projectHomeRWLock.writeLock().unlock();
		}
	}

	@Override
	public boolean removeProjectDeploymentHome(String projectId, String projectHomeId, String homeId) throws ManagementException {
		this.projectHomeRWLock.writeLock().lock();
		try {
			// TODO:
			// stop all running nodes and dispose ProjectHome on the remote Home.

			// Set Project's deployment Home
			boolean succeed = this.projectHomeHandler.removeDeploymentHome(projectId, projectHomeId, homeId);

			// Save cluster model
			if (isAutoSave()) {
				save();
			}

			return succeed;
		} finally {
			this.projectHomeRWLock.writeLock().unlock();
		}
	}

	// ------------------------------------------------------------------------------------------
	// ProjectNode
	// ------------------------------------------------------------------------------------------
	@Override
	public List<ProjectNode> getProjectNodes(String projectId, String projectHomeId) throws ManagementException {
		this.projectNodeRWLock.readLock().lock();
		try {
			return this.projectNodeHandler.getProjectNodes(projectId, projectHomeId);
		} finally {
			this.projectNodeRWLock.readLock().unlock();
		}
	}

	@Override
	public ProjectNode getProjectNode(String projectId, String projectHomeId, String projectNodeId) throws ManagementException {
		this.projectNodeRWLock.readLock().lock();
		try {
			return this.projectNodeHandler.getProjectNode(projectId, projectHomeId, projectNodeId);
		} finally {
			this.projectNodeRWLock.readLock().unlock();
		}
	}

	@Override
	public ProjectNode addProjectNode(String projectId, String projectHomeId, ProjectNode newProjectNodeRequest) throws ManagementException {
		this.projectNodeRWLock.writeLock().lock();
		try {
			ProjectNode newProjectNode = this.projectNodeHandler.addProjectNode(projectId, projectHomeId, newProjectNodeRequest);
			if (isAutoSave()) {
				save();
			}
			return newProjectNode;
		} finally {
			this.projectNodeRWLock.writeLock().unlock();
		}
	}

	@Override
	public void updateProjectNode(String projectId, String projectHomeId, ProjectNode projectNode) throws ManagementException {
		this.projectNodeRWLock.writeLock().lock();
		try {
			this.projectNodeHandler.updateProjectNode(projectId, projectHomeId, projectNode);
			if (isAutoSave()) {
				save();
			}
		} finally {
			this.projectNodeRWLock.writeLock().unlock();
		}
	}

	@Override
	public boolean deleteProjectNode(String projectId, String projectHomeId, String projectNodeId) throws ManagementException {
		this.projectNodeRWLock.writeLock().lock();
		try {
			// TODO:
			// 1. Send command to remote Home (if a Home is configured):
			// (1) Stop the node in the remote home
			// (2) Delete the node in the remote home

			// 3. Delete ProjectNode
			boolean succeed = this.projectNodeHandler.deleteProjectNode(projectId, projectHomeId, projectNodeId);

			// 4. Save cluster model
			if (isAutoSave()) {
				save();
			}

			return succeed;
		} finally {
			this.projectNodeRWLock.writeLock().unlock();
		}
	}

	@Override
	public List<Software> getInstalledProjectNodeSoftware(String projectId, String projectHomeId, String projectNodeId) throws ManagementException {
		this.projectNodeRWLock.readLock().lock();
		try {
			return this.projectNodeHandler.getInstalledSoftware(projectId, projectHomeId, projectNodeId);
		} finally {
			this.projectNodeRWLock.readLock().unlock();
		}
	}

	@Override
	public boolean installProjectNodeSoftware(String projectId, String projectHomeId, String projectNodeId, String softwareId) throws ManagementException {
		this.projectNodeRWLock.writeLock().lock();
		try {
			Project project = getProject(projectId);
			if (project == null) {

			}
			Software software = project.getSoftware(softwareId);
			if (software == null) {

			}

			boolean succeed = this.projectNodeHandler.installSoftware(projectId, projectHomeId, projectNodeId, softwareId);
			if (isAutoSave()) {
				save();
			}
			return succeed;
		} finally {
			this.projectNodeRWLock.writeLock().unlock();
		}
	}

	@Override
	public boolean uninstallProjectNodeSoftware(String projectId, String projectHomeId, String projectNodeId, String softwareId) throws ManagementException {
		this.projectNodeRWLock.writeLock().lock();
		try {
			boolean succeed = this.projectNodeHandler.uninstallSoftware(projectId, projectHomeId, projectNodeId, softwareId);
			if (isAutoSave()) {
				save();
			}
			return succeed;
		} finally {
			this.projectNodeRWLock.writeLock().unlock();
		}
	}

	// ------------------------------------------------------------------------------------------
	// Project Software
	// ------------------------------------------------------------------------------------------
	@Override
	public List<Software> getProjectSoftware(String projectId) throws ManagementException {
		this.projectSoftwareRWLock.readLock().lock();
		try {
			return this.projectSoftwareHandler.getProjectSoftware(projectId);
		} finally {
			this.projectSoftwareRWLock.readLock().unlock();
		}
	}

	@Override
	public Software getProjectSoftware(String projectId, String softwareId) throws ManagementException {
		this.projectSoftwareRWLock.readLock().lock();
		try {
			return this.projectSoftwareHandler.getProjectSoftware(projectId, softwareId);
		} finally {
			this.projectSoftwareRWLock.readLock().unlock();
		}
	}

	@Override
	public InputStream getProjectSoftwareContent(String projectId, String softwareId) throws ManagementException {
		this.projectSoftwareRWLock.readLock().lock();
		try {
			return this.projectSoftwareHandler.getProjectSoftwareContent(projectId, softwareId);
		} finally {
			this.projectSoftwareRWLock.readLock().unlock();
		}
	}

	@Override
	public boolean setProjectSoftwareContent(String projectId, String softwareId, String fileName, long length, Date lastModified, InputStream input) throws ManagementException {
		this.projectSoftwareRWLock.writeLock().lock();
		try {
			boolean succeed = this.projectSoftwareHandler.setProjectSoftwareContent(projectId, softwareId, fileName, length, lastModified, input);
			if (isAutoSave()) {
				save();
			}
			return succeed;
		} finally {
			this.projectSoftwareRWLock.writeLock().unlock();
		}
	}

	@Override
	public Software addProjectSoftware(String projectId, Software newSoftwareRequest) throws ManagementException {
		this.projectSoftwareRWLock.writeLock().lock();
		try {
			Software newSoftware = this.projectSoftwareHandler.addProjectSoftware(projectId, newSoftwareRequest);
			if (isAutoSave()) {
				save();
			}
			return newSoftware;
		} finally {
			this.projectSoftwareRWLock.writeLock().unlock();
		}
	}

	@Override
	public void updateProjectSoftware(String projectId, Software software) throws ManagementException {
		this.projectSoftwareRWLock.writeLock().lock();
		try {
			this.projectSoftwareHandler.updateProjectSoftware(projectId, software);
			if (isAutoSave()) {
				save();
			}
		} finally {
			this.projectSoftwareRWLock.writeLock().unlock();
		}
	}

	@Override
	public boolean deleteProjectSoftware(String projectId, String softwareId) throws ManagementException {
		this.projectSoftwareRWLock.writeLock().lock();
		try {
			// TODO:
			// 1. Remove the software from ProjectHome.
			// 2. Undeploy the woftware from remote Home.

			boolean succeed = this.projectSoftwareHandler.deleteProjectSoftware(projectId, softwareId);
			if (succeed && isAutoSave()) {
				save();
			}
			return succeed;
		} finally {
			this.projectSoftwareRWLock.writeLock().unlock();
		}
	}

}
