package org.nb.home.util;

import org.origin.core.workspace.IProject;
import org.origin.core.workspace.nature.NatureRegistry;
import org.origin.core.workspace.nature.ProjectNature;
import org.origin.core.workspace.nature.ProjectNatureProvider;

public class HomeProjectNature implements ProjectNature, ProjectNatureProvider {

	public static HomeProjectNature PROVIDER = new HomeProjectNature();
	public static final String NATURE_ID = "nb.home.ProjectNature";

	public static void register() {
		NatureRegistry.INSTANCE.register(NATURE_ID, PROVIDER);
	}

	public static void unregister() {
		NatureRegistry.INSTANCE.unregister(NATURE_ID);
	}

	@Override
	public HomeProjectNature newInstance() {
		return new HomeProjectNature();
	}

	protected IProject project;

	@Override
	public String getNatureId() {
		return NATURE_ID;
	}

	@Override
	public void setResource(IProject project) {
		this.project = project;
	}

	@Override
	public IProject getResource() {
		return this.project;
	}

	@Override
	public void load() {

	}

	@Override
	public void configure() {

	}

	@Override
	public void save() {

	}

	@Override
	public void deconfigure() {

	}

}
