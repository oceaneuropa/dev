package osgi.mgm.service.impl.persistence;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import osgi.mgm.common.util.JSONUtil;
import osgi.mgm.service.model.Artifact;
import osgi.mgm.service.model.ClusterRoot;
import osgi.mgm.service.model.Home;
import osgi.mgm.service.model.Machine;
import osgi.mgm.service.model.MetaSector;
import osgi.mgm.service.model.MetaSpace;

public class LocalMgmReader {

	protected File file;

	public LocalMgmReader(File file) {
		this.file = file;
	}

	public ClusterRoot read() {
		ClusterRoot root = null;
		try {
			JSONObject rootJSON = JSONUtil.load(this.file);
			root = jsonToRoot(rootJSON);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return root;
	}

	/**
	 * Convert root JSONObject to ClusterRoot model.
	 * 
	 * @param rootJSON
	 * @return
	 */
	protected ClusterRoot jsonToRoot(JSONObject rootJSON) {
		if (rootJSON == null) {
			return null;
		}

		ClusterRoot root = new ClusterRoot();

		// "machines" array
		if (rootJSON.has("machines")) {
			JSONArray machinesArray = rootJSON.getJSONArray("machines");
			if (machinesArray != null) {
				int length = machinesArray.length();
				for (int i = 0; i < length; i++) {
					JSONObject machineJSON = machinesArray.getJSONObject(i);
					if (machineJSON != null) {
						Machine machine = jsonToMachine(machineJSON);
						if (machine != null) {
							machine.setParent(root);
							root.addMachine(machine);
						}
					}
				}
			}
		}

		// "metaSectors" array
		if (rootJSON.has("metaSectors")) {
			JSONArray metaSectorsArray = rootJSON.getJSONArray("metaSectors");
			if (metaSectorsArray != null) {
				int length = metaSectorsArray.length();
				for (int i = 0; i < length; i++) {
					JSONObject metaSectorJSON = metaSectorsArray.getJSONObject(i);
					if (metaSectorJSON != null) {
						MetaSector metaSector = jsonToMetaSector(metaSectorJSON);
						if (metaSector != null) {
							metaSector.setParent(root);
							root.addMetaSector(metaSector);
						}
					}
				}
			}
		}

		return root;
	}

	/**
	 * Convert machine JSONObject to Machine model.
	 * 
	 * @param machineJSON
	 * @return
	 */
	protected Machine jsonToMachine(JSONObject machineJSON) {
		if (machineJSON == null) {
			return null;
		}

		Machine machine = new Machine();

		// "id" attribute
		String id = null;
		if (machineJSON.has("id")) {
			id = machineJSON.getString("id");
		}
		machine.setId(id);

		// "name" attribute
		String name = null;
		if (machineJSON.has("name")) {
			name = machineJSON.getString("name");
		}
		machine.setName(name);

		// "description" attribute
		String description = null;
		if (machineJSON.has("description")) {
			description = machineJSON.getString("description");
		}
		machine.setDescription(description);

		// "ipAddress" attribute
		String ipAddress = null;
		if (machineJSON.has("ipAddress")) {
			ipAddress = machineJSON.getString("ipAddress");
		}
		machine.setIpAddress(ipAddress);

		// "homes" array
		if (machineJSON.has("homes")) {
			JSONArray homesArray = machineJSON.getJSONArray("homes");
			if (homesArray != null) {
				int length = homesArray.length();
				for (int i = 0; i < length; i++) {
					JSONObject homeJSON = homesArray.getJSONObject(i);
					if (homeJSON != null) {
						Home home = jsonToHome(homeJSON);
						if (home != null) {
							home.setParent(machine);
							machine.addHome(home);
						}
					}
				}
			}
		}

		return machine;
	}

	/**
	 * Convert home JSONObject to Home model.
	 * 
	 * @param homeJSON
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected Home jsonToHome(JSONObject homeJSON) {
		if (homeJSON == null) {
			return null;
		}

		Home home = new Home();

		// "id" attribute
		String id = null;
		if (homeJSON.has("id")) {
			id = homeJSON.getString("id");
		}
		home.setId(id);

		// "name" attribute
		String name = null;
		if (homeJSON.has("name")) {
			name = homeJSON.getString("name");
		}
		home.setName(name);

		// "description" attribute
		String description = null;
		if (homeJSON.has("description")) {
			description = homeJSON.getString("description");
		}
		home.setDescription(description);

		// "url" attribute
		String url = null;
		if (homeJSON.has("url")) {
			url = homeJSON.getString("url");
		}
		home.setUrl(url);

		// "joinedMetaSectorIds" array
		if (homeJSON.has("joinedMetaSectorIds")) {
			Object joinedMetaSectorIdsObj = homeJSON.get("joinedMetaSectorIds");
			if (joinedMetaSectorIdsObj instanceof List) {
				home.setJoinedMetaSectorIds((List<String>) joinedMetaSectorIdsObj);
			}
		}

		// "joinedMetaSpaceIds" array
		if (homeJSON.has("joinedMetaSpaceIds")) {
			Object joinedMetaSpaceIdsObj = homeJSON.get("joinedMetaSpaceIds");
			if (joinedMetaSpaceIdsObj instanceof List) {
				home.setJoinedMetaSpaceIds((List<String>) joinedMetaSpaceIdsObj);
			}
		}

		return home;
	}

	/**
	 * Convert metaSector JSONObject to MetaSector model.
	 * 
	 * @param metaSectorJSON
	 * @return
	 */
	protected MetaSector jsonToMetaSector(JSONObject metaSectorJSON) {
		if (metaSectorJSON == null) {
			return null;
		}

		MetaSector metaSector = new MetaSector();

		// "id" attribute
		String id = null;
		if (metaSectorJSON.has("id")) {
			id = metaSectorJSON.getString("id");
		}
		metaSector.setId(id);

		// "name" attribute
		String name = null;
		if (metaSectorJSON.has("name")) {
			name = metaSectorJSON.getString("name");
		}
		metaSector.setName(name);

		// "description" attribute
		String description = null;
		if (metaSectorJSON.has("description")) {
			description = metaSectorJSON.getString("description");
		}
		metaSector.setDescription(description);

		// "metaSpaces" array
		if (metaSectorJSON.has("metaSpaces")) {
			JSONArray metaSpacesArray = metaSectorJSON.getJSONArray("metaSpaces");
			if (metaSpacesArray != null) {
				int length = metaSpacesArray.length();
				for (int i = 0; i < length; i++) {
					JSONObject metaSpaceJSON = metaSpacesArray.getJSONObject(i);
					if (metaSpaceJSON != null) {
						MetaSpace metaSpace = jsonToMetaSpace(metaSpaceJSON);
						if (metaSpace != null) {
							metaSpace.setParent(metaSector);
							metaSector.addMetaSpace(metaSpace);
						}
					}
				}
			}
		}

		// "artifacts" array
		if (metaSectorJSON.has("artifacts")) {
			JSONArray artifactsArray = metaSectorJSON.getJSONArray("artifacts");
			if (artifactsArray != null) {
				int length = artifactsArray.length();
				for (int i = 0; i < length; i++) {
					JSONObject artifactJSON = artifactsArray.getJSONObject(i);
					if (artifactJSON != null) {
						Artifact artifact = jsonToArtifact(artifactJSON);
						if (artifact != null) {
							artifact.setParent(metaSector);
							metaSector.addArtifact(artifact);
						}
					}
				}
			}
		}

		return metaSector;
	}

	/**
	 * Convert metaSpace JSONObject to MetaSpace model.
	 * 
	 * @param metaSectorJSON
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected MetaSpace jsonToMetaSpace(JSONObject metaSpaceJSON) {
		if (metaSpaceJSON == null) {
			return null;
		}

		MetaSpace metaSpace = new MetaSpace();

		// "id" attribute
		String id = null;
		if (metaSpaceJSON.has("id")) {
			id = metaSpaceJSON.getString("id");
		}
		metaSpace.setId(id);

		// "name" attribute
		String name = null;
		if (metaSpaceJSON.has("name")) {
			name = metaSpaceJSON.getString("name");
		}
		metaSpace.setName(name);

		// "description" attribute
		String description = null;
		if (metaSpaceJSON.has("description")) {
			description = metaSpaceJSON.getString("description");
		}
		metaSpace.setDescription(description);

		// "deployedArtifactIds" array
		if (metaSpaceJSON.has("deployedArtifactIds")) {
			Object deployedArtifactIdsObj = metaSpaceJSON.get("deployedArtifactIds");
			if (deployedArtifactIdsObj instanceof List) {
				metaSpace.setDeployedArtifactIds((List<String>) deployedArtifactIdsObj);
			}
		}

		return metaSpace;
	}

	/**
	 * Convert artifact JSONObject to Artifact model.
	 * 
	 * @param artifactJSON
	 * @return
	 */
	protected Artifact jsonToArtifact(JSONObject artifactJSON) {
		if (artifactJSON == null) {
			return null;
		}

		Artifact artifact = new Artifact();

		// "id" attribute
		String id = null;
		if (artifactJSON.has("id")) {
			id = artifactJSON.getString("id");
		}
		artifact.setId(id);

		// "name" attribute
		String name = null;
		if (artifactJSON.has("name")) {
			name = artifactJSON.getString("name");
		}
		artifact.setName(name);

		// "description" attribute
		String description = null;
		if (artifactJSON.has("description")) {
			description = artifactJSON.getString("description");
		}
		artifact.setDescription(description);

		// "fileName" attribute
		String fileName = null;
		if (artifactJSON.has("fileName")) {
			fileName = artifactJSON.getString("fileName");
		}
		artifact.setFileName(fileName);

		// "filePath" attribute
		String filePath = null;
		if (artifactJSON.has("filePath")) {
			filePath = artifactJSON.getString("filePath");
		}
		artifact.setFilePath(filePath);

		// "fileSize" attribute
		long fileSize = 0;
		if (artifactJSON.has("fileSize")) {
			fileSize = artifactJSON.getLong("fileSize");
		}
		artifact.setFileSize(fileSize);

		// "checksum" attribute
		long checksum = 0;
		if (artifactJSON.has("checksum")) {
			checksum = artifactJSON.getLong("checksum");
		}
		artifact.setChecksum(checksum);

		return artifact;
	}

}
