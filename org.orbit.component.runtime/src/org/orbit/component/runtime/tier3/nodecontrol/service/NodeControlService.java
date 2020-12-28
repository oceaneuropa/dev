package org.orbit.component.runtime.tier3.nodecontrol.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.origin.common.resources.IWorkspace;
import org.origin.common.resources.node.INode;
import org.origin.common.rest.editpolicy.ServiceEditPolicies;
import org.origin.common.service.AccessTokenProvider;
import org.origin.common.service.IWebService;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public interface NodeControlService extends IWebService, AccessTokenProvider {

	ServiceEditPolicies getEditPolicies();

	String getPlatformHome();

	String getNodespaceLocation();

	IWorkspace getWorkspace();

	List<INode> getNodes();

	List<INode> getNodes(String typeId);

	boolean nodeExists(String id);

	INode getNode(String id);

	INode createNode(String id, String typeId, String name) throws IOException;

	boolean addAttribute(String id, String name, Object value) throws IOException;

	boolean updateAttribute(String id, String oldName, String name, Object value) throws IOException;

	boolean deleteAttribute(String id, String name) throws IOException;

	boolean deleteNode(String id) throws IOException;

	boolean startNode(String id, String accessToken, Map<String, Object> options) throws IOException;

	boolean stopNode(String id, String accessToken, Map<String, Object> options) throws IOException;

	boolean isNodeStarting(String id) throws IOException;

	boolean isNodeStarted(String id, String accessToken) throws IOException;

	boolean isNodeStopping(String id) throws IOException;

	boolean isNodeStopped(String id, String accessToken) throws IOException;

}
