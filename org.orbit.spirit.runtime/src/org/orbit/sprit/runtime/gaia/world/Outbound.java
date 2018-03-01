package org.orbit.sprit.runtime.gaia.world;

import java.io.OutputStream;

public interface Outbound {

	OutputStream getOutputStream();

	void close();

}
