package org.orbit.component.runtime.cli;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.felix.service.command.Descriptor;
import org.apache.felix.service.command.Parameter;
import org.orbit.component.runtime.ComponentConstants;
import org.orbit.component.runtime.tier3.nodecontrol.service.NodeControlService;
import org.orbit.platform.sdk.command.CommandActivator;
import org.origin.common.annotation.Annotated;
import org.origin.common.annotation.Dependency;
import org.origin.common.osgi.OSGiServiceUtil;
import org.origin.common.resources.IPath;
import org.origin.common.resources.node.INode;
import org.origin.common.resources.node.NodeDescription;
import org.origin.common.util.PrettyPrinter;
import org.origin.common.util.PropertyUtil;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NodeControlRuntimeCommand implements Annotated, CommandActivator {

	public static final String ID = "org.orbit.component.runtime.cli.NodeControlRuntimeCommand";

	protected static Logger LOG = LoggerFactory.getLogger(NodeControlRuntimeCommand.class);

	protected static String[] NODE_COLS = new String[] { "Id", "Path", "Attributes" };

	protected BundleContext bundleContext;
	protected Map<Object, Object> properties;
	protected String scheme = "orbit";

	@Dependency
	protected NodeControlService nodeControlService;

	protected String getScheme() {
		return this.scheme;
	}

	/**
	 * 
	 * @param bundleContext
	 */
	public void start(BundleContext bundleContext) {
		LOG.info("start()");
		this.bundleContext = bundleContext;

		Hashtable<String, Object> props = new Hashtable<String, Object>();
		props.put("osgi.command.scope", getScheme());
		props.put("osgi.command.function", new String[] { //
				// node
				"lnodes", "lnode", "create_node", "delete_node", "set_node_attr", "remove_node_attr", "start_node", "stop_node" //
		});

		Map<Object, Object> properties = new Hashtable<Object, Object>();
		PropertyUtil.loadProperty(bundleContext, properties, ComponentConstants.ORBIT_DOMAIN_SERVICE_URL);
		this.properties = properties;

		OSGiServiceUtil.register(this.bundleContext, NodeControlRuntimeCommand.class.getName(), this, props);
		OSGiServiceUtil.register(this.bundleContext, Annotated.class.getName(), this);
	}

	/**
	 * 
	 * @param bundleContext
	 */
	public void stop(BundleContext bundleContext) {
		LOG.info("stop()");

		OSGiServiceUtil.unregister(Annotated.class.getName(), this);
		OSGiServiceUtil.unregister(NodeControlRuntimeCommand.class.getName(), this);

		this.bundleContext = null;
	}

	protected NodeControlService getNodeControlService() {
		if (this.nodeControlService == null) {
			throw new IllegalStateException("NodeControlService is null.");
		}
		return this.nodeControlService;
	}

	/**
	 * 
	 * @param absentValue
	 * @param paramValue
	 * @return
	 */
	protected String checkParameter(Object absentValue, String paramValue) {
		if (absentValue != null && absentValue.equals(paramValue)) {
			return null;
		}
		return paramValue;
	}

	protected String getAccessToken() {
		return null;
	}

	@Descriptor("List nodes")
	public void lnodes(// Parameters
			@Descriptor("node id") @Parameter(names = { "-typeId", "--typeId" }, absentValue = Parameter.UNSPECIFIED) String typeId //
	) {
		// CLIHelper.getInstance().printCommand(getScheme(), "lnodes");
		typeId = checkParameter(Parameter.UNSPECIFIED, typeId);

		try {
			NodeControlService service = getNodeControlService();

			List<INode> nodes = null;
			if (typeId != null) {
				nodes = service.getNodes(typeId);
			} else {
				nodes = service.getNodes();
			}

			String[][] rows = new String[nodes.size()][NODE_COLS.length];
			int rowIndex = 0;
			for (INode node : nodes) {
				String id = null;
				String attributes = "";
				IPath fullpath = node.getFullPath();

				NodeDescription desc = node.getDescription();
				if (desc != null) {
					id = desc.getId();

					int index = 0;
					Map<String, Object> attribute = desc.getAttributes();
					for (Iterator<String> itor = attribute.keySet().iterator(); itor.hasNext();) {
						String attrName = itor.next();
						Object attrValue = attribute.get(attrName);
						if (index > 0) {
							attributes += ",";
						}
						attributes += attrName + "=" + attrValue;
						index++;
					}
				}

				rows[rowIndex++] = new String[] { id, fullpath.getPathString(), attributes };
			}
			PrettyPrinter.prettyPrint(NODE_COLS, rows, nodes.size());

		} catch (Exception e) {
			// e.printStackTrace();
			System.err.println(e.getMessage());
		}
	}

	@Descriptor("Create node")
	public void create_node( //
			// Parameters
			@Descriptor("node id") @Parameter(names = { "-id", "--id" }, absentValue = Parameter.UNSPECIFIED) String id, //
			@Descriptor("node type id") @Parameter(names = { "-typeId", "--typeId" }, absentValue = Parameter.UNSPECIFIED) String typeId, //
			@Descriptor("node name") @Parameter(names = { "-name", "--name" }, absentValue = Parameter.UNSPECIFIED) String name //
	) {
		id = checkParameter(Parameter.UNSPECIFIED, id);
		typeId = checkParameter(Parameter.UNSPECIFIED, typeId);
		name = checkParameter(Parameter.UNSPECIFIED, name);

		try {
			NodeControlService service = getNodeControlService();
			INode node = service.createNode(id, typeId, name);

			boolean succeed = (node != null) ? true : false;
			if (succeed) {
				System.out.println("Node is successfully created.");
			} else {
				System.out.println("Node is not created.");
			}
		} catch (Exception e) {
			// e.printStackTrace();
			System.err.println(e.getMessage());
		}
	}

	@Descriptor("Delete node")
	public void delete_node( //
			// Parameters
			@Descriptor("node id") @Parameter(names = { "-id", "--id" }, absentValue = Parameter.UNSPECIFIED) String id //
	) {
		id = checkParameter(Parameter.UNSPECIFIED, id);

		try {
			NodeControlService service = getNodeControlService();
			boolean succeed = service.deleteNode(id);

			if (succeed) {
				System.out.println("Node is successfully deleted.");
			} else {
				System.out.println("Node is not deleted.");
			}
		} catch (Exception e) {
			// e.printStackTrace();
			System.err.println(e.getMessage());
		}
	}

	@Descriptor("Set node attribute")
	public void set_node_attr( //
			// Parameters
			@Descriptor("node id") @Parameter(names = { "-id", "--id" }, absentValue = Parameter.UNSPECIFIED) String id, //
			@Descriptor("attribute name") @Parameter(names = { "-attrName", "--attrName" }, absentValue = Parameter.UNSPECIFIED) String attrName, //
			@Descriptor("attribute value") @Parameter(names = { "-attrValue", "--attrValue" }, absentValue = Parameter.UNSPECIFIED) String attrValue //
	) {
		id = checkParameter(Parameter.UNSPECIFIED, id);
		attrName = checkParameter(Parameter.UNSPECIFIED, attrName);
		attrValue = checkParameter(Parameter.UNSPECIFIED, attrValue);

		try {
			NodeControlService service = getNodeControlService();
			boolean succeed = service.addAttribute(id, attrName, attrValue);

			if (succeed) {
				System.out.println("Node attribute is successfully updated.");
			} else {
				System.out.println("Node attribute is not updated.");
			}
		} catch (Exception e) {
			// e.printStackTrace();
			System.err.println(e.getMessage());
		}
	}

	@Descriptor("Remove node attribute")
	public void remove_node_attr( //
			// Parameters
			@Descriptor("node id") @Parameter(names = { "-id", "--id" }, absentValue = Parameter.UNSPECIFIED) String id, //
			@Descriptor("attribute name") @Parameter(names = { "-attrName", "--attrName" }, absentValue = Parameter.UNSPECIFIED) String attrName //
	) {
		id = checkParameter(Parameter.UNSPECIFIED, id);
		attrName = checkParameter(Parameter.UNSPECIFIED, attrName);

		try {
			NodeControlService service = getNodeControlService();
			boolean succeed = service.deleteAttribute(id, attrName);

			if (succeed) {
				System.out.println("Node attribute is successfully removed.");
			} else {
				System.out.println("Node attribute is not removed.");
			}
		} catch (Exception e) {
			// e.printStackTrace();
			System.err.println(e.getMessage());
		}
	}

	@Descriptor("Start node")
	public void start_node( //
			// Parameters
			@Descriptor("node id") @Parameter(names = { "-id", "--id" }, absentValue = Parameter.UNSPECIFIED) String id //
	) {
		id = checkParameter(Parameter.UNSPECIFIED, id);

		try {
			NodeControlService service = getNodeControlService();
			String accessToken = getAccessToken();

			Map<String, Object> options = new HashMap<String, Object>();
			boolean succeed = service.startNode(id, accessToken, options);

			if (succeed) {
				System.out.println("Node is successfully started.");
			} else {
				System.out.println("Node is not started.");
			}
		} catch (Exception e) {
			// e.printStackTrace();
			System.err.println(e.getMessage());
		}
	}

	@Descriptor("Stop node")
	public void stop_node( //
			// Parameters
			@Descriptor("node id") @Parameter(names = { "-id", "--id" }, absentValue = Parameter.UNSPECIFIED) String id //
	) {
		id = checkParameter(Parameter.UNSPECIFIED, id);

		try {
			NodeControlService service = getNodeControlService();
			String accessToken = getAccessToken();

			Map<String, Object> options = new HashMap<String, Object>();
			boolean succeed = service.stopNode(id, accessToken, options);

			if (succeed) {
				System.out.println("Node is successfully stopped.");
			} else {
				System.out.println("Node is not stopped.");
			}
		} catch (Exception e) {
			// e.printStackTrace();
			System.err.println(e.getMessage());
		}
	}

}
