package org.plutus.lottery.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.plutus.lottery.common.Draw;

public interface DrawReader {

	List<Draw> read(InputStream input) throws IOException;

}
