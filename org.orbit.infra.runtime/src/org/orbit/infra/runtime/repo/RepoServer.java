package org.orbit.infra.runtime.repo;

import java.io.IOException;

public interface RepoServer {

	void start() throws IOException;

	void stop() throws IOException;

}
