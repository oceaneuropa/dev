package org.nb.home.service;

/*

/home1
	/bin
		startHome.sh

	config.ini
		management.host=127.0.0.1:9090

	metasectors/
		sector1/
		
		
		/sector2
		
 	
 		/sector3
 
 
 * 
 */
public interface HomeService {

	/**
	 * Ping the Home service
	 * 
	 * @return
	 */
	public int ping();

	/**
	 * Get Home status.
	 * 
	 * @return
	 */
	public String getStatus();

	// ------------------------------------------------------------------------------------------
	// MetaSector
	// ------------------------------------------------------------------------------------------
	public void createSectorFolder(String sectorName);

}
