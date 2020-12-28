package other.orbit.infra.runtime.indexes.service;

import static java.util.concurrent.TimeUnit.SECONDS;

import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import org.orbit.infra.runtime.InfraConstants;
import org.orbit.infra.runtime.indexes.service.IndexService;
import org.origin.common.util.PropertyUtil;

public class IndexServicePrinter {

	public static final String PROP_INITIAL_DELAY = "index.service.printer.initialDelay";
	public static final String PROP_PERIOD = "index.service.printer.period";

	public static final int DEFAULT_INITIAL_DELAY = 10;
	public static final int DEFAULT_PERIOD = 60;

	protected IndexService indexService;

	protected ScheduledExecutorService printScheduler = Executors.newScheduledThreadPool(1);
	protected ScheduledFuture<?> printHandle;
	protected Map<?, ?> properties;

	protected int initialDelay;
	protected int period;

	/**
	 * 
	 * @param indexService
	 * @param properties
	 */
	public IndexServicePrinter(IndexService indexService, Map<?, ?> properties) {
		this.indexService = indexService;

		this.properties = properties;
		this.initialDelay = PropertyUtil.getInt(properties, PROP_INITIAL_DELAY, DEFAULT_INITIAL_DELAY);
		this.period = PropertyUtil.getInt(properties, PROP_PERIOD, DEFAULT_PERIOD);
	}

	/**
	 * 
	 * @param properties
	 */
	public synchronized void update(Map<String, Object> properties) {
		if (properties != null && !properties.equals(this.properties)) {
			this.properties = properties;
			this.initialDelay = PropertyUtil.getInt(properties, PROP_INITIAL_DELAY, DEFAULT_INITIAL_DELAY);
			this.period = PropertyUtil.getInt(properties, PROP_PERIOD, DEFAULT_PERIOD);

			if (this.printHandle != null && !this.printHandle.isCancelled()) {
				unschedulePrinter();
				schedulePrinter();
			}
		}
	}

	public void start() {
		// scheduled runner to print out cached data
		unschedulePrinter();
		schedulePrinter();
	}

	public void stop() {
		unschedulePrinter();
	}

	protected void schedulePrinter() {
		Runnable printerRunnable = new Runnable() {
			@Override
			public void run() {
				try {
					printProfile();
					// printIndexItems();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		this.printHandle = printScheduler.scheduleAtFixedRate(printerRunnable, initialDelay, this.period, SECONDS);
	}

	protected void unschedulePrinter() {
		if (this.printHandle != null) {
			if (!this.printHandle.isCancelled()) {
				this.printHandle.cancel(true);
			}
			this.printHandle = null;
		}
	}

	protected Map<?, ?> getIndexServiceConfigProperties() {
		Map<?, ?> properties = null;
		// if (this.indexService instanceof ConfigPropertiesProvider) {
		// properties = ((ConfigPropertiesProvider) this.indexService).getProperties();
		// }
		return properties;
	}

	protected String getName() {
		return PropertyUtil.getString(getIndexServiceConfigProperties(), InfraConstants.COMPONENT_INDEX_SERVICE_NAME, null);
	}

	protected String getHostUrl() {
		return PropertyUtil.getString(getIndexServiceConfigProperties(), InfraConstants.COMPONENT_INDEX_SERVICE_HOST_URL, null);
	}

	protected String getContextRoot() {
		return PropertyUtil.getString(getIndexServiceConfigProperties(), InfraConstants.COMPONENT_INDEX_SERVICE_CONTEXT_ROOT, null);
	}

	/**
	 * Print out the profile of this index service node.
	 */
	public void printProfile() {
		String name = getName();
		String url = getHostUrl();
		String contextRoot = getContextRoot();

		System.out.println("IndexService Profile:");
		System.out.println("------------------------------------------------------------------------------------------------------------------------------------");
		System.out.println("Name: " + name);
		System.out.println("URL: " + url);
		System.out.println("Context Root: " + contextRoot);
		System.out.println("------------------------------------------------------------------------------------------------------------------------------------");
		System.out.println();
	}

	// public void printIndexItems() throws IndexServiceException {
	// List<IndexItem> indexItems = indexService.getIndexItems();
	// System.out.println("Cached data (" + indexItems.size() + "):");
	// System.out.println("------------------------------------------------------------------------------------------------------------------------------------");
	// for (IndexItem indexItem : indexItems) {
	// System.out.println(indexItem);
	// }
	// System.out.println("------------------------------------------------------------------------------------------------------------------------------------");
	// System.out.println();
	// }

}
