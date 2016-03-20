package osgi.mgm.service.impl;

import static osgi.mgm.service.MgmConstants.ERROR_CODE_EMPTY_CLUSTER_ROOT;

import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import osgi.mgm.service.MgmException;
import osgi.mgm.service.MgmService;
import osgi.mgm.service.impl.datahandler.ArtifactDataHandler;
import osgi.mgm.service.impl.datahandler.HomeDataHandler;
import osgi.mgm.service.impl.datahandler.MachineDataHandler;
import osgi.mgm.service.impl.datahandler.MetaSectorDataHandler;
import osgi.mgm.service.impl.datahandler.MetaSpaceDataHandler;
import osgi.mgm.service.impl.persistence.MgmPersistenceAdapter;
import osgi.mgm.service.impl.persistence.MgmPersistenceFactory;
import osgi.mgm.service.model.Artifact;
import osgi.mgm.service.model.ArtifactQuery;
import osgi.mgm.service.model.ClusterRoot;
import osgi.mgm.service.model.Home;
import osgi.mgm.service.model.HomeQuery;
import osgi.mgm.service.model.Machine;
import osgi.mgm.service.model.MachineQuery;
import osgi.mgm.service.model.MetaSector;
import osgi.mgm.service.model.MetaSectorQuery;
import osgi.mgm.service.model.MetaSpace;
import osgi.mgm.service.model.MetaSpaceQuery;

public class MgmServiceImpl implements MgmService {

	protected Logger logger = LoggerFactory.getLogger(getClass());
	protected BundleContext bundleContext;
	protected ServiceRegistration<?> serviceReg;

	// adapter for loading/saving root data model
	protected MgmPersistenceAdapter persistenceAdapter;

	// root data model
	protected ClusterRoot root;

	// data handlers
	protected MachineDataHandler machineDataHandler;
	protected HomeDataHandler homeDataHandler;
	protected MetaSectorDataHandler metaSectorDataHandler;
	protected MetaSpaceDataHandler metaSpaceDataHandler;
	protected ArtifactDataHandler artifactDataHandler;

	// read/write lock for data handlers
	protected ReadWriteLock machineRWLock = new ReentrantReadWriteLock();
	protected ReadWriteLock homeRWLock = new ReentrantReadWriteLock();
	protected ReadWriteLock metaSectorRWLock = new ReentrantReadWriteLock();
	protected ReadWriteLock metaSpaceRWLock = new ReentrantReadWriteLock();
	protected ReadWriteLock artifactRWLock = new ReentrantReadWriteLock();

	/**
	 * 
	 * @param bundleContext
	 */
	public MgmServiceImpl(BundleContext bundleContext) {
		this.bundleContext = bundleContext;

		// load properties
		Hashtable<String, Object> props = new Hashtable<String, Object>();
		set(props, MgmPersistenceAdapter.PERSISTENCE_TYPE);
		set(props, MgmPersistenceAdapter.PERSISTENCE_LOCAL_DIR);

		// get persistence
		this.persistenceAdapter = MgmPersistenceFactory.createInstance(props);
	}

	/**
	 * 
	 * @param props
	 * @param key
	 */
	protected void set(Hashtable<String, Object> props, String key) {
		String value = this.bundleContext.getProperty(key);
		if (value == null) {
			value = System.getProperty(key);
			if (value == null) {
				return;
			}
		}
		props.put(key, value);
	}

	/**
	 * 
	 * @param persistenceAdapter
	 */
	public void setPersistenceAdapter(MgmPersistenceAdapter persistenceAdapter) {
		this.persistenceAdapter = persistenceAdapter;
	}

	/**
	 * Start MGM service
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
		this.machineDataHandler = new MachineDataHandler(this);
		this.homeDataHandler = new HomeDataHandler(this);
		this.metaSectorDataHandler = new MetaSectorDataHandler(this);
		this.metaSpaceDataHandler = new MetaSpaceDataHandler(this);
		this.artifactDataHandler = new ArtifactDataHandler(this);

		// 3. Register as a service
		Hashtable<String, Object> props = new Hashtable<String, Object>();
		this.serviceReg = this.bundleContext.registerService(MgmService.class, this, props);
	}

	/**
	 * Stop MGM service
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
			return this.machineDataHandler.getMachines();
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
			return this.machineDataHandler.getMachines(query);
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
			return this.machineDataHandler.getMachine(machineId);
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
			this.machineDataHandler.addMachine(machine);
			this.persistenceAdapter.save(this.root);
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
			this.machineDataHandler.updateMachine(machine);
			this.persistenceAdapter.save(this.root);
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
			this.machineDataHandler.deleteMachine(machineId);
			this.persistenceAdapter.save(this.root);
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
			return this.homeDataHandler.getHomes(machineId);
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
			return this.homeDataHandler.getHomes(machineId, query);
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
			return this.homeDataHandler.getHome(homeId);
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
			this.homeDataHandler.addHome(machineId, home);
			this.persistenceAdapter.save(this.root);
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
			this.homeDataHandler.updateHome(home);
			this.persistenceAdapter.save(this.root);
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
			this.homeDataHandler.deleteHome(homeId);

			// 4. Save cluster model
			this.persistenceAdapter.save(this.root);
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
			return this.metaSectorDataHandler.getMetaSectors();
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
			return this.metaSectorDataHandler.getMetaSectors(query);
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
			return this.metaSectorDataHandler.getMetaSector(metaSectorId);
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
			this.metaSectorDataHandler.addMetaSector(metaSector);
			this.persistenceAdapter.save(this.root);
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
			this.metaSectorDataHandler.updateMetaSector(metaSector);
			this.persistenceAdapter.save(this.root);
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
			this.metaSectorDataHandler.deleteMetaSector(metaSectorId);
			this.persistenceAdapter.save(this.root);
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
			return this.metaSpaceDataHandler.getMetaSpaces(metaSectorId);
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
			return this.metaSpaceDataHandler.getMetaSpaces(metaSectorId, query);
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
			return this.metaSpaceDataHandler.getMetaSpace(metaSpaceId);
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
			this.metaSpaceDataHandler.addMetaSpace(metaSectorId, metaSpace);
			this.persistenceAdapter.save(this.root);
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
			this.metaSpaceDataHandler.updateMetaSpace(metaSpace);
			this.persistenceAdapter.save(this.root);
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
			this.metaSpaceDataHandler.deleteMetaSpace(metaSpaceId);
			this.persistenceAdapter.save(this.root);
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
			return this.artifactDataHandler.getArtifacts(metaSectorId);
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
			return this.artifactDataHandler.getArtifacts(metaSectorId, query);
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
			return this.artifactDataHandler.getArtifact(artifactId);
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
			this.artifactDataHandler.addArtifact(metaSectorId, artifact);
			this.persistenceAdapter.save(this.root);
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
			this.artifactDataHandler.updateArtifact(artifact);
			this.persistenceAdapter.save(this.root);
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
			this.artifactDataHandler.deleteArtifact(artifactId);
			this.persistenceAdapter.save(this.root);
		} finally {
			this.artifactRWLock.writeLock().unlock();
		}
	}

}
