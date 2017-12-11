package org.orbit.infra.connector.test;

import java.io.IOException;
import java.util.List;

import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexProvider;

public class IndexProviderTestWin {

	protected IndexProvider indexProvider;

	public IndexProviderTestWin() {
		this.indexProvider = getIndexProvider();
	}

	protected void setUp() {
		this.indexProvider = getIndexProvider();
	}

	protected IndexProvider getIndexProvider() {
		// IndexServiceConfiguration config = new IndexServiceConfiguration("http://127.0.0.1:9090/orbit/v1");
		// String indexProviderId = "filesystem.index.provider";
		// return IndexProviderFactory.getInstance().createIndexProvider(config);
		return null;
	}

	@Test
	public void test001_getIndexItems() {
		System.out.println("--- --- --- getIndexItems() --- --- ---");
		try {
			List<IndexItem> indexItems = indexProvider.getIndexItems("filesystem.");
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
		Result result = JUnitCore.runClasses(IndexProviderTestWin.class);
		System.out.println("--- --- --- IndexProviderTestWin.main() --- --- ---");
		for (Failure failure : result.getFailures()) {
			System.out.println(failure.toString());
		}
		System.out.println("result.wasSuccessful() = " + result.wasSuccessful());
	}

}
