package org.origin.core.resources.node.internal.misc;

import java.io.File;
import java.io.IOException;

import org.origin.core.resources.node.NodeDescription;

public class NodeDescriptionPersistence {

	private static NodeDescriptionPersistence INSTANCE = new NodeDescriptionPersistence();

	public static NodeDescriptionPersistence getInstance() {
		return INSTANCE;
	}

	/**
	 * 
	 * @param nodeFolder
	 * @return
	 */
	private File getNodeDescriptionFile(File nodeFolder) {
		File dotMetadataDir = new File(nodeFolder, ".metadata");
		return new File(dotMetadataDir, ".node");
	}

	/**
	 * 
	 * @param nodeFolder
	 * @return
	 * @throws IOException
	 */
	public NodeDescription load(File nodeFolder) throws IOException {
		File dotNodeFile = getNodeDescriptionFile(nodeFolder);
		NodeDescriptionReader reader = new NodeDescriptionReader();
		return reader.read(dotNodeFile);
	}

	/**
	 * 
	 * @param nodeFolder
	 * @param nodeDesc
	 * @throws IOException
	 */
	public void save(File nodeFolder, NodeDescription nodeDesc) throws IOException {
		File dotNodeFile = getNodeDescriptionFile(nodeFolder);
		if (!dotNodeFile.getParentFile().exists()) {
			dotNodeFile.getParentFile().mkdirs();
		}
		NodeDescriptionWriter writer = new NodeDescriptionWriter();
		writer.write(nodeDesc, dotNodeFile);
	}

}
