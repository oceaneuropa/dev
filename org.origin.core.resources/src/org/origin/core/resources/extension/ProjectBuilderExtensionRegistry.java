package org.origin.core.resources.extension;

import java.util.ArrayList;
import java.util.List;

public class ProjectBuilderExtensionRegistry {

	private static ProjectBuilderExtensionRegistry INSTANCE = new ProjectBuilderExtensionRegistry();

	public static ProjectBuilderExtensionRegistry getInstance() {
		return INSTANCE;
	}

	protected List<ProjectBuilder> projectBuilders = new ArrayList<ProjectBuilder>();
	protected ProjectBuilder[] projectBuildersArray = new ProjectBuilder[0];

	/**
	 * 
	 * @param projectBuilder
	 */
	public synchronized boolean register(ProjectBuilder projectBuilder) {
		if (projectBuilder != null && !this.projectBuilders.contains(projectBuilder)) {
			boolean succeed = this.projectBuilders.add(projectBuilder);
			update();
			return succeed;
		}
		return false;
	}

	/**
	 * 
	 * @param projectBuilder
	 */
	public synchronized boolean unregister(ProjectBuilder projectBuilder) {
		if (projectBuilder != null && this.projectBuilders.contains(projectBuilder)) {
			boolean succeed = this.projectBuilders.remove(projectBuilder);
			update();
			return succeed;
		}
		return false;
	}

	/**
	 * 
	 * @param id
	 */
	public synchronized ProjectBuilder unregister(String id) {
		if (id != null) {
			ProjectBuilder projectBuilderToRemove = null;
			for (ProjectBuilder projectBuilder : this.projectBuilders) {
				if (id.equals(projectBuilder.getId())) {
					projectBuilderToRemove = projectBuilder;
					break;
				}
			}
			if (projectBuilderToRemove != null) {
				this.projectBuilders.remove(projectBuilderToRemove);
				update();
				return projectBuilderToRemove;
			}
		}
		return null;
	}

	protected void update() {
		this.projectBuildersArray = this.projectBuilders.toArray(new ProjectBuilder[this.projectBuilders.size()]);
	}

	public synchronized ProjectBuilder[] getProjectBuilders() {
		return this.projectBuildersArray;
	}

}
