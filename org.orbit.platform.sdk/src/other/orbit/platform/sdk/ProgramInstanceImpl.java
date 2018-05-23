package other.orbit.platform.sdk;

import org.origin.common.extensions.core.IExtension.Context;

public class ProgramInstanceImpl implements IProgramInstance {

	protected Context context;
	protected IProgramLauncher launcher;
	protected Object referencedInstance;
	protected boolean exited = false;

	public ProgramInstanceImpl() {
	}

	/**
	 * 
	 * @param context
	 * @param launcher
	 * @param referencedInstance
	 */
	public ProgramInstanceImpl(Context context, IProgramLauncher launcher, Object referencedInstance) {
		this.context = context;
		this.launcher = launcher;
		this.referencedInstance = referencedInstance;
	}

	@Override
	public Context getContext() {
		return this.context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	@Override
	public IProgramLauncher getLauncher() {
		return this.launcher;
	}

	public void setProgramExtension(IProgramLauncher launcher) {
		this.launcher = launcher;
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
			IProgramLauncher launcher = getLauncher();
			Context context = getContext();
			Object referencedInstance = getReferenceInstance();
			if (launcher != null) {
				exitCode = launcher.exit(context, referencedInstance);
			}
			this.exited = true;
		}
		return exitCode;
	}

}
