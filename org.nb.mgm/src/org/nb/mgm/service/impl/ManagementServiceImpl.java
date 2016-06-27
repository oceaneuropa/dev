package org.nb.mgm.service.impl;

import static org.nb.mgm.service.MgmConstants.ERROR_CODE_EMPTY_CLUSTER_ROOT;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.nb.mgm.exception.MgmException;
import org.nb.mgm.handler.ArtifactHandler;
import org.nb.mgm.handler.HomeHandler;
import org.nb.mgm.handler.MachineHandler;
import org.nb.mgm.handler.MetaSectorHandler;
import org.nb.mgm.handler.MetaSpaceHandler;
import org.nb.mgm.handler.ProjectHandler;
import org.nb.mgm.handler.ProjectHomeConfigHandler;
import org.nb.mgm.handler.ProjectNodeConfigHandler;
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
import org.nb.mgm.model.runtime.ProjectHomeConfig;
import org.nb.mgm.model.runtime.ProjectNodeConfig;
import org.nb.mgm.persistence.MgmPersistenceAdapter;
import org.nb.mgm.persistence.MgmPersistenceFactory;
import org.nb.mgm.service.ManagementService;
import org.origin.common.util.PropertyUtil;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ManagementServiceImpl implements ManagementService {

	protected Logger logger = LoggerFactory.getLogger(getClass());
	protected BundleContext bundleContext;
	protected ServiceRegistration<?> serviceReg;

	// adapter for loading/saving root data model
	protected MgmPersistenceAdapter persistenceAdapter;

	// root data model
	protected ClusterRoot root;

	// data handlers
	protected MachineHandler machineHandler;
	protected HomeHandler homeHandler;
	protected MetaSectorHandler metaSectorHandler;
	protected MetaSpaceHandler metaSpaceHandler;
	protected ArtifactHandler artifactHandler;
	protected ProjectHandler projectHandler;
	protected ProjectHomeConfigHandler homeConfigHandler;
	protected ProjectNodeConfigHandler nodeConfigHandler;

	// read/write lock for data handlers
	protected ReadWriteLock machineRWLock = new ReentrantReadWriteLock();
	protected ReadWriteLock homeRWLock = new ReentrantReadWriteLock();
	protected ReadWriteLock metaSectorRWLock = new ReentrantReadWriteLock();
	protected ReadWriteLock metaSpaceRWLock = new ReentrantReadWriteLock();
	protected ReadWriteLock artifactRWLock = new ReentrantReadWriteLock();
	protected ReadWriteLock projectRWLock = new ReentrantReadWriteLock();
	protected ReadWriteLock homeConfigRWLock = new ReentrantReadWriteLock();
	protected ReadWriteLock nodeConfigRWLock = new ReentrantReadWriteLock();

	protected boolean autoSave = false;

	/**
	 * 
	 * @param bundleContext
	 */
	public ManagementServiceImpl(BundleContext bundleContext) {
		this.bundleContext = bundleContext;

		// load properties
		Map<Object, Object> props = new Hashtable<Object, Object>();
		PropertyUtil.loadProperty(this.bundleContext, props, MgmPersistenceAdapter.PERSISTENCE_TYPE);
		PropertyUtil.loadProperty(this.bundleContext, props, MgmPersistenceAdapter.PERSISTENCE_LOCAL_DIR);
		PropertyUtil.loadProperty(this.bundleContext, props, MgmPersistenceAdapter.PERSISTENCE_AUTOSAVE);

		// get persistence
		this.persistenceAdapter = MgmPersistenceFactory.createInstance(props);
	}

	/**
	 * 
	 * @param persistenceAdapter
	 */
	public void setPersistenceAdapter(MgmPersistenceAdapter persistenceAdapter) {
		this.persistenceAdapter = persistenceAdapter;
	}

	/**
	 * Start Management service
	 * 
	 * @throws MgmException
	 */
	public void start() throws MgmException {
		// 1. Load cluster data model
		this.root = this.persistenceAdapter.load();
		if (this.root == null) {
			throw new MgmException(ERROR_CODE_EMPTY_CLUSTER_ROOT, "Cluster cannot be loaded.", null);
		}

		// 2. create the data handlers
		this.machineHandler = new MachineHandler(this);
		this.homeHandler = new HomeHandler(this);
		this.metaSectorHandler = new MetaSectorHandler(this);
		this.metaSpaceHandler = new MetaSpaceHandler(this);
		this.artifactHandler = new ArtifactHandler(this);
		this.projectHandler = new ProjectHandler(this);
		this.homeConfigHandler = new ProjectHomeConfigHandler(this);
		this.nodeConfigHandler = new ProjectNodeConfigHandler(this);

		// 3. Register as a service
		Hashtable<String, Object> props = new Hashtable<String, Object>();
		this.serviceReg = this.bundleContext.registerService(ManagementService.class, this, props);
	}

	/**
	 * Stop Management service
	 * 
	 * @throws MgmException
	 */
	public void stop() throws MgmException {
		// Unregister the service
		if (this.serviceReg != null) {
			this.serviceReg.unregister();
			this.serviceReg = null;
		}
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
	// Root
	// ------------------------------------------------------------------------------------------
	public ClusterRoot getRoot() {
		return this.root;
	}

	// ------------------------------------------------------------------------------------------
	// Machine
	// ------------------------------------------------------------------------------------------
	/**
	 * Get all Machines.
	 * 
	 * @return
	 */
	@Override
	public List<Machine> getMachines() throws MgmException {
		this.machineRWLock.readLock().lock();
		try {
			return this.machineHandler.getMachines();
		} finally {
			this.machineRWLock.readLock().unlock();
		}
	}

	/**
	 * Get Machines by query.
	 * 
	 * @param query
	 * @return
	 */
	@Override
	public List<Machine> getMachines(MachineQuery query) throws MgmException {
		this.machineRWLock.readLock().lock();
		try {
			return this.machineHandler.getMachines(query);
		} finally {
			this.machineRWLock.readLock().unlock();
		}
	}

	/**
	 * Get Machine information by Id.
	 * 
	 * @param machineId
	 * @return
	 * @throws MgmException
	 */
	@Override
	public Machine getMachine(String machineId) throws MgmException {
		this.machineRWLock.readLock().lock();
		try {
			return this.machineHandler.getMachine(machineId);
		} finally {
			this.machineRWLock.readLock().unlock();
		}
	}

	/**
	 * Add a Machine to the cluster.
	 * 
	 * @param machine
	 * @throws MgmException
	 */
	@Override
	public void addMachine(Machine machine) throws MgmException {
		this.machineRWLock.writeLock().lock();
		try {
			this.machineHandler.addMachine(machine);
			if (isAutoSave()) {
				save();
			}
		} finally {
			this.machineRWLock.writeLock().unlock();
		}
	}

	/**
	 * Update Machine information.
	 * 
	 * @param machine
	 * @throws MgmException
	 */
	@Override
	public void updateMachine(Machine machine) throws MgmException {
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

	/**
	 * Delete a Machine from the cluster.
	 * 
	 * @param machineId
	 * @throws MgmException
	 */
	@Override
	public void deleteMachine(String machineId) throws MgmException {
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

	/**
	 * Get Machine properties.
	 * 
	 * @param machineId
	 * @return
	 * @throws MgmException
	 */
	public Map<String, Object> getMachineProperties(String machineId) throws MgmException {
		this.machineRWLock.readLock().lock();
		try {
			return this.machineHandler.getProperties(machineId);
		} finally {
			this.machineRWLock.readLock().unlock();
		}
	}

	/**
	 * Set Machine properties.
	 * 
	 * @param machineId
	 * @param properties
	 * @throws MgmException
	 */
	public boolean setMachineProperties(String machineId, Map<String, Object> properties) throws MgmException {
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

	/**
	 * Remove Machine properties.
	 * 
	 * @param machineId
	 * @param propNames
	 * @throws MgmException
	 */
	public boolean removeMachineProperties(String machineId, List<String> propNames) throws MgmException {
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
	/**
	 * Get all Homes in a Machine.
	 * 
	 * @param machineId
	 * @return
	 */
	@Override
	public List<Home> getHomes(String machineId) throws MgmException {
		this.homeRWLock.readLock().lock();
		try {
			return this.homeHandler.getHomes(machineId);
		} finally {
			this.homeRWLock.readLock().unlock();
		}
	}

	/**
	 * Get Homes in a Machine by query.
	 * 
	 * @param machineId
	 * @param query
	 * @return
	 */
	@Override
	public List<Home> getHomes(String machineId, HomeQuery query) throws MgmException {
		this.homeRWLock.readLock().lock();
		try {
			return this.homeHandler.getHomes(machineId, query);
		} finally {
			this.homeRWLock.readLock().unlock();
		}
	}

	/**
	 * Get Home information by Id.
	 * 
	 * @param homeId
	 * @return
	 */
	@Override
	public Home getHome(String homeId) throws MgmException {
		this.homeRWLock.readLock().lock();
		try {
			return this.homeHandler.getHome(homeId);
		} finally {
			this.homeRWLock.readLock().unlock();
		}
	}

	/**
	 * Add a Home to a Machine.
	 * 
	 * @param machineId
	 * @param home
	 * @throws MgmException
	 */
	@Override
	public void addHome(String machineId, Home home) throws MgmException {
		this.homeRWLock.writeLock().lock();
		try {
			this.homeHandler.addHome(machineId, home);
			if (isAutoSave()) {
				save();
			}
		} finally {
			this.homeRWLock.writeLock().unlock();
		}
	}

	/**
	 * Update Home information.
	 * 
	 * @param home
	 * @throws MgmException
	 */
	@Override
	public void updateHome(Home home) throws MgmException {
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

	/**
	 * Delete a Home from a Machine.
	 * 
	 * @param homeId
	 * @throws MgmException
	 */
	@Override
	public void deleteHome(String homeId) throws MgmException {
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

	/**
	 * Get Home properties.
	 * 
	 * @param homeId
	 * @return
	 * @throws MgmException
	 */
	@Override
	public Map<String, Object> getHomeProperties(String homeId) throws MgmException {
		this.homeRWLock.readLock().lock();
		try {
			return this.homeHandler.getProperties(homeId);
		} finally {
			this.homeRWLock.readLock().unlock();
		}
	}

	/**
	 * Set Home properties.
	 * 
	 * @param homeId
	 * @param properties
	 * @throws MgmException
	 */
	@Override
	public boolean setHomeProperties(String homeId, Map<String, Object> properties) throws MgmException {
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

	/**
	 * Remove Home properties.
	 * 
	 * @param homeId
	 * @param propNames
	 * @throws MgmException
	 */
	@Override
	public boolean removeHomeProperties(String homeId, List<String> propNames) throws MgmException {
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
	/**
	 * Get all MetaSectors.
	 * 
	 * @return
	 */
	@Override
	public List<MetaSector> getMetaSectors() throws MgmException {
		this.metaSectorRWLock.readLock().lock();
		try {
			return this.metaSectorHandler.getMetaSectors();
		} finally {
			this.metaSectorRWLock.readLock().unlock();
		}
	}

	/**
	 * Get MetaSectors by query.
	 * 
	 * @param query
	 * @return
	 */
	@Override
	public List<MetaSector> getMetaSectors(MetaSectorQuery query) throws MgmException {
		this.metaSectorRWLock.readLock().lock();
		try {
			return this.metaSectorHandler.getMetaSectors(query);
		} finally {
			this.metaSectorRWLock.readLock().unlock();
		}
	}

	/**
	 * Get MetaSector information by Id.
	 * 
	 * @param metaSectorId
	 * @return
	 * @throws MgmException
	 */
	@Override
	public MetaSector getMetaSector(String metaSectorId) throws MgmException {
		this.metaSectorRWLock.readLock().lock();
		try {
			return this.metaSectorHandler.getMetaSector(metaSectorId);
		} finally {
			this.metaSectorRWLock.readLock().unlock();
		}
	}

	/**
	 * Add a MetaSector to the cluster.
	 * 
	 * @param metaSector
	 * @throws MgmException
	 */
	@Override
	public void addMetaSector(MetaSector metaSector) throws MgmException {
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

	/**
	 * Update MetaSector information.
	 * 
	 * @param metaSector
	 * @throws MgmException
	 */
	@Override
	public void updateMetaSector(MetaSector metaSector) throws MgmException {
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

	/**
	 * Delete a MetaSector from the cluster.
	 * 
	 * @param metaSectorId
	 * @throws MgmException
	 */
	@Override
	public void deleteMetaSector(String metaSectorId) throws MgmException {
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
	/**
	 * Get all MetaSpaces in a MetaSector.
	 * 
	 * @param metaSectorId
	 * @return
	 */
	@Override
	public List<MetaSpace> getMetaSpaces(String metaSectorId) throws MgmException {
		this.metaSpaceRWLock.readLock().lock();
		try {
			return this.metaSpaceHandler.getMetaSpaces(metaSectorId);
		} finally {
			this.metaSpaceRWLock.readLock().unlock();
		}
	}

	/**
	 * Get MetaSpaces in a MetaSector by query.
	 * 
	 * @param metaSectorId
	 * @param query
	 * @return
	 */
	@Override
	public List<MetaSpace> getMetaSpaces(String metaSectorId, MetaSpaceQuery query) throws MgmException {
		this.metaSpaceRWLock.readLock().lock();
		try {
			return this.metaSpaceHandler.getMetaSpaces(metaSectorId, query);
		} finally {
			this.metaSpaceRWLock.readLock().unlock();
		}
	}

	/**
	 * Get MetaSpace information by Id.
	 * 
	 * @param metaSpaceId
	 * @return
	 */
	@Override
	public MetaSpace getMetaSpace(String metaSpaceId) throws MgmException {
		this.metaSpaceRWLock.readLock().lock();
		try {
			return this.metaSpaceHandler.getMetaSpace(metaSpaceId);
		} finally {
			this.metaSpaceRWLock.readLock().unlock();
		}
	}

	/**
	 * Add a MetaSpace to a MetaSector.
	 * 
	 * @param metaSectorId
	 * @param metaSpace
	 * @throws MgmException
	 */
	@Override
	public void addMetaSpace(String metaSectorId, MetaSpace metaSpace) throws MgmException {
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

	/**
	 * Update MetaSpace information.
	 * 
	 * @param metaSpace
	 * @throws MgmException
	 */
	@Override
	public void updateMetaSpace(MetaSpace metaSpace) throws MgmException {
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

	/**
	 * Delete a MetaSpace from a MetaSector.
	 * 
	 * @param metaSpaceId
	 * @throws MgmException
	 */
	@Override
	public void deleteMetaSpace(String metaSpaceId) throws MgmException {
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
	/**
	 * Get all Artifacts in a MetaSector.
	 * 
	 * @param metaSectorId
	 * @return
	 */
	@Override
	public List<Artifact> getArtifacts(String metaSectorId) throws MgmException {
		this.artifactRWLock.readLock().lock();
		try {
			return this.artifactHandler.getArtifacts(metaSectorId);
		} finally {
			this.artifactRWLock.readLock().unlock();
		}
	}

	/**
	 * Get Artifacts in a MetaSector by query.
	 * 
	 * @param metaSectorId
	 * @param query
	 * @return
	 */
	@Override
	public List<Artifact> getArtifacts(String metaSectorId, ArtifactQuery query) throws MgmException {
		this.artifactRWLock.readLock().lock();
		try {
			return this.artifactHandler.getArtifacts(metaSectorId, query);
		} finally {
			this.artifactRWLock.readLock().unlock();
		}
	}

	/**
	 * Get Artifact information by Id.
	 * 
	 * @param artifactId
	 * @return
	 */
	@Override
	public Artifact getArtifact(String artifactId) throws MgmException {
		this.artifactRWLock.readLock().lock();
		try {
			return this.artifactHandler.getArtifact(artifactId);
		} finally {
			this.artifactRWLock.readLock().unlock();
		}
	}

	/**
	 * Add a Artifact to a MetaSector.
	 * 
	 * @param metaSectorId
	 * @param artifact
	 * @throws MgmException
	 */
	@Override
	public void addArtifact(String metaSectorId, Artifact artifact) throws MgmException {
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

	/**
	 * Update Artifact information.
	 * 
	 * @param artifact
	 * @throws MgmException
	 */
	@Override
	public void updateArtifact(Artifact artifact) throws MgmException {
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

	/**
	 * Delete a Artifact from a MetaSector.
	 * 
	 * @param artifactId
	 * @throws MgmException
	 */
	@Override
	public void deleteArtifact(String artifactId) throws MgmException {
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
	public List<Project> getProjects() throws MgmException {
		this.projectRWLock.readLock().lock();
		try {
			return this.projectHandler.getProjects();
		} finally {
			this.projectRWLock.readLock().unlock();
		}
	}

	@Override
	public Project getProject(String projectId) throws MgmException {
		this.projectRWLock.readLock().lock();
		try {
			return this.projectHandler.getProject(projectId);
		} finally {
			this.projectRWLock.readLock().unlock();
		}
	}

	@Override
	public void addProject(Project project) throws MgmException {
		this.projectRWLock.writeLock().lock();
		try {
			this.projectHandler.addProject(project);
			if (isAutoSave()) {
				save();
			}
		} finally {
			this.projectRWLock.writeLock().unlock();
		}
	}

	@Override
	public void updateProject(Project project) throws MgmException {
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
	public boolean deleteProject(String projectId) throws MgmException {
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
	// ProjectHomeConfig
	// ------------------------------------------------------------------------------------------
	@Override
	public List<ProjectHomeConfig> getProjectHomeConfigs(String projectId) throws MgmException {
		this.homeConfigRWLock.readLock().lock();
		try {
			return this.homeConfigHandler.getHomeConfigs(projectId);
		} finally {
			this.homeConfigRWLock.readLock().unlock();
		}
	}

	@Override
	public ProjectHomeConfig getProjectHomeConfig(String homeConfigId) throws MgmException {
		this.homeConfigRWLock.readLock().lock();
		try {
			return this.homeConfigHandler.getHomeConfig(homeConfigId);
		} finally {
			this.homeConfigRWLock.readLock().unlock();
		}
	}

	@Override
	public void addProjectHomeConfig(String projectId, ProjectHomeConfig homeConfig) throws MgmException {
		this.homeConfigRWLock.writeLock().lock();
		try {
			this.homeConfigHandler.addHomeConfig(projectId, homeConfig);
			if (isAutoSave()) {
				save();
			}
		} finally {
			this.homeConfigRWLock.writeLock().unlock();
		}
	}

	@Override
	public void updateProjectHomeConfig(ProjectHomeConfig homeConfig) throws MgmException {
		this.homeConfigRWLock.writeLock().lock();
		try {
			this.homeConfigHandler.updateHomeConfig(homeConfig);
			if (isAutoSave()) {
				save();
			}
		} finally {
			this.homeConfigRWLock.writeLock().unlock();
		}
	}

	@Override
	public boolean deleteProjectHomeConfig(String homeConfigId) throws MgmException {
		this.homeConfigRWLock.writeLock().lock();
		try {
			// TODO:
			// 1. Send command to remote Home (if a Home is configured):
			// (1) Stop all nodes that belongs to the project in the remote home
			// (2) Delete all nodes that belongs to the project in the remote home

			// 3. Delete ProjectHomeConfig
			boolean succeed = this.homeConfigHandler.deleteHomeConfig(homeConfigId);

			// 4. Save cluster model
			if (isAutoSave()) {
				save();
			}

			return succeed;
		} finally {
			this.homeConfigRWLock.writeLock().unlock();
		}
	}

	// ------------------------------------------------------------------------------------------
	// ProjectNodeConfig
	// ------------------------------------------------------------------------------------------
	@Override
	public List<ProjectNodeConfig> getProjectNodeConfigs(String homeConfigId) throws MgmException {
		this.nodeConfigRWLock.readLock().lock();
		try {
			return this.nodeConfigHandler.getNodeConfigs(homeConfigId);
		} finally {
			this.nodeConfigRWLock.readLock().unlock();
		}
	}

	@Override
	public ProjectNodeConfig getProjectNodeConfig(String nodeConfigId) throws MgmException {
		this.nodeConfigRWLock.readLock().lock();
		try {
			return this.nodeConfigHandler.getNodeConfig(nodeConfigId);
		} finally {
			this.nodeConfigRWLock.readLock().unlock();
		}
	}

	@Override
	public void addProjectNodeConfig(String homeConfigId, ProjectNodeConfig nodeConfig) throws MgmException {
		this.nodeConfigRWLock.writeLock().lock();
		try {
			this.nodeConfigHandler.addNodeConfig(homeConfigId, nodeConfig);
			if (isAutoSave()) {
				save();
			}
		} finally {
			this.nodeConfigRWLock.writeLock().unlock();
		}
	}

	@Override
	public void updateProjectNodeConfig(ProjectNodeConfig nodeConfig) throws MgmException {
		this.nodeConfigRWLock.writeLock().lock();
		try {
			this.nodeConfigHandler.updateNodeConfig(nodeConfig);
			if (isAutoSave()) {
				save();
			}
		} finally {
			this.nodeConfigRWLock.writeLock().unlock();
		}
	}

	@Override
	public boolean deleteProjectNodeConfig(String nodeConfigId) throws MgmException {
		this.nodeConfigRWLock.writeLock().lock();
		try {
			// TODO:
			// 1. Send command to remote Home (if a Home is configured):
			// (1) Stop the node in the remote home
			// (2) Delete the node in the remote home

			// 3. Delete ProjectNodeConfig
			boolean succeed = this.nodeConfigHandler.deleteNodeConfig(nodeConfigId);

			// 4. Save cluster model
			if (isAutoSave()) {
				save();
			}

			return succeed;
		} finally {
			this.nodeConfigRWLock.writeLock().unlock();
		}
	}

}
