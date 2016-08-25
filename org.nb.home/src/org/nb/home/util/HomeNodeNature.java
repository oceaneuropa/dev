package org.nb.home.util;

import org.origin.core.workspace.IFolder;
import org.origin.core.workspace.nature.FolderNature;
import org.origin.core.workspace.nature.FolderNatureProvider;
import org.origin.core.workspace.nature.NatureRegistry;

public class HomeNodeNature implements FolderNature, FolderNatureProvider {

	public static HomeNodeNature PROVIDER = new HomeNodeNature();
	public static final String NATURE_ID = "nb.home.NodeNature";

	public static void register() {
		NatureRegistry.INSTANCE.register(NATURE_ID, PROVIDER);
	}

	public static void unregister() {
		NatureRegistry.INSTANCE.unregister(NATURE_ID);
	}

	@Override
	public HomeNodeNature newInstance() {
		return new HomeNodeNature();
	}

	protected IFolder folder;

	@Override
	public String getNatureId() {
		return NATURE_ID;
	}

	@Override
	public void setResource(IFolder folder) {
		this.folder = folder;
	}

	@Override
	public IFolder getResource() {
		return this.folder;
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
