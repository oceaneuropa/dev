package jersey.example1;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.apache.log4j.Logger;
//import org.mobicents.servlet.restcomm.rvd.commons.http.SslMode; 
//import org.mobicents.servlet.restcomm.rvd.configuration.RestcommConfig; 
//import org.mobicents.servlet.restcomm.rvd.http.utils.UriUtils; 
//import org.mobicents.servlet.restcomm.rvd.model.RvdConfig; 
import org.w3c.dom.Document;

import com.thoughtworks.xstream.XStream;

public class RvdConfiguration {
	static final Logger logger = Logger.getLogger(RvdConfiguration.class.getName());

	private static RvdConfiguration instance = null;

	private static final String WORKSPACE_DIRECTORY_NAME = "workspace";
	public static final String PROTO_DIRECTORY_PREFIX = "_proto";
	public static final String REST_SERVICES_PATH = "services"; // the "services" from the /restcomm-rvd/services/apps/... path

	public static final String WAVS_DIRECTORY_NAME = "wavs";
	private static final String RVD_PROJECT_VERSION = "1.4"; // version for rvd project syntax
	private static final String PACKAGING_VERSION = "1.0";
	private static final String RAS_APPLICATION_VERSION = "2"; // version of the RAS application specification
	public static final String STICKY_PREFIX = "sticky_"; // a prefix for rvd sticky variable names
	public static final String MODULE_PREFIX = "module_"; // a prefix for rvd module-scoped variable names
	public static final String CORE_VARIABLE_PREFIX = "core_"; // a prefix for rvd variables that come from Restcomm parameters
	public static final String PACKAGING_DIRECTORY_NAME = "packaging";
	public static final String TICKET_COOKIE_NAME = "rvdticket"; // the name of the cookie that is used to store ticket ids for authentication
	private static Set<String> restcommParameterNames = new HashSet<String>(Arrays.asList(new String[] { "CallSid", "AccountSid", "From", "To", "Body", "CallStatus", "ApiVersion", "Direction", "CallerName" })); // the
																																																					// names
																																																					// of
																																																					// the
																																																					// parameters
																																																					// supplied
																																																					// by
																																																					// restcomm
																																																					// request
																																																					// when
																																																					// starting
																																																					// an
																																																					// application
	public static final String PROJECT_LOG_FILENAME = "projectLog";
	public static final String DEFAULT_APPSTORE_DOMAIN = "apps.restcomm.com";
	public static final HashSet<String> builtinRestcommParameters = new HashSet<String>(Arrays.asList(new String[] { "CallSid", "AccountSid", "From", "To", "Body", "CallStatus", "ApiVersion", "Direction", "CallerName" }));
	public static final String RESTCOMM_HEADER_PREFIX = "SipHeader_"; // the prefix added to HTTP headers from Restcomm
	public static final String RESTCOMM_HEADER_PREFIX_DIAL = "DialSipHeader_"; // another prefix

	private String workspaceBasePath;
	private String externalServiceBase; // use this when relative urls (starting with /) are specified in ExternalService steps
	private RvdConfig rvdConfig; // the configuration settings from rvd.xml
	private RestcommConfig restcommConfig;

	private String contextRootPath;
	private URI restcommBaseUri;

	public static RvdConfiguration getInstance() {
		if (instance == null) {
			throw new IllegalStateException("RVD configuration has not been loaded.");
		}
		return instance;
	}

	public static RvdConfiguration createOnce(ServletContext servletContext) {
		synchronized (RvdConfiguration.class) {
			if (instance == null) {
				instance = new RvdConfiguration(servletContext);
			}
			return instance;
		}
	}

	private RvdConfiguration(ServletContext servletContext) {
		contextRootPath = servletContext.getRealPath("/");
		logger.info("contextRootPath: " + contextRootPath);
		load();
	}

	private void load() {
		// load configuration from rvd.xml file
		rvdConfig = loadRvdXmlConfig(contextRootPath + "WEB-INF/rvd.xml");
		// workspaceBasePath option
		String workspaceBasePath = contextRootPath + WORKSPACE_DIRECTORY_NAME;
		if (rvdConfig.getWorkspaceLocation() != null && !"".equals(rvdConfig.getWorkspaceLocation())) {
			if (rvdConfig.getWorkspaceLocation().startsWith("/"))
				workspaceBasePath = rvdConfig.getWorkspaceLocation(); // this is an absolute path
			else
				workspaceBasePath = contextRootPath + rvdConfig.getWorkspaceLocation(); // this is a relative path hooked under RVD context
		}
		this.workspaceBasePath = workspaceBasePath;

		restcommConfig = loadRestcommXmlConfig(contextRootPath + "../restcomm.war/WEB-INF/conf/restcomm.xml");

		logger.info("Using workspace at " + workspaceBasePath);
	}

	/**
	 * Loads rvd.xml into an RvdConfig. Returns null if the file is not found
	 * 
	 * @param pathToXml
	 * @return
	 */
	private RvdConfig loadRvdXmlConfig(String pathToXml) {
		try {
			FileInputStream input = new FileInputStream(pathToXml);
			XStream xstream = new XStream();
			xstream.alias("rvd", RvdConfig.class);
			rvdConfig = (RvdConfig) xstream.fromXML(input);
			return rvdConfig;
		} catch (FileNotFoundException e) {
			logger.warn("RVD configuration file not found: " + pathToXml);
			return null;
		}
	}

	/**
	 * Load configuration options from restcomm.xml that make sence for RVD.
	 * 
	 * @param pathToXml
	 * @return
	 */
	private RestcommConfig loadRestcommXmlConfig(String pathToXml) {
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder;
		File file = new File(pathToXml);
		try {
			docBuilder = docBuilderFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(file);
			XPathFactory xPathfactory = XPathFactory.newInstance();
			XPath xpath = xPathfactory.newXPath();
			// read ssl-mode
			XPathExpression expr = xpath.compile("/restcomm/http-client/ssl-mode/text()");
			String sslMode = (String) expr.evaluate(doc, XPathConstants.STRING);
			// read use-hostname-to-resolve-relative-url
			expr = xpath.compile("/restcomm/http-client/use-hostname-to-resolve-relative-url/text()");
			String useHostname = (String) expr.evaluate(doc, XPathConstants.STRING);
			// read hostname
			expr = xpath.compile("/restcomm/http-client/hostname/text()");
			String hostname = (String) expr.evaluate(doc, XPathConstants.STRING);

			RestcommConfig config = new RestcommConfig(sslMode, hostname, useHostname);
			return config;

		} catch (Exception e) {
			logger.error("Error parsing Restcomm config file: " + file.getPath(), e);
			return null;
		}
	}

	public String getWorkspaceBasePath() {
		return this.workspaceBasePath;
	}

	public String getProjectBasePath(String projectName) {
		return this.workspaceBasePath + File.separator + projectName;
	}

	public static String getRvdProjectVersion() {
		return RVD_PROJECT_VERSION;
	}

	public static String getPackagingVersion() {
		return PACKAGING_VERSION;
	}

	public static String getRasApplicationVersion() {
		return RAS_APPLICATION_VERSION;
	}

	public String getExternalServiceBase() {
		return externalServiceBase;
	}

	public void setExternalServiceBase(String externalServiceBase) {
		this.externalServiceBase = externalServiceBase;
	}

	public String getEffectiveRestcommIp(HttpServletRequest request) {
		String ipFromXml = rvdConfig.getRestcommPublicIp();

		String ip = request.getLocalAddr(); // use request ip as default
		if (ipFromXml != null && !"".equals(ipFromXml)) {
			ip = ipFromXml;
		}
		return ip;
	}

	// Always returns the destination port in the request. When the configuration/settings scheme clears
	// out a proper implementation should be done.
	// TODO
	public String getEffectiveRestcommPort(HttpServletRequest request) {
		int port = request.getLocalPort();
		return "" + port;
	}

	/**
	 * Returns the IP rvd listens to for internal use. This address can be used by restcomm to access applications. A request object is required to get it. Even
	 * when this request comes from a browser, this function should report the correct IP.
	 * 
	 * @param request
	 * @return
	 */
	public String getRvdInternalIp(HttpServletRequest request) {
		return request.getLocalAddr();
	}

	public int getRvdInternalPort(HttpServletRequest request) {
		return request.getLocalPort();
	}

	public static Set<String> getRestcommParameterNames() {
		return restcommParameterNames;
	}

	public SslMode getSslMode() {
		return restcommConfig.getSslMode();
	}

	public boolean getUseHostnameToResolveRelativeUrl() {
		return restcommConfig.isUseHostnameToResolveRelativeUrl();
	}

	public String getHostnameOverride() {
		return restcommConfig.getHostname();
	}

	// this is lazy loaded because HttpConnector enumeration (done in resolve()) fails otherwise
	public URI getRestcommBaseUri() {
		if (this.restcommBaseUri == null) {
			UriUtils uriUtils = new UriUtils(this);
			URI restcommBaseUri = null;
			try {
				URI uri = new URI("/");
				restcommBaseUri = uriUtils.resolve(uri);
			} catch (URISyntaxException e) {
				/* we should never reach here */ throw new IllegalStateException();
			}
			this.restcommBaseUri = restcommBaseUri;
		}
		return restcommBaseUri;
	}
}
