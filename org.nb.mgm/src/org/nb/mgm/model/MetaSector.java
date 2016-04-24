package org.nb.mgm.model;

import java.util.ArrayList;
import java.util.List;

import org.origin.common.rest.model.ModelObject;

public class MetaSector extends ModelObject {

	protected List<Artifact> artifacts = new ArrayList<Artifact>();
	protected List<MetaSpace> metaSpaces = new ArrayList<MetaSpace>();

	public MetaSector() {
	}

	/**
	 * 
	 * @param parent
	 */
	public MetaSector(Object parent) {
		super(parent);
	}

	// ----------------------------------------------------------------------------------------------------------------
	// MetaSpace
	// ----------------------------------------------------------------------------------------------------------------
	public List<MetaSpace> getMetaSpaces() {
		return this.metaSpaces;
	}

	public void addMetaSpace(MetaSpace metaSpace) {
		if (metaSpace != null && !this.metaSpaces.contains(metaSpace)) {
			metaSpace.setParent(this);
			this.metaSpaces.add(metaSpace);
		}
	}

	public void deleteMetaSpace(MetaSpace metaSpace) {
		if (metaSpace != null && this.metaSpaces.contains(metaSpace)) {
			this.metaSpaces.remove(metaSpace);
			metaSpace.setParent(null);
		}
	}

	// ----------------------------------------------------------------------------------------------------------------
	// Artifact
	// ----------------------------------------------------------------------------------------------------------------
	public List<Artifact> getArtifacts() {
		return this.artifacts;
	}

	public void addArtifact(Artifact artifact) {
		if (artifact != null && !this.artifacts.contains(artifact)) {
			artifact.setParent(this);
			this.artifacts.add(artifact);
		}
	}

	public void deleteArtifact(Artifact artifact) {
		if (artifact != null && this.artifacts.contains(artifact)) {
			this.artifacts.remove(artifact);
			artifact.setParent(null);
		}
	}

}
