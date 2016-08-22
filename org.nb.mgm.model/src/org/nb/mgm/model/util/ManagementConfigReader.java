package org.nb.mgm.model.util;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.nb.mgm.model.runtime.Artifact;
import org.nb.mgm.model.runtime.ClusterRoot;
import org.nb.mgm.model.runtime.Home;
import org.nb.mgm.model.runtime.Home.HomeProxy;
import org.nb.mgm.model.runtime.Machine;
import org.nb.mgm.model.runtime.MetaSector;
import org.nb.mgm.model.runtime.MetaSpace;
import org.nb.mgm.model.runtime.Project;
import org.nb.mgm.model.runtime.ProjectHome;
import org.nb.mgm.model.runtime.ProjectNode;
import org.nb.mgm.model.runtime.Software;
import org.origin.common.json.JSONUtil;
import org.origin.common.util.DateUtil;

public class ManagementConfigReader {

	protected File file;

	public ManagementConfigReader(File file) {
		this.file = file;
	}

	/**
	 * 
	 * @return
	 */
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
	 * Convert root JSONObject to ClusterRoot.
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

		// "projects" array
		if (rootJSON.has("projects")) {
			JSONArray projectsArray = rootJSON.getJSONArray("projects");
			if (projectsArray != null) {
				int length = projectsArray.length();
				for (int i = 0; i < length; i++) {
					JSONObject projectJSON = projectsArray.getJSONObject(i);
					if (projectJSON != null) {
						Project project = jsonToProject(projectJSON);
						if (project != null) {
							project.setParent(root);
							root.addProject(project);
						}
					}
				}
			}
		}

		return root;
	}

	/**
	 * Convert machine JSONObject to Machine.
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

		// "properties" attribute
		Map<String, Object> properties = null;
		if (machineJSON.has("properties")) {
			String propertiesString = machineJSON.getString("properties");
			if (propertiesString != null) {
				properties = JSONUtil.toProperties(propertiesString);
			}
		}
		if (properties != null) {
			machine.setProperties(properties);
		}

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
	 * Convert home JSONObject to Home.
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
		// String url = null;
		// if (homeJSON.has("url")) {
		// url = homeJSON.getString("url");
		// }
		// home.setUrl(url);

		// "properties" attribute
		Map<String, Object> properties = null;
		if (homeJSON.has("properties")) {
			String propertiesString = homeJSON.getString("properties");
			if (propertiesString != null) {
				properties = JSONUtil.toProperties(propertiesString);
			}
		}
		if (properties != null) {
			home.setProperties(properties);
		}

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
	 * Convert metaSector JSONObject to MetaSector.
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
	 * Convert metaSpace JSONObject to MetaSpace.
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
	 * Convert artifact JSONObject to Artifact.
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

	/**
	 * Convert project JSONObject to Project.
	 * 
	 * @param projectJSON
	 * @return
	 */
	protected Project jsonToProject(JSONObject projectJSON) {
		if (projectJSON == null) {
			return null;
		}

		Project project = new Project();

		// "id" attribute
		String id = null;
		if (projectJSON.has("id")) {
			id = projectJSON.getString("id");
		}
		project.setId(id);

		// "name" attribute
		String name = null;
		if (projectJSON.has("name")) {
			name = projectJSON.getString("name");
		}
		project.setName(name);

		// "description" attribute
		String description = null;
		if (projectJSON.has("description")) {
			description = projectJSON.getString("description");
		}
		project.setDescription(description);

		// "homeConfigs" array
		if (projectJSON.has("homeConfigs")) {
			JSONArray homeConfigsArray = projectJSON.getJSONArray("homeConfigs");
			if (homeConfigsArray != null) {
				int length = homeConfigsArray.length();
				for (int i = 0; i < length; i++) {
					JSONObject projectHomeJSON = homeConfigsArray.getJSONObject(i);
					if (projectHomeJSON != null) {
						ProjectHome projectHome = jsonToProjectHome(projectHomeJSON);
						if (projectHome != null) {
							projectHome.setParent(project);
							project.addHome(projectHome);
						}
					}
				}
			}
		}

		// "softwareConfigs" array
		if (projectJSON.has("softwareConfigs")) {
			JSONArray softwareConfigsArray = projectJSON.getJSONArray("softwareConfigs");
			if (softwareConfigsArray != null) {
				int length = softwareConfigsArray.length();
				for (int i = 0; i < length; i++) {
					JSONObject softwareJSON = softwareConfigsArray.getJSONObject(i);
					if (softwareJSON != null) {
						Software software = jsonToSoftware(softwareJSON);
						if (software != null) {
							software.setParent(project);
							project.addSoftware(software);
						}
					}
				}
			}
		}

		return project;
	}

	/**
	 * Convert Software JSONObject to Software.
	 * 
	 * @param softwareJSON
	 * @return
	 */
	protected Software jsonToSoftware(JSONObject softwareJSON) {
		if (softwareJSON == null) {
			return null;
		}

		Software software = new Software();

		// "id" attribute
		String id = null;
		if (softwareJSON.has("id")) {
			id = softwareJSON.getString("id");
		}
		software.setId(id);

		// "type" attribute
		String type = null;
		if (softwareJSON.has("type")) {
			type = softwareJSON.getString("type");
		}
		software.setType(type);

		// "name" attribute
		String name = null;
		if (softwareJSON.has("name")) {
			name = softwareJSON.getString("name");
		}
		software.setName(name);

		// "version" attribute
		String version = null;
		if (softwareJSON.has("version")) {
			version = softwareJSON.getString("version");
		}
		software.setVersion(version);

		// "description" attribute
		String description = null;
		if (softwareJSON.has("description")) {
			description = softwareJSON.getString("description");
		}
		software.setDescription(description);

		// "length" attribute
		long length = 0;
		if (softwareJSON.has("length")) {
			try {
				// Object lengthObj = softwareJSON.get("length"); // this method returns an Integer object.
				length = softwareJSON.getLong("length");
			} catch (Exception e) {
				e.printStackTrace();
				length = 0;
			}
		}
		software.setLength(length);

		// "lastModified" attribute
		Date lastModified = null;
		if (softwareJSON.has("lastModified")) {
			try {
				String lastModifiedString = softwareJSON.getString("lastModified");
				lastModified = DateUtil.toDate(lastModifiedString, DateUtil.getCommonDateFormats());
			} catch (Exception e) {
				e.printStackTrace();
				lastModified = null;
			}
		}
		software.setLastModified(lastModified);

		// "md5" attribute
		String md5 = null;
		if (softwareJSON.has("md5")) {
			md5 = softwareJSON.getString("md5");
		}
		software.setMd5(md5);

		// "localPath" attribute
		String localPath = null;
		if (softwareJSON.has("localPath")) {
			localPath = softwareJSON.getString("localPath");
		}
		software.setLocalPath(localPath);

		// "fileName" attribute
		String fileName = null;
		if (softwareJSON.has("fileName")) {
			fileName = softwareJSON.getString("fileName");
		}
		software.setFileName(fileName);

		return software;
	}

	/**
	 * Convert ProjectHome JSONObject to ProjectHome.
	 * 
	 * @param projectHomeJSON
	 * @return
	 */
	protected ProjectHome jsonToProjectHome(JSONObject projectHomeJSON) {
		if (projectHomeJSON == null) {
			return null;
		}

		ProjectHome projectHome = new ProjectHome();

		// "id" attribute
		String id = null;
		if (projectHomeJSON.has("id")) {
			id = projectHomeJSON.getString("id");
		}
		projectHome.setId(id);

		// "name" attribute
		String name = null;
		if (projectHomeJSON.has("name")) {
			name = projectHomeJSON.getString("name");
		}
		projectHome.setName(name);

		// "description" attribute
		String description = null;
		if (projectHomeJSON.has("description")) {
			description = projectHomeJSON.getString("description");
		}
		projectHome.setDescription(description);

		// "nodeConfigs" array
		if (projectHomeJSON.has("nodeConfigs")) {
			JSONArray nodeConfigsArray = projectHomeJSON.getJSONArray("nodeConfigs");
			if (nodeConfigsArray != null) {
				int length = nodeConfigsArray.length();
				for (int i = 0; i < length; i++) {
					JSONObject projectNodeJSON = nodeConfigsArray.getJSONObject(i);
					if (projectNodeJSON != null) {
						ProjectNode projectNode = jsonToProjectNode(projectNodeJSON);
						if (projectNode != null) {
							projectNode.setParent(projectHome);
							projectHome.addNode(projectNode);
						}
					}
				}
			}
		}

		// "deploymentHome" attribute
		if (projectHomeJSON.has("deploymentHome")) {
			JSONObject deploymentHomeJSON = projectHomeJSON.getJSONObject("deploymentHome");
			{
				if (deploymentHomeJSON.has("homeId")) {
					String homeId = deploymentHomeJSON.getString("homeId");
					HomeProxy homeProxy = new HomeProxy(homeId);
					projectHome.setDeploymentHome(homeProxy);
				}
			}
		}

		return projectHome;
	}

	/**
	 * Convert ProjectNode JSONObject to ProjectNode.
	 * 
	 * @param projectNodeJSON
	 * @return
	 */
	protected ProjectNode jsonToProjectNode(JSONObject projectNodeJSON) {
		if (projectNodeJSON == null) {
			return null;
		}

		ProjectNode projectNode = new ProjectNode();

		// "id" attribute
		String id = null;
		if (projectNodeJSON.has("id")) {
			id = projectNodeJSON.getString("id");
		}
		projectNode.setId(id);

		// "name" attribute
		String name = null;
		if (projectNodeJSON.has("name")) {
			name = projectNodeJSON.getString("name");
		}
		projectNode.setName(name);

		// "description" attribute
		String description = null;
		if (projectNodeJSON.has("description")) {
			description = projectNodeJSON.getString("description");
		}
		projectNode.setDescription(description);

		// "properties" attribute
		Map<String, Object> properties = null;
		if (projectNodeJSON.has("properties")) {
			String propertiesString = projectNodeJSON.getString("properties");
			if (propertiesString != null) {
				properties = JSONUtil.toProperties(propertiesString);
			}
		}
		if (properties != null) {
			projectNode.setProperties(properties);
		}

		// "installedSoftware" attribute
		if (projectNodeJSON.has("installedSoftware")) {
			JSONArray softwareArray = projectNodeJSON.getJSONArray("installedSoftware");
			if (softwareArray != null) {
				int length = softwareArray.length();
				for (int i = 0; i < length; i++) {
					JSONObject softwareJSON = softwareArray.getJSONObject(i);
					if (softwareJSON != null) {
						// "softwareId" attribute
						String softwareId = null;
						if (softwareJSON.has("softwareId")) {
							softwareId = softwareJSON.getString("softwareId");
						}
						if (softwareId != null) {
							projectNode.addSoftwareId(softwareId);
						}
					}
				}
			}
		}

		return projectNode;
	}

}
