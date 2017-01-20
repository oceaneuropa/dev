package org.plutus.stock.resource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

import org.origin.common.resource.impl.ResourceImpl;
import org.plutus.stock.resource.util.StockDataReader;
import org.plutus.stock.resource.util.StockDataWriter;

public class StockDataResource extends ResourceImpl {

	/**
	 * 
	 * @param file
	 */
	public StockDataResource(File file) {
		super(file);
	}

	/**
	 * 
	 * @param uri
	 */
	public StockDataResource(URI uri) {
		super(uri);
	}

	@Override
	protected void doLoad(InputStream input) throws IOException {
		StockDataReader reader = new StockDataReader();
		reader.read(this, input);
	}

	@Override
	protected void doSave(OutputStream output) throws IOException {
		StockDataWriter writer = new StockDataWriter();
		writer.write(this, output);
	}

}
