package org.origin.common.rest.server;

import java.net.URI;

import org.origin.common.rest.switcher.Switcher;
import org.osgi.framework.BundleContext;

/*
 * https://www.javaworld.com/article/2077921/architecture-scalability/server-load-balancing-architectures--part-1--transport-level-load-balancing.html
 * 
 * https://www.javaworld.com/article/2077922/architecture-scalability/server-load-balancing-architectures-part-2-application-level-load-balanci.html
 * 
 */
public class AbstractJerseyWSApplicationSwitcher extends AbstractJerseyWSApplication {

	protected Switcher<URI> switcher;

	public AbstractJerseyWSApplicationSwitcher(String contextRoot, int feature, Switcher<URI> switcher) {
		super(contextRoot, feature);
		this.switcher = switcher;
	}

	public Switcher<URI> getSwitcher() {
		return this.switcher;
	}

	public void setSwitcher(Switcher<URI> switcher) {
		this.switcher = switcher;
	}

	@Override
	public void start(BundleContext bundleContext) {
		if (this.switcher != null) {
			this.switcher.start();
		}

		super.start(bundleContext);
	}

	@Override
	public void stop(BundleContext bundleContext) {
		super.stop(bundleContext);

		if (this.switcher != null) {
			this.switcher.stop();
		}
	}

}
