package org.orbit.service.program.impl;

import org.orbit.service.program.ProgramInstance;
import org.orbit.service.program.ProgramProvider;
import org.osgi.framework.BundleContext;

public class ProgramInstanceImpl implements ProgramInstance {

	protected BundleContext bundleContext;
	protected ProgramProvider programProvider;
	protected Object referencedInstance;
	protected boolean exited = false;

	public ProgramInstanceImpl() {
	}

	/**
	 * 
	 * @param bundleContext
	 * @param programProvider
	 * @param referencedInstance
	 */
	public ProgramInstanceImpl(BundleContext bundleContext, ProgramProvider programProvider, Object referencedInstance) {
		this.bundleContext = bundleContext;
		this.programProvider = programProvider;
		this.referencedInstance = referencedInstance;
	}

	@Override
	public BundleContext getBundleContext() {
		return this.bundleContext;
	}

	public void setBundleContext(BundleContext bundleContext) {
		this.bundleContext = bundleContext;
	}

	public ProgramProvider getProgramProvider() {
		return this.programProvider;
	}

	public void setProgramProvider(ProgramProvider programProvider) {
		this.programProvider = programProvider;
	}

	@Override
	public Object getReferenceInstance() {
		return this.referencedInstance;
	}

	public void setReferenceInstance(Object referencedInstance) {
		this.referencedInstance = referencedInstance;
	}

	@Override
	public int exit() {
		int exitCode = 0;
		if (!this.exited) {
			ProgramProvider programProvider = getProgramProvider();
			BundleContext bundleContext = getBundleContext();
			Object referencedInstance = getReferenceInstance();
			if (programProvider != null && bundleContext != null) {
				exitCode = programProvider.exit(bundleContext, referencedInstance);
			}
			this.exited = true;
		}
		return exitCode;
	}

}
