package org.origin.mgm.client;

import java.io.IOException;
import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runners.MethodSorters;
import org.origin.mgm.client.api.IndexItem;
import org.origin.mgm.client.api.IndexService;
import org.origin.mgm.client.api.IndexServiceConfiguration;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class IndexServiceTestWin {

	protected IndexService indexService;

	public IndexServiceTestWin() {
		this.indexService = getIndexService();
	}

	protected void setUp() {
		this.indexService = getIndexService();
	}

	protected IndexService getIndexService() {
		IndexServiceConfiguration config = new IndexServiceConfiguration("http://127.0.0.1:9090", "admin", "123");
		return IndexService.newInstance(config);
	}

	@Test
	public void test001_getIndexItems() {
		System.out.println("--- --- --- getIndexItems() --- --- ---");
		try {
			List<IndexItem> indexItems = indexService.getIndexItems();
			for (IndexItem indexItem : indexItems) {
				System.out.println(indexItem.toString());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println();
	}

	@Test
	public void test002_getIndexItemsWithNamespace() {
		System.out.println("--- --- --- test002_getIndexItemsWithNamespace() --- --- ---");
		String namespace = "drive";
		try {
			List<IndexItem> indexItems = indexService.getIndexItemsByNamespace(namespace);
			for (IndexItem indexItem : indexItems) {
				System.out.println(indexItem.toString());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println();
	}

	@Test
	public void test003_getIndexItemsWithIndexProviderIdAndNamespace() {
		System.out.println("--- --- --- test003_getIndexItemsWithIndexProviderIdAndNamespace() --- --- ---");
		String indexProviderId = "driver.index.provider";
		String namespace = "drive";
		try {
			List<IndexItem> indexItems = indexService.getIndexItems(indexProviderId, namespace);
			for (IndexItem indexItem : indexItems) {
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
		Result result = JUnitCore.runClasses(IndexServiceTestWin.class);
		System.out.println("--- --- --- IndexServiceTestWin.main() --- --- ---");
		for (Failure failure : result.getFailures()) {
			System.out.println(failure.toString());
		}
		System.out.println("result.wasSuccessful() = " + result.wasSuccessful());
	}

}
