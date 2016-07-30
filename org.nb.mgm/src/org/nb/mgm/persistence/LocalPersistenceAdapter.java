package org.nb.mgm.persistence;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.nb.mgm.model.runtime.ClusterRoot;
import org.nb.mgm.service.ManagementConstants;

/*
 * {
 * 		machines: [
 * 			{
 * 				id: 
 * 				name:
 * 				description:
 * 				ipAddress:
 * 				homes: [
 * 					{
 * 						id:
 * 						name:
 * 						description:
 * 						url:
 * 						joinedMetaSectorIds:[]
 * 						joinedMetaSpaceIds:[]
 * 					},	
 * 				]
 * 			},
 *      ],
 * 		metaSectors: [
 * 			{
 * 				id:
 * 				name:
 * 				description:
 * 				artifacts: [
 * 					{
 * 						id:
 * 						name:
 * 						description:
 * 						
 * 					}
 * 				]
 * 				metaSpaces: [
 * 					{
 * 						id:
 * 						name:
 * 						description:
 * 						deployedArtifactIds: []
 * 					}
 * 				]
 * 			}
 * 		]
 * }
 * 
 */
public class LocalPersistenceAdapter implements MgmPersistenceAdapter {

	protected Map<Object, Object> props;

	/**
	 * 
	 * @param props
	 */
	public LocalPersistenceAdapter(Map<Object, Object> props) {
		this.props = props;
	}

	protected File getPersistenceFile() {
		String dir = (String) this.props.get(PERSISTENCE_LOCAL_DIR);
		if (dir == null || dir.isEmpty()) {
			dir = (String) this.props.get(ManagementConstants.CLUSTER_HOME_DIR);
		}

		File file = new File(dir, "cluster.json");
		return file;
	}

	@Override
	public ClusterRoot load() {
		File file = getPersistenceFile();

		if (file == null || !file.exists()) {
			ClusterRoot root = new ClusterRoot();
			save(root);
		}

		LocalJsonReader reader = new LocalJsonReader(file);
		ClusterRoot root = reader.read();
		return root;
	}

	@Override
	public void save(ClusterRoot root) {
		File file = getPersistenceFile();
		LocalJsonWriter writer = new LocalJsonWriter(file, root);
		try {
			writer.write();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
