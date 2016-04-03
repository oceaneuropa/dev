package osgi.mgm.runtime.model;

import java.util.ArrayList;
import java.util.List;

public class MetaSpace extends ModelObject {

	protected List<String> deployedArtifactIds = new ArrayList<String>();

	public MetaSpace() {
	}

	/**
	 * 
	 * @param metaSector
	 */
	public MetaSpace(MetaSector metaSector) {
		super(metaSector);
	}

	public MetaSector getMetaSector() {
		if (this.getParent() instanceof MetaSector) {
			return (MetaSector) this.getParent();
		}
		return null;
	}

	// ----------------------------------------------------------------------------------------------------------------
	// deployedArtifactIds
	// ----------------------------------------------------------------------------------------------------------------
	public List<String> getDeployedArtifactIds() {
		return deployedArtifactIds;
	}

	public void setDeployedArtifactIds(List<String> deployedArtifactIds) {
		this.deployedArtifactIds = deployedArtifactIds;
	}

	public void addDeployedArtifactId(String deployedArtifactId) {
		if (deployedArtifactId != null && !this.deployedArtifactIds.contains(deployedArtifactId)) {
			this.deployedArtifactIds.add(deployedArtifactId);
		}
	}

	public void removeDeployedArtifactId(String deployedArtifactId) {
		if (deployedArtifactId != null && this.deployedArtifactIds.contains(deployedArtifactId)) {
			this.deployedArtifactIds.remove(deployedArtifactId);
		}
	}

}
