package org.plutus.stock.resource;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

import org.origin.common.resource.impl.ResourceImpl;
import org.plutus.stock.resource.util.StockStatReader;
import org.plutus.stock.resource.util.StockStatWriter;

public class StockStatResource extends ResourceImpl {

	/**
	 * 
	 * @param uri
	 */
	public StockStatResource(URI uri) {
		super(uri);
	}

	@Override
	protected void doLoad(InputStream input) throws IOException {
		StockStatReader reader = new StockStatReader();
		reader.read(this, input);
	}

	@Override
	protected void doSave(OutputStream output) throws IOException {
		StockStatWriter writer = new StockStatWriter();
		writer.write(this, output);
	}

}
