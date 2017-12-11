package org.orbit.infra.connector.test;

import java.io.IOException;
import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runners.MethodSorters;
import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexService;

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
		// IndexServiceConfiguration config = new IndexServiceConfiguration("http://127.0.0.1:9090/orbit/v1");
		// return IndexServiceFactory.getInstance().createIndexService(config);
		return null;
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
	public void test003_getIndexItemsWithIndexProviderIdAndType() {
		System.out.println("--- --- --- test003_getIndexItemsWithIndexProviderIdAndType() --- --- ---");
		String indexProviderId = "driver.index.provider";
		String type = "drive";
		try {
			List<IndexItem> indexItems = indexService.getIndexItems(indexProviderId, type);
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
