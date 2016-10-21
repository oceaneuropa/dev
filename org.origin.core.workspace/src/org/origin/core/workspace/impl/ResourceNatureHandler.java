package org.origin.core.workspace.impl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;

import org.origin.core.workspace.IResource;
import org.origin.core.workspace.IResourceDescription;
import org.origin.core.workspace.nature.NatureRegistry;
import org.origin.core.workspace.nature.ResourceNature;

public class ResourceNatureHandler {

	protected Map<String, ResourceNature<?>> naturesMap = new LinkedHashMap<String, ResourceNature<?>>();

	/**
	 * 
	 * @param natureId
	 * @param natureClass
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public synchronized <NATURE extends ResourceNature<RES>, RES extends IResource> NATURE getNature(String natureId, Class<NATURE> natureClass) {
		NATURE nature = (NATURE) this.naturesMap.get(natureId);
		if (nature == null || this != nature.getResource()) {
			nature = NatureRegistry.INSTANCE.createNature(natureId, natureClass);
			if (nature != null) {
				nature.setResource((RES) this);
				this.naturesMap.put(natureId, nature);
			}
		}
		return (NATURE) nature;
	}

	/**
	 * Configure natures
	 * 
	 * (1) for removed natures: nature.deconfigure();
	 * 
	 * (2) for added natures: nature.configure(); nature.load();
	 * 
	 * @param oldDescription
	 * @param newDescription
	 * @param natureClass
	 */
	public <NATURE extends ResourceNature<RES>, RES extends IResource> void configureNatures(IResourceDescription oldDescription, IResourceDescription newDescription, Class<NATURE> natureClass) {
		// 1. get deleted natureIds and add natureIds by comparing the old and new set of natureIds.
		HashSet<String> oldNatureIds = new HashSet<String>(Arrays.asList(oldDescription.getNatureIds()));
		HashSet<String> newNatureIds = new HashSet<String>(Arrays.asList(newDescription.getNatureIds()));
		if (oldNatureIds.equals(newNatureIds)) {
			return;
		}
		@SuppressWarnings("unchecked")
		HashSet<String> deletedNatureIds = (HashSet<String>) oldNatureIds.clone();
		@SuppressWarnings("unchecked")
		HashSet<String> addNatureIds = (HashSet<String>) newNatureIds.clone();
		addNatureIds.removeAll(oldNatureIds);
		deletedNatureIds.removeAll(newNatureIds);

		// (1) for removed natures
		// nature.deconfigure();
		for (String natureIdToDelete : deletedNatureIds) {
			deconfigureNature(natureIdToDelete, natureClass);
		}

		// (2) for added natures
		// nature.configure();
		// nature.load();
		for (String natureIdToAdd : addNatureIds) {
			configureNature(natureIdToAdd, natureClass);
		}

		for (String natureIdToAdd : addNatureIds) {
			loadNature(natureIdToAdd, natureClass);
		}
	}

	/**
	 * 
	 * @param natureId
	 * @param natureClass
	 */
	public <NATURE extends ResourceNature<RES>, RES extends IResource> void loadNature(String natureId, Class<NATURE> natureClass) {
		NATURE nature = getNature(natureId, natureClass);
		if (nature != null) {
			nature.load();
		}
	}

	/**
	 * 
	 * @param natureId
	 * @param natureClass
	 */
	public <NATURE extends ResourceNature<RES>, RES extends IResource> void configureNature(String natureId, Class<NATURE> natureClass) {
		NATURE nature = getNature(natureId, natureClass);
		if (nature != null) {
			nature.configure();
		}
	}

	/**
	 * 
	 * @param natureId
	 * @param natureClass
	 */
	public <NATURE extends ResourceNature<RES>, RES extends IResource> void saveNature(String natureId, Class<NATURE> natureClass) {
		NATURE nature = getNature(natureId, natureClass);
		if (nature != null) {
			nature.save();
		}
	}

	/**
	 * Deconfigure a nature.
	 * 
	 * @param natureId
	 * @param natureClass
	 */
	public <NATURE extends ResourceNature<RES>, RES extends IResource> void deconfigureNature(String natureId, Class<NATURE> natureClass) {
		NATURE nature = getNature(natureId, natureClass);
		if (nature != null) {
			nature.deconfigure();
		}
	}

}
