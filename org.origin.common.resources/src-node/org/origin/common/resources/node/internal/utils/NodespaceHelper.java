package org.origin.common.resources.node.internal.utils;

import java.io.File;

public class NodespaceHelper {

	private static NodespaceHelper INSTANCE = new NodespaceHelper();

	public static NodespaceHelper getInstance() {
		return INSTANCE;
	}

	/**
	 * 
	 * @param folder
	 * @return
	 */
	public boolean isNodespace(File folder) {
		if (folder != null && folder.isDirectory()) {
			File dotMetadataDir = new File(folder, ".metadata");
			if (dotMetadataDir.isDirectory()) {
				File dotNodespaceFile = new File(dotMetadataDir, ".nodespace");
				if (dotNodespaceFile.isFile()) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 
	 * @param folder
	 * @return
	 */
	public boolean isNode(File folder) {
		if (folder != null && folder.isDirectory()) {
			File dotMetadataDir = new File(folder, ".metadata");
			if (dotMetadataDir.isDirectory()) {
				File dotNodeFile = new File(dotMetadataDir, ".node");
				if (dotNodeFile.isFile()) {
					return true;
				}
			}
		}
		return false;
	}

}
