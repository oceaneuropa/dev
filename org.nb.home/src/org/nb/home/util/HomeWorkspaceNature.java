package org.nb.home.util;

import org.origin.core.workspace.IWorkspace;
import org.origin.core.workspace.nature.NatureRegistry;
import org.origin.core.workspace.nature.WorkspaceNature;
import org.origin.core.workspace.nature.WorkspaceNatureProvider;

public class HomeWorkspaceNature implements WorkspaceNature, WorkspaceNatureProvider {

	public static HomeWorkspaceNature PROVIDER = new HomeWorkspaceNature();
	public static final String NATURE_ID = "nb.home.WorkspaceNature";

	public static void register() {
		NatureRegistry.INSTANCE.register(NATURE_ID, PROVIDER);
	}

	public static void unregister() {
		NatureRegistry.INSTANCE.unregister(NATURE_ID);
	}

	@Override
	public HomeWorkspaceNature newInstance() {
		return new HomeWorkspaceNature();
	}

	protected IWorkspace workspace;

	@Override
	public String getNatureId() {
		return NATURE_ID;
	}

	@Override
	public void setResource(IWorkspace workspace) {
		this.workspace = workspace;
	}

	@Override
	public IWorkspace getResource() {
		return this.workspace;
	}

	@Override
	public void load() {

	}

	@Override
	public void save() {

	}

	@Override
	public void configure() {

	}

	@Override
	public void deconfigure() {

	}

}
