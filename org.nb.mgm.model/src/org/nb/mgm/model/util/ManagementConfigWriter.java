package org.nb.mgm.model.util;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
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
import org.origin.common.json.JSONUtil;
import org.origin.common.util.DateUtil;

public class ManagementConfigWriter {

	protected File file;
	protected ClusterRoot root;

	/**
	 * 
	 * @param file
	 * @param root
	 */
	public ManagementConfigWriter(File file, ClusterRoot root) {
		this.file = file;
		this.root = root;
	}

	/**
	 * 
	 * @throws IOException
	 */
	public void write() throws IOException {
		JSONObject rootJSON = rootToJSON(this.root);
		if (rootJSON != null) {
			JSONUtil.save(rootJSON, file);
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

		// "projects" array
		JSONArray projectsArray = new JSONArray();
		{
			List<Project> projects = root.getProjects();
			int projectIndex = 0;
			for (Iterator<Project> projectItor = projects.iterator(); projectItor.hasNext();) {
				Project project = projectItor.next();
				JSONObject projectJSON = projectToJSON(project);
				if (projectJSON != null) {
					projectsArray.put(projectIndex++, projectJSON);
				}
			}
		}
		rootJSON.put("projects", projectsArray);

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

		// "properties" attribute
		Map<String, Object> properties = machine.getProperties();
		String propertiesString = JSONUtil.toJsonString(properties);
		if (propertiesString != null) {
			machineJSON.put("properties", propertiesString);
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
		// String url = home.getUrl();
		// if (url != null) {
		// homeJSON.put("url", url);
		// }

		// "properties" attribute
		Map<String, Object> properties = home.getProperties();
		String propertiesString = JSONUtil.toJsonString(properties);
		if (propertiesString != null) {
			homeJSON.put("properties", propertiesString);
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

	/**
	 * Convert Project model to JSONObject
	 * 
	 * @param project
	 * @return
	 */
	protected JSONObject projectToJSON(Project project) {
		if (project == null) {
			return null;
		}
		JSONObject projectJSON = new JSONObject();

		// "id" attribute
		String id = project.getId();
		if (id != null) {
			projectJSON.put("id", id);
		}

		// "name" attribute
		String name = project.getName();
		if (name != null) {
			projectJSON.put("name", name);
		}

		// "description" attribute
		String description = project.getDescription();
		if (description != null) {
			projectJSON.put("description", description);
		}

		// "homeConfigs" attribute
		JSONArray homeConfigsArray = new JSONArray();
		{
			List<ProjectHome> projectHomes = project.getHomes();
			int homeConfigIndex = 0;
			for (Iterator<ProjectHome> projectHomeItor = projectHomes.iterator(); projectHomeItor.hasNext();) {
				ProjectHome projectHome = projectHomeItor.next();
				JSONObject projectHomeJSON = projectHomeToJSON(projectHome);
				if (projectHomeJSON != null) {
					homeConfigsArray.put(homeConfigIndex++, projectHomeJSON);
				}
			}
		}
		projectJSON.put("homeConfigs", homeConfigsArray);

		// "softwareConfigs" attribute
		JSONArray softwareConfigsArray = new JSONArray();
		{
			int softwareIndex = 0;
			for (Iterator<Software> softwareItor = project.getSoftware().iterator(); softwareItor.hasNext();) {
				Software software = softwareItor.next();
				JSONObject softwareJSON = softwareToJSON(software);
				if (softwareJSON != null) {
					softwareConfigsArray.put(softwareIndex++, softwareJSON);
				}
			}
		}
		projectJSON.put("softwareConfigs", softwareConfigsArray);

		return projectJSON;
	}

	/**
	 * Convert ProjectHome model to JSONObject
	 * 
	 * @param projectHome
	 * @return
	 */
	protected JSONObject projectHomeToJSON(ProjectHome projectHome) {
		if (projectHome == null) {
			return null;
		}

		JSONObject projectHomeJSON = new JSONObject();

		// "id" attribute
		String id = projectHome.getId();
		if (id != null) {
			projectHomeJSON.put("id", id);
		}

		// "name" attribute
		String name = projectHome.getName();
		if (name != null) {
			projectHomeJSON.put("name", name);
		}

		// "description" attribute
		String description = projectHome.getDescription();
		if (description != null) {
			projectHomeJSON.put("description", description);
		}

		// "properties" attribute
		// Map<String, Object> properties = homeConfig.getProperties();
		// String propertiesString = JSONUtil.toJsonString(properties);
		// if (propertiesString != null) {
		// homeJSON.put("properties", propertiesString);
		// }

		// "nodeConfigs" attribute
		JSONArray nodeConfigsArray = new JSONArray();
		{
			List<ProjectNode> projectNodes = projectHome.getNodes();
			int nodeConfigIndex = 0;
			for (Iterator<ProjectNode> projectNodeItor = projectNodes.iterator(); projectNodeItor.hasNext();) {
				ProjectNode projectNode = projectNodeItor.next();
				JSONObject projectNodeJSON = projectNodeToJSON(projectNode);
				if (projectNodeJSON != null) {
					nodeConfigsArray.put(nodeConfigIndex++, projectNodeJSON);
				}
			}
		}
		projectHomeJSON.put("nodeConfigs", nodeConfigsArray);

		// "deploymentHome" attribute
		Home home = projectHome.getDeploymentHome();
		if (home != null) {
			String homeId = home.getId();

			JSONObject deploymentHomeJSON = new JSONObject();
			{
				deploymentHomeJSON.put("homeId", homeId);
			}
			projectHomeJSON.put("deploymentHome", deploymentHomeJSON);
		}

		return projectHomeJSON;
	}

	/**
	 * Convert Software model to JSONObject
	 * 
	 * @param software
	 * @return
	 */
	protected JSONObject softwareToJSON(Software software) {
		if (software == null) {
			return null;
		}

		JSONObject softwareJSON = new JSONObject();

		// "id" attribute
		String id = software.getId();
		if (id != null) {
			softwareJSON.put("id", id);
		}

		// "type" attribute
		String type = software.getType();
		if (type != null) {
			softwareJSON.put("type", type);
		}

		// "name" attribute
		String name = software.getName();
		if (name != null) {
			softwareJSON.put("name", name);
		}

		// "version" attribute
		String version = software.getVersion();
		if (version != null) {
			softwareJSON.put("version", version);
		}

		// "description" attribute
		String description = software.getDescription();
		if (description != null) {
			softwareJSON.put("description", description);
		}

		// "length" attribute
		long length = software.getLength();
		softwareJSON.put("length", length);

		// "lastModified" attribute
		Date lastModified = software.getLastModified();
		if (lastModified != null) {
			softwareJSON.put("lastModified", DateUtil.toString(lastModified, DateUtil.getJdbcDateFormat()));
		}

		// "md5" attribute
		String md5 = software.getMd5();
		if (md5 != null) {
			softwareJSON.put("md5", md5);
		}

		// "localPath" attribute
		String localPath = software.getLocalPath();
		if (localPath != null) {
			softwareJSON.put("localPath", localPath);
		}

		// "fileName" attribute
		String fileName = software.getFileName();
		if (fileName != null) {
			softwareJSON.put("fileName", fileName);
		}

		return softwareJSON;
	}

	/**
	 * Convert ProjectNode model to JSONObject
	 * 
	 * @param projectNode
	 * @return
	 */
	protected JSONObject projectNodeToJSON(ProjectNode projectNode) {
		if (projectNode == null) {
			return null;
		}

		JSONObject projectNodeJSON = new JSONObject();

		// "id" attribute
		String id = projectNode.getId();
		if (id != null) {
			projectNodeJSON.put("id", id);
		}

		// "name" attribute
		String name = projectNode.getName();
		if (name != null) {
			projectNodeJSON.put("name", name);
		}

		// "description" attribute
		String description = projectNode.getDescription();
		if (description != null) {
			projectNodeJSON.put("description", description);
		}

		// "properties" attribute
		Map<String, Object> properties = projectNode.getProperties();
		String propertiesString = JSONUtil.toJsonString(properties);
		if (propertiesString != null) {
			projectNodeJSON.put("properties", propertiesString);
		}

		// "installedSoftware" attribute
		JSONArray softwareArray = new JSONArray();
		{
			int softwareIndex = 0;
			for (Iterator<Software> softwareItor = projectNode.getInstalledSoftware().iterator(); softwareItor.hasNext();) {
				Software software = softwareItor.next();
				String softwareId = software.getId();

				JSONObject softwareJSON = new JSONObject();
				{
					softwareJSON.put("softwareId", softwareId);
				}
				softwareArray.put(softwareIndex++, softwareJSON);
			}
		}
		projectNodeJSON.put("installedSoftware", softwareArray);

		return projectNodeJSON;
	}

}
