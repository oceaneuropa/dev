package org.plutus.lottery.powerball;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface DrawReader {

	List<Draw> read(InputStream input) throws IOException;

}
