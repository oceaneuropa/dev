package org.origin.mgm.client;

import java.io.IOException;
import java.util.List;

import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.origin.mgm.client.api.IndexItem;
import org.origin.mgm.client.api.IndexItemConfigurable;
import org.origin.mgm.client.api.IndexProvider;
import org.origin.mgm.client.api.IndexServiceConfiguration;

public class IndexProviderTestWin {

	protected IndexProvider indexService;

	public IndexProviderTestWin() {
		this.indexService = getIndexProvider();
	}

	protected void setUp() {
		this.indexService = getIndexProvider();
	}

	protected IndexProvider getIndexProvider() {
		IndexServiceConfiguration config = new IndexServiceConfiguration("http://127.0.0.1:9090", "admin", "123");
		String indexProviderId = "filesystem.index.provider";
		return IndexProvider.newInstance(config, indexProviderId);
	}

	@Test
	public void test001_getIndexItems() {
		System.out.println("--- --- --- getIndexItems() --- --- ---");
		try {
			List<IndexItemConfigurable> indexItems = indexService.getIndexItems();
			for (IndexItemConfigurable indexItem : indexItems) {
				System.out.println(indexItem.toString());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println();
	}

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Result result = JUnitCore.runClasses(IndexProviderTestWin.class);
		System.out.println("--- --- --- IndexProviderTestWin.main() --- --- ---");
		for (Failure failure : result.getFailures()) {
			System.out.println(failure.toString());
		}
		System.out.println("result.wasSuccessful() = " + result.wasSuccessful());
	}

}
