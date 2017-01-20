package org.plutus.stock.resource;

import java.io.File;
import java.net.URI;

import org.apache.commons.io.FilenameUtils;
import org.origin.common.resource.ResourceFactory;
import org.origin.common.resource.ResourceFactoryRegistry;
import org.origin.common.workingcopy.AbstractWorkingCopyFactory;
import org.origin.common.workingcopy.WorkingCopy;
import org.plutus.stock.common.StockConstants;

public class StockStatResourceFactory extends AbstractWorkingCopyFactory<StockStatResource> {

	public static final String FACTORY_NAME = "StockStatResourceFactory";

	public static StockStatResourceFactory INSTANCE = new StockStatResourceFactory();

	/**
	 * Register resource factory.
	 * 
	 */
	public static void register() {
		ResourceFactory<?> factory = ResourceFactoryRegistry.INSTANCE.getFactory(FACTORY_NAME);
		if (!(factory instanceof StockStatResourceFactory)) {
			ResourceFactoryRegistry.INSTANCE.register(FACTORY_NAME, StockStatResourceFactory.INSTANCE);
		}
	}

	/**
	 * Unregister resource factory.
	 * 
	 */
	public static void unregister() {
		ResourceFactory<?> factory = ResourceFactoryRegistry.INSTANCE.getFactory(FACTORY_NAME);
		if (factory instanceof StockStatResourceFactory) {
			ResourceFactoryRegistry.INSTANCE.unregister(FACTORY_NAME);
		}
	}

	@Override
	public String getName() {
		return FACTORY_NAME;
	}

	@Override
	public boolean isSupported(URI uri) {
		File file = new File(uri);
		String fileExt = FilenameUtils.getExtension(file.getName());
		if (StockConstants.STOCK_STAT_FILE_EXTENSION.equalsIgnoreCase(fileExt)) {
			return true;
		}
		return false;
	}

	@Override
	public StockStatResource createResource(URI uri) {
		return new StockStatResource(uri);
	}

	@Override
	protected WorkingCopy createWorkingCopy(URI uri) {
		return new StockStatWorkingCopy(uri);
	}

}
