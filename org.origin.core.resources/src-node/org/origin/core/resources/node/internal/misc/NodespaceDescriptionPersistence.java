package org.origin.core.resources.node.internal.misc;

import java.io.File;
import java.io.IOException;

import org.origin.core.resources.node.NodespaceDescription;

public class NodespaceDescriptionPersistence {

	private static NodespaceDescriptionPersistence INSTANCE = new NodespaceDescriptionPersistence();

	public static NodespaceDescriptionPersistence getInstance() {
		return INSTANCE;
	}

	/**
	 * 
	 * @param nodespaceFolder
	 * @return
	 */
	private File getNodespaceDescriptionFile(File nodespaceFolder) {
		File dotMetadataDir = new File(nodespaceFolder, ".metadata");
		return new File(dotMetadataDir, ".nodespace");
	}

	/**
	 * 
	 * @param nodespaceFolder
	 * @return
	 * @throws IOException
	 */
	public NodespaceDescription load(File nodespaceFolder) throws IOException {
		File dotNodespaceFile = getNodespaceDescriptionFile(nodespaceFolder);
		NodespaceDescriptionReader reader = new NodespaceDescriptionReader();
		return reader.read(dotNodespaceFile);
	}

	/**
	 * 
	 * @param nodespaceFolder
	 * @param nodespaceDesc
	 * @throws IOException
	 */
	public void save(File nodespaceFolder, NodespaceDescription nodespaceDesc) throws IOException {
		File dotNodespaceFile = getNodespaceDescriptionFile(nodespaceFolder);
		if (!dotNodespaceFile.getParentFile().exists()) {
			dotNodespaceFile.getParentFile().mkdirs();
		}
		NodespaceDescriptionWriter writer = new NodespaceDescriptionWriter();
		writer.write(nodespaceDesc, dotNodespaceFile);
	}

}
