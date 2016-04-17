package org.nb.mgm.persistence;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.nb.common.util.JSONUtil;
import org.nb.mgm.model.Artifact;
import org.nb.mgm.model.ClusterRoot;
import org.nb.mgm.model.Home;
import org.nb.mgm.model.Machine;
import org.nb.mgm.model.MetaSector;
import org.nb.mgm.model.MetaSpace;

public class LocalMgmWriter {

	protected File file;
	protected ClusterRoot root;

	/**
	 * 
	 * @param file
	 * @param root
	 */
	public LocalMgmWriter(File file, ClusterRoot root) {
		this.file = file;
		this.root = root;
	}

	public void write() throws IOException {
		JSONObject rootJSON = rootToJSON(this.root);
		if (rootJSON != null) {
			JSONUtil.save(file, rootJSON);
		}
	}

	/**
	 * Convert ClusterRoot model to JSONObject
	 * 
	 * @param root
	 * @return
	 */
	protected JSONObject rootToJSON(ClusterRoot root) {
		if (root == null) {
			return null;
		}

		JSONObject rootJSON = new JSONObject();

		// "machines" array
		JSONArray machinesArray = new JSONArray();
		{
			List<Machine> machines = root.getMachines();
			int machineIndex = 0;
			for (Iterator<Machine> machineItor = machines.iterator(); machineItor.hasNext();) {
				Machine machine = machineItor.next();
				JSONObject machineJSON = machineToJSON(machine);
				if (machineJSON != null) {
					machinesArray.put(machineIndex++, machineJSON);
				}
			}
		}
		rootJSON.put("machines", machinesArray);

		// "metaSectors" array
		JSONArray metaSectorsArray = new JSONArray();
		{
			List<MetaSector> metaSectors = root.getMetaSectors();
			int metaSectorIndex = 0;
			for (Iterator<MetaSector> metaSectorItor = metaSectors.iterator(); metaSectorItor.hasNext();) {
				MetaSector metaSector = metaSectorItor.next();
				JSONObject metaSectorJSON = metaSectorToJSON(metaSector);
				if (metaSectorJSON != null) {
					metaSectorsArray.put(metaSectorIndex++, metaSectorJSON);
				}
			}
		}
		rootJSON.put("metaSectors", metaSectorsArray);

		return rootJSON;
	}

	/**
	 * Convert Machine model to JSONObject
	 * 
	 * @param machine
	 * @return
	 */
	protected JSONObject machineToJSON(Machine machine) {
		if (machine == null) {
			return null;
		}
		JSONObject machineJSON = new JSONObject();

		// "id" attribute
		String id = machine.getId();
		if (id != null) {
			machineJSON.put("id", id);
		}

		// "name" attribute
		String name = machine.getName();
		if (name != null) {
			machineJSON.put("name", name);
		}

		// "description" attribute
		String description = machine.getDescription();
		if (description != null) {
			machineJSON.put("description", description);
		}

		// "ipAddress" attribute
		String ipAddress = machine.getIpAddress();
		if (ipAddress != null) {
			machineJSON.put("ipAddress", ipAddress);
		}

		// "homes" attribute
		JSONArray homesArray = new JSONArray();
		{
			List<Home> homes = machine.getHomes();
			int homeIndex = 0;
			for (Iterator<Home> homeItor = homes.iterator(); homeItor.hasNext();) {
				Home home = homeItor.next();
				JSONObject homeJSON = homeToJSON(home);
				if (homeJSON != null) {
					homesArray.put(homeIndex++, homeJSON);
				}
			}
		}
		machineJSON.put("homes", homesArray);

		return machineJSON;
	}

	/**
	 * Convert Home model to JSONObject
	 * 
	 * @param home
	 * @return
	 */
	protected JSONObject homeToJSON(Home home) {
		if (home == null) {
			return null;
		}

		JSONObject homeJSON = new JSONObject();

		// "id" attribute
		String id = home.getId();
		if (id != null) {
			homeJSON.put("id", id);
		}

		// "name" attribute
		String name = home.getName();
		if (name != null) {
			homeJSON.put("name", name);
		}

		// "description" attribute
		String description = home.getDescription();
		if (description != null) {
			homeJSON.put("description", description);
		}

		// "url" attribute
		String url = home.getUrl();
		if (url != null) {
			homeJSON.put("url", url);
		}

		// "joinedMetaSectorIds" array
		List<String> joinedMetaSectorIds = home.getJoinedMetaSectorIds();
		if (joinedMetaSectorIds != null && !joinedMetaSectorIds.isEmpty()) {
			homeJSON.put("joinedMetaSectorIds", joinedMetaSectorIds);
		}

		// "joinedMetaSpaceIds" array
		List<String> joinedMetaSpaceIds = home.getJoinedMetaSpaceIds();
		if (joinedMetaSpaceIds != null && !joinedMetaSpaceIds.isEmpty()) {
			homeJSON.put("joinedMetaSpaceIds", joinedMetaSpaceIds);
		}

		return homeJSON;
	}

	/**
	 * Convert MetaSector model to JSONObject
	 * 
	 * @param metaSector
	 * @return
	 */
	protected JSONObject metaSectorToJSON(MetaSector metaSector) {
		if (metaSector == null) {
			return null;
		}
		JSONObject metaSectorJSON = new JSONObject();

		// "id" attribute
		String id = metaSector.getId();
		if (id != null) {
			metaSectorJSON.put("id", id);
		}

		// "name" attribute
		String name = metaSector.getName();
		if (name != null) {
			metaSectorJSON.put("name", name);
		}

		// "description" attribute
		String description = metaSector.getDescription();
		if (description != null) {
			metaSectorJSON.put("description", description);
		}

		// "metaSpaces" array
		JSONArray metaSpacesArray = new JSONArray();
		{
			List<MetaSpace> metaSpaces = metaSector.getMetaSpaces();
			int metaSpaceIndex = 0;
			for (Iterator<MetaSpace> metaSpaceItor = metaSpaces.iterator(); metaSpaceItor.hasNext();) {
				MetaSpace metaSpace = metaSpaceItor.next();
				JSONObject metaSpaceJSON = metaSpaceToJSON(metaSpace);
				if (metaSpaceJSON != null) {
					metaSpacesArray.put(metaSpaceIndex++, metaSpaceJSON);
				}
			}
		}
		metaSectorJSON.put("metaSpaces", metaSpacesArray);

		// "artifacts" array
		JSONArray artifactsArray = new JSONArray();
		{
			List<Artifact> artifacts = metaSector.getArtifacts();
			int artifactIndex = 0;
			for (Iterator<Artifact> artifactItor = artifacts.iterator(); artifactItor.hasNext();) {
				Artifact artifact = artifactItor.next();
				JSONObject artifactJSON = artifactToJSON(artifact);
				if (artifactJSON != null) {
					artifactsArray.put(artifactIndex++, artifactJSON);
				}
			}
		}
		metaSectorJSON.put("artifacts", artifactsArray);

		return metaSectorJSON;
	}

	/**
	 * Convert MetaSpace model to JSONObject
	 * 
	 * @param metaSpace
	 * @return
	 */
	protected JSONObject metaSpaceToJSON(MetaSpace metaSpace) {
		if (metaSpace == null) {
			return null;
		}
		JSONObject metaSpaceJSON = new JSONObject();

		// "id" attribute
		String id = metaSpace.getId();
		if (id != null) {
			metaSpaceJSON.put("id", id);
		}

		// "name" attribute
		String name = metaSpace.getName();
		if (name != null) {
			metaSpaceJSON.put("name", name);
		}

		// "description" attribute
		String description = metaSpace.getDescription();
		if (description != null) {
			metaSpaceJSON.put("description", description);
		}

		// "deployedArtifactIds" array
		List<String> deployedArtifactIds = metaSpace.getDeployedArtifactIds();
		if (deployedArtifactIds != null && !deployedArtifactIds.isEmpty()) {
			metaSpaceJSON.put("deployedArtifactIds", deployedArtifactIds);
		}

		return metaSpaceJSON;
	}

	/**
	 * Convert Artifact model to JSONObject
	 * 
	 * @param artifact
	 * @return
	 */
	protected JSONObject artifactToJSON(Artifact artifact) {
		if (artifact == null) {
			return null;
		}

		JSONObject artifactJSON = new JSONObject();

		// "id" attribute
		String id = artifact.getId();
		if (id != null) {
			artifactJSON.put("id", id);
		}

		// "name" attribute
		String name = artifact.getName();
		if (name != null) {
			artifactJSON.put("name", name);
		}

		// "description" attribute
		String description = artifact.getDescription();
		if (description != null) {
			artifactJSON.put("description", description);
		}

		// "filePath" attribute
		String filePath = artifact.getFilePath();
		if (filePath != null) {
			artifactJSON.put("filePath", filePath);
		}

		// "fileName" attribute
		String fileName = artifact.getFileName();
		if (fileName != null) {
			artifactJSON.put("fileName", fileName);
		}

		// "fileSize" attribute
		long fileSize = artifact.getFileSize();
		artifactJSON.put("fileSize", fileSize);

		// "checksum" attribute
		long checksum = artifact.getChecksum();
		artifactJSON.put("checksum", checksum);

		return artifactJSON;
	}

}
