package google.drive.example.v3.util;

import java.io.IOException;
import java.util.List;

import com.google.api.services.drive.Drive;

public interface GoogleDriveConnector {

	/**
	 * 
	 * @return
	 */
	public List<String> getScopes();

	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	public Drive getDriveService() throws IOException;

}
