package org.orbit.os.runtime.world;

import java.io.OutputStream;

public interface Outbound {

	OutputStream getOutputStream();

	void close();

}
