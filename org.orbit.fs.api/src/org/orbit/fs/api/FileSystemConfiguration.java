package org.orbit.fs.api;

import org.origin.common.adapter.IAdaptable;

/**
 *
 */
public interface FileSystemConfiguration extends IAdaptable {

	public String getUrl();

	public void setUrl(String url);

	public String getContextRoot();

	public void setContextRoot(String contextRoot);

	public String getUsername();

	public void setUsername(String username);

	public String getPassword();

	public void setPassword(String password);

}
