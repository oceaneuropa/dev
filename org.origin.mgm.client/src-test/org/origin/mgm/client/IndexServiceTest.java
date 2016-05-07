package org.origin.mgm.client;

import java.util.List;

import org.junit.Test;
import org.origin.common.rest.client.ClientException;
import org.origin.mgm.client.api.IndexService;
import org.origin.mgm.client.api.IndexServiceFactory;
import org.origin.mgm.model.runtime.IndexItem;

public class IndexServiceTest {

	protected IndexService indexService;

	public IndexServiceTest() {
		this.indexService = getIndexService();
	}

	protected void setUp() {
		this.indexService = getIndexService();
	}

	protected IndexService getIndexService() {
		return IndexServiceFactory.createIndexService("http://127.0.0.1:9090/indexservice/v1", "admin", "admin");
	}

	@Test
	public void testGetMachines() {
		System.out.println("--- --- --- testGetMachines() --- --- ---");
		try {
			List<IndexItem> indexItems = indexService.getIndexItems();
			for (IndexItem indexItem : indexItems) {
				System.out.println(indexItem.toString());
			}
		} catch (ClientException e) {
			e.printStackTrace();
		}
		System.out.println();
	}

}
