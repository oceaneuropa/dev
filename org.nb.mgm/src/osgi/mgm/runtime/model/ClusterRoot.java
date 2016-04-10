package osgi.mgm.runtime.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/*
 * Cluster Root model
 * 
 * Host   										There can be (0,n) Hosts
 * --> Home (0,n)								Each Host contains (0,n) Homes
 *     --> Joined Sector (0,n)					Each Home can join (0,n) Sectors; 
 *     											The Sectors must exist;
 * 
 *     --> Joined Sector - Space (0,n)			Each Home can join (0,n) Spaces; 
 *     											The Spaces must exist; 
 *     											The Spaces must belong to joined Sectors (or ensure the belonged Sector to join the Home); 
 * 
 *         --> Node (0, n)						There can be (0,n) Nodes in joined Space;
 *         										Can start/stop;
 *
 * Sector										There can be (0,n) Sectors;
 * --> Artifact	(0,n)							There can be (0,n) Artifacts; 
 * 												Can upload/delete to Sector;
 * 
 * --> Space									There can be (0,n) Spaces; 
 * 												Can deploy Artifacts to Space (deploy to all Homes which joined the Space); 
 * 												Can start/stop (which will start/stop all Nodes in Homes which joined the Space); 
 * 
 * 
 * ---------------------------------------------------------------------------------------------------------------------------------------------------------------------
 * 												SectorM															SectorN
 * 												(Uploaded: Atrifact11, Artifact12...)							(Uploaded: Atrifact21, Atrifact22...)
 * 
 * 								SpaceM1							SpaceM2							SpaceN1							SpaceN2		
 * 								(Deployed: Atrifact11)			(Deployed: Artifact12)			(Deployed: Atrifact21)			(Deployed: Artifact22)
 * --------------------------------------------------------------------------------------------------------------------------------------------------------------------- 
 *  MachineX
 *  		HomeX1				Join: SectorM/SpaceM1
 *  
 *  		HomeX2				Join: SectorM/SpaceM1
 *  
 *  MachineY
 *  		HomeY1				Join: SectorM/SpaceM1			Join: SectorM/SpaceM2
 *  
 *  		HomeY2				Join: SectorM/SpaceM1			Join: SectorM/SpaceM2
 *  
 *  MachineZ
 *  		HomeZ1												Join: SectorM/SpaceM2
 *  
 *  		HomeZ2												Join: SectorM/SpaceM2
 *  
 *  ---------------------------------------------------------------------------------------------------------------------------------------------------------------------
 *  
 */
public class ClusterRoot {

	protected List<Machine> machines = new ArrayList<Machine>();
	protected List<MetaSector> metaSectors = new ArrayList<MetaSector>();

	public ClusterRoot() {
	}

	// ----------------------------------------------------------------------------------------------------------------
	// Machine
	// ----------------------------------------------------------------------------------------------------------------
	public List<Machine> getMachines() {
		return this.machines;
	}

	public void addMachine(Machine machine) {
		if (machine != null && !this.machines.contains(machine)) {
			this.machines.add(machine);
		}
	}

	public void deleteMachine(Machine machine) {
		if (machine != null && this.machines.contains(machine)) {
			this.machines.remove(machine);
		}
	}

	// ----------------------------------------------------------------------------------------------------------------
	// MetaSector
	// ----------------------------------------------------------------------------------------------------------------
	public List<MetaSector> getMetaSectors() {
		return this.metaSectors;
	}

	public void addMetaSector(MetaSector metaSector) {
		if (metaSector != null && !this.metaSectors.contains(metaSector)) {
			this.metaSectors.add(metaSector);
		}
	}

	public void deleteMetaSector(MetaSector metaSector) {
		if (metaSector != null && this.metaSectors.contains(metaSector)) {
			this.metaSectors.remove(metaSector);
		}
	}

	// ----------------------------------------------------------------------------------------------------------------
	// Methods to resolve model by id
	// ----------------------------------------------------------------------------------------------------------------
	public Home getResolveHome(String homeId) {
		Home home = null;
		for (Iterator<Machine> hostItor = getMachines().iterator(); hostItor.hasNext();) {
			Machine currHost = hostItor.next();
			for (Iterator<Home> homeItor = currHost.getHomes().iterator(); homeItor.hasNext();) {
				Home currHome = homeItor.next();
				if (currHome.getId().equals(homeId)) {
					home = currHome;
					break;
				}
			}
			if (home != null) {
				break;
			}
		}
		return home;
	}

	public MetaSector getResolveMetaSector(String metaSectorId) {
		MetaSector metaSector = null;
		for (Iterator<MetaSector> sectorItor = getMetaSectors().iterator(); sectorItor.hasNext();) {
			MetaSector currMetaSector = sectorItor.next();
			if (currMetaSector.getId().equals(metaSectorId)) {
				metaSector = currMetaSector;
				break;
			}
		}
		return metaSector;
	}

}
