package org.origin.common.launch;

public interface LaunchConstants {

	String PLATFORM_HOME = "platform.home";

	// "1 << 1" -> 0010 -> 2
	int RUN = 1 << 1;

	// "1 << 2" -> 0100 -> 4
	int DEBUG = 1 << 2;

	String LAUNCH_EXTENSION = "launch";
	String DOT_LAUNCH_EXTENSION = ".launch";

	String LAUNCH_INSTANCE_ID = "launch_instance_id";
	String PROCESS_INSTANCE_ID = "process_instance_id";

	String PROGRAM = "program";

	String SYSTEM_ARGUMENTS_MAP = "system_arguments_map";
	String VM_ARGUMENTS_MAP = "vm_arguments_map";
	String PROGRAM_ARGUMENTS_LIST = "program_arguments_list";

	String OSGI_CONFIG_PROPERTIES_MAP = "osgi_config_properties_map";
	String OSGI_CONFIG_VM_ARGUMENTS_MAP = "osgi_config_vm_arguments_map";

	// ------------------------------------------------------------------------------------
	// see org.eclipse.osgi.service.environment.Constants
	// ------------------------------------------------------------------------------------
	// The constants need to be aligned with the OSGi ones. See page 64-588 of the spec
	public static final String OS_WIN32 = "win32";//$NON-NLS-1$
	public static final String OS_LINUX = "linux";//$NON-NLS-1$
	public static final String OS_AIX = "aix";//$NON-NLS-1$
	public static final String OS_SOLARIS = "solaris";//$NON-NLS-1$
	public static final String OS_HPUX = "hpux";//$NON-NLS-1$
	public static final String OS_QNX = "qnx";//$NON-NLS-1$
	public static final String OS_MACOSX = "macosx";//$NON-NLS-1$
	public static final String OS_EPOC32 = "epoc32";//$NON-NLS-1$
	public static final String OS_OS400 = "os/400"; //$NON-NLS-1$
	public static final String OS_OS390 = "os/390"; //$NON-NLS-1$
	public static final String OS_ZOS = "z/os"; //$NON-NLS-1$
	public static final String OS_UNKNOWN = "unknown";//$NON-NLS-1$
	public static final String ARCH_X86 = "x86";//$NON-NLS-1$
	public static final String ARCH_PA_RISC = "PA_RISC";//$NON-NLS-1$
	public static final String ARCH_PPC = "ppc";//$NON-NLS-1$
	public static final String ARCH_PPC64 = "ppc64";//$NON-NLS-1$
	public static final String ARCH_SPARC = "sparc";//$NON-NLS-1$
	public static final String ARCH_X86_64 = "x86_64";//$NON-NLS-1$
	public static final String ARCH_AMD64 = ARCH_X86_64;
	public static final String ARCH_IA64 = "ia64"; //$NON-NLS-1$
	public static final String ARCH_IA64_32 = "ia64_32";//$NON-NLS-1$
	public static final String WS_WIN32 = "win32";//$NON-NLS-1$
	public static final String WS_WPF = "wpf"; //$NON-NLS-1$
	public static final String WS_MOTIF = "motif";//$NON-NLS-1$
	public static final String WS_GTK = "gtk";//$NON-NLS-1$
	public static final String WS_PHOTON = "photon";//$NON-NLS-1$
	public static final String WS_CARBON = "carbon";//$NON-NLS-1$
	public static final String WS_COCOA = "cocoa";//$NON-NLS-1$
	public static final String WS_S60 = "s60";//$NON-NLS-1$
	public static final String WS_UNKNOWN = "unknown";//$NON-NLS-1$

	// ------------------------------------------------------------------------------------
	// see org.eclipse.pde.internal.launching.IPDEConstants
	// ------------------------------------------------------------------------------------
	String PLUGIN_ID = "org.eclipse.pde.launching"; //$NON-NLS-1$
	String UI_PLUGIN_ID = "org.eclipse.pde.ui"; //$NON-NLS-1$
	String LEGACY_UI_TEST_APPLICATION = "org.eclipse.pde.junit.runtime.legacytestapplication"; //$NON-NLS-1$
	String NON_UI_THREAD_APPLICATION = "org.eclipse.pde.junit.runtime.nonuithreadtestapplication"; //$NON-NLS-1$
	String UI_TEST_APPLICATION = "org.eclipse.pde.junit.runtime.uitestapplication"; //$NON-NLS-1$
	String CORE_TEST_APPLICATION = "org.eclipse.pde.junit.runtime.coretestapplication"; //$NON-NLS-1$
	String RESTART = "restart"; //$NON-NLS-1$
	String DOCLEARLOG = "clearwslog"; //$NON-NLS-1$
	String LAUNCHER_PDE_VERSION = "pde.version"; //$NON-NLS-1$
	String APPEND_ARGS_EXPLICITLY = "append.args"; //$NON-NLS-1$

	// ------------------------------------------------------------------------------------
	// see org.eclipse.jdt.launching.JavaRuntime
	// ------------------------------------------------------------------------------------
	public static final String JRELIB_VARIABLE = "JRE_LIB"; //$NON-NLS-1$
	public static final String JRESRC_VARIABLE = "JRE_SRC"; //$NON-NLS-1$
	public static final String JRESRCROOT_VARIABLE = "JRE_SRCROOT"; //$NON-NLS-1$
	public static final String EXTENSION_POINT_RUNTIME_CLASSPATH_ENTRY_RESOLVERS = "runtimeClasspathEntryResolvers"; //$NON-NLS-1$
	public static final String EXTENSION_POINT_RUNTIME_CLASSPATH_PROVIDERS = "classpathProviders"; //$NON-NLS-1$
	public static final String EXTENSION_POINT_EXECUTION_ENVIRONMENTS = "executionEnvironments"; //$NON-NLS-1$
	public static final String EXTENSION_POINT_VM_INSTALLS = "vmInstalls"; //$NON-NLS-1$
	public static final String EXTENSION_POINT_LIBRARY_LOCATION_RESOLVERS = "libraryLocationResolvers"; //$NON-NLS-1$
	public static final String JRE_CONTAINER = LaunchActivator.getDefault().getUniqueIdentifier() + ".JRE_CONTAINER"; //$NON-NLS-1$
	public static final String JRE_CONTAINER_MARKER = LaunchActivator.getDefault().getUniqueIdentifier() + ".jreContainerMarker"; //$NON-NLS-1$
	public static final int ERR_UNABLE_TO_RESOLVE_JRE = 160;
	public static final String PREF_CONNECT_TIMEOUT = LaunchActivator.getDefault().getUniqueIdentifier() + ".PREF_CONNECT_TIMEOUT"; //$NON-NLS-1$
	public static final String PREF_VM_XML = LaunchActivator.getDefault().getUniqueIdentifier() + ".PREF_VM_XML"; //$NON-NLS-1$
	public static final String PREF_STRICTLY_COMPATIBLE_JRE_NOT_AVAILABLE = LaunchActivator.getDefault().getUniqueIdentifier() + ".PREF_STRICTLY_COMPATIBLE_JRE_NOT_AVAILABLE"; //$NON-NLS-1$
	public static final int DEF_CONNECT_TIMEOUT = 20000;

	// ------------------------------------------------------------------------------------
	// see org.eclipse.pde.launching.IPDELauncherConstants
	// ------------------------------------------------------------------------------------
	String LOCATION = "location"; //$NON-NLS-1$
	String DOCLEAR = "clearws"; //$NON-NLS-1$
	String ASKCLEAR = "askclear"; //$NON-NLS-1$
	String APPLICATION = "application"; //$NON-NLS-1$
	String PRODUCT = "product"; //$NON-NLS-1$
	String USE_PRODUCT = "useProduct"; //$NON-NLS-1$
	String APP_TO_TEST = "testApplication"; //$NON-NLS-1$
	String VMINSTALL = "vminstall"; //$NON-NLS-1$
	String BOOTSTRAP_ENTRIES = "bootstrap"; //$NON-NLS-1$
	String USE_DEFAULT = "default"; //$NON-NLS-1$
	String USEFEATURES = "usefeatures"; //$NON-NLS-1$
	String SELECTED_WORKSPACE_PLUGINS = "selected_workspace_plugins"; //$NON-NLS-1$
	String DESELECTED_WORKSPACE_PLUGINS = "deselected_workspace_plugins"; //$NON-NLS-1$
	String AUTOMATIC_ADD = "automaticAdd"; //$NON-NLS-1$
	String AUTOMATIC_VALIDATE = "automaticValidate"; //$NON-NLS-1$
	String SELECTED_TARGET_PLUGINS = "selected_target_plugins"; //$NON-NLS-1$
	String INCLUDE_OPTIONAL = "includeOptional"; //$NON-NLS-1$
	String TRACING = "tracing"; //$NON-NLS-1$
	String TRACING_OPTIONS = "tracingOptions"; //$NON-NLS-1$
	String TRACING_SELECTED_PLUGIN = "selectedPlugin"; //$NON-NLS-1$
	String TRACING_CHECKED = "checked"; //$NON-NLS-1$
	String TRACING_NONE = "[NONE]"; //$NON-NLS-1$
	String CONFIG_USE_DEFAULT_AREA = "useDefaultConfigArea"; //$NON-NLS-1$
	String CONFIG_LOCATION = "configuration_location"; //$NON-NLS-1$
	String CONFIG_CLEAR_AREA = "clearConfig"; //$NON-NLS-1$
	String CONFIG_GENERATE_DEFAULT = "useDefaultConfig"; //$NON-NLS-1$
	String CONFIG_TEMPLATE_LOCATION = "templateConfig"; //$NON-NLS-1$
	String PRODUCT_FILE = "productFile"; //$NON-NLS-1$
	String OSGI_FRAMEWORK_ID = "osgi_framework_id"; //$NON-NLS-1$
	String DEFAULT_AUTO_START = "default_auto_start"; //$NON-NLS-1$
	String DEFAULT_START_LEVEL = "default_start_level"; //$NON-NLS-1$
	String WORKSPACE_BUNDLES = "workspace_bundles"; //$NON-NLS-1$
	String TARGET_BUNDLES = "target_bundles"; //$NON-NLS-1$
	String DEFINED_TARGET = "defined_target"; //$NON-NLS-1$
	String SHOW_SELECTED_ONLY = "show_selected_only"; //$NON-NLS-1$
	String TAB_BUNDLES_ID = "org.eclipse.pde.ui.launch.tab.osgi.bundles"; //$NON-NLS-1$
	String TAB_CONFIGURATION_ID = "org.eclipse.pde.ui.launch.tab.configuration"; //$NON-NLS-1$
	String TAB_MAIN_ID = "org.eclipse.pde.ui.launch.tab.main"; //$NON-NLS-1$
	String TAB_OSGI_SETTINGS_ID = "org.eclipse.pde.ui.launch.tab.osgi.settings"; //$NON-NLS-1$
	String TAB_PLUGIN_JUNIT_MAIN_ID = "org.eclipse.pde.ui.launch.tab.junit.main"; //$NON-NLS-1$
	String TAB_PLUGINS_ID = "org.eclipse.pde.ui.launch.tab.plugins"; //$NON-NLS-1$
	String TAB_TRACING_ID = "org.eclipse.pde.ui.launch.tab.tracing"; //$NON-NLS-1$
	String TAB_TEST_ID = "org.eclipse.pde.ui.launch.tab.test"; //$NON-NLS-1$
	String OSGI_CONFIGURATION_TYPE = "org.eclipse.pde.ui.EquinoxLauncher"; //$NON-NLS-1$
	String RUN_IN_UI_THREAD = "run_in_ui_thread"; //$NON-NLS-1$
	String ECLIPSE_APPLICATION_LAUNCH_CONFIGURATION_TYPE = "org.eclipse.pde.ui.RuntimeWorkbench"; //$NON-NLS-1$
	String GENERATE_PROFILE = "generateProfile"; //$NON-NLS-1$
	String SELECTED_FEATURES = "selected_features"; //$NON-NLS-1$
	String USE_CUSTOM_FEATURES = "useCustomFeatures"; //$NON-NLS-1$
	String FEATURE_DEFAULT_LOCATION = "featureDefaultLocation"; //$NON-NLS-1$
	String FEATURE_PLUGIN_RESOLUTION = "featurePluginResolution"; //$NON-NLS-1$
	String LOCATION_DEFAULT = "default"; //$NON-NLS-1$
	String LOCATION_EXTERNAL = "external"; //$NON-NLS-1$
	String LOCATION_WORKSPACE = "workspace"; //$NON-NLS-1$
	String ADDITIONAL_PLUGINS = "additional_plugins"; //$NON-NLS-1$

	// ------------------------------------------------------------------------------------
	// see org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants
	// ------------------------------------------------------------------------------------
	public static final String ID_JAVA_APPLICATION = LaunchActivator.getDefault().getUniqueIdentifier() + ".localJavaApplication"; //$NON-NLS-1$
	public static final String ID_REMOTE_JAVA_APPLICATION = LaunchActivator.getDefault().getUniqueIdentifier() + ".remoteJavaApplication"; //$NON-NLS-1$
	public static final String ID_JAVA_APPLET = LaunchActivator.getDefault().getUniqueIdentifier() + ".javaApplet"; //$NON-NLS-1$
	public static final String ID_SOCKET_ATTACH_VM_CONNECTOR = LaunchActivator.getDefault().getUniqueIdentifier() + ".socketAttachConnector"; //$NON-NLS-1$
	public static final String ID_SOCKET_LISTEN_VM_CONNECTOR = LaunchActivator.getDefault().getUniqueIdentifier() + ".socketListenConnector"; //$NON-NLS-1$
	public static final String ID_JAVA_PROCESS_TYPE = "java"; //$NON-NLS-1$
	public static final String ATTR_PROJECT_NAME = LaunchActivator.getDefault().getUniqueIdentifier() + ".PROJECT_ATTR"; //$NON-NLS-1$
	public static final String ATTR_MAIN_TYPE_NAME = LaunchActivator.getDefault().getUniqueIdentifier() + ".MAIN_TYPE"; //$NON-NLS-1$
	public static final String ATTR_STOP_IN_MAIN = LaunchActivator.getDefault().getUniqueIdentifier() + ".STOP_IN_MAIN"; //$NON-NLS-1$
	public static final String ATTR_PROGRAM_ARGUMENTS = LaunchActivator.getDefault().getUniqueIdentifier() + ".PROGRAM_ARGUMENTS"; //$NON-NLS-1$
	public static final String ATTR_VM_ARGUMENTS = LaunchActivator.getDefault().getUniqueIdentifier() + ".VM_ARGUMENTS"; //$NON-NLS-1$
	// public static final String ATTR_WORKING_DIRECTORY = LaunchActivator.getDefault().getUniqueIdentifier() + ".WORKING_DIRECTORY"; //$NON-NLS-1$
	public static final String WORKING_DIRECTORY = "working_directory"; //$NON-NLS-1$
	// public static final String ATTR_JRE_CONTAINER_PATH = JavaRuntime.JRE_CONTAINER;
	public static final String ATTR_VM_INSTALL_NAME = LaunchActivator.getDefault().getUniqueIdentifier() + ".VM_INSTALL_NAME"; //$NON-NLS-1$
	public static final String ATTR_VM_INSTALL_TYPE = LaunchActivator.getDefault().getUniqueIdentifier() + ".VM_INSTALL_TYPE_ID"; //$NON-NLS-1$
	public static final String ATTR_VM_INSTALL_TYPE_SPECIFIC_ATTRS_MAP = LaunchActivator.getDefault().getUniqueIdentifier() + "VM_INSTALL_TYPE_SPECIFIC_ATTRS_MAP"; //$NON-NLS-1$
	public static final String ATTR_VM_CONNECTOR = LaunchActivator.getDefault().getUniqueIdentifier() + ".VM_CONNECTOR_ID"; //$NON-NLS-1$
	public static final String ATTR_CLASSPATH = LaunchActivator.getDefault().getUniqueIdentifier() + ".CLASSPATH"; //$NON-NLS-1$
	public static final String ATTR_DEFAULT_CLASSPATH = LaunchActivator.getDefault().getUniqueIdentifier() + ".DEFAULT_CLASSPATH"; //$NON-NLS-1$
	public static final String ATTR_CLASSPATH_PROVIDER = LaunchActivator.getDefault().getUniqueIdentifier() + ".CLASSPATH_PROVIDER"; //$NON-NLS-1$
	public static final String ATTR_SOURCE_PATH = LaunchActivator.getDefault().getUniqueIdentifier() + ".SOURCE_PATH"; //$NON-NLS-1$
	public static final String ATTR_DEFAULT_SOURCE_PATH = LaunchActivator.getDefault().getUniqueIdentifier() + ".DEFAULT_SOURCE_PATH"; //$NON-NLS-1$
	public static final String ATTR_SOURCE_PATH_PROVIDER = LaunchActivator.getDefault().getUniqueIdentifier() + ".SOURCE_PATH_PROVIDER"; //$NON-NLS-1$
	public static final String ATTR_ALLOW_TERMINATE = LaunchActivator.getDefault().getUniqueIdentifier() + ".ALLOW_TERMINATE"; //$NON-NLS-1$
	public static final String ATTR_JAVA_COMMAND = LaunchActivator.getDefault().getUniqueIdentifier() + ".JAVA_COMMAND"; //$NON-NLS-1$
	public static final String ATTR_CONNECT_MAP = LaunchActivator.getDefault().getUniqueIdentifier() + ".CONNECT_MAP"; //$NON-NLS-1$
	public static final String ATTR_APPLET_WIDTH = LaunchActivator.getDefault().getUniqueIdentifier() + ".APPLET_WIDTH"; //$NON-NLS-1$
	public static final String ATTR_APPLET_HEIGHT = LaunchActivator.getDefault().getUniqueIdentifier() + ".APPLET_HEIGHT"; //$NON-NLS-1$
	public static final String ATTR_APPLET_NAME = LaunchActivator.getDefault().getUniqueIdentifier() + ".APPLET_NAME"; //$NON-NLS-1$
	public static final String ATTR_APPLET_PARAMETERS = LaunchActivator.getDefault().getUniqueIdentifier() + ".APPLET_PARAMETERS"; //$NON-NLS-1$
	public static final String ATTR_APPLET_APPLETVIEWER_CLASS = LaunchActivator.getDefault().getUniqueIdentifier() + ".APPLET_APPLETVIEWER_CLASS"; //$NON-NLS-1$
	public static final String ATTR_BOOTPATH_PREPEND = LaunchActivator.getDefault().getUniqueIdentifier() + ".-Xbootclasspath/p:"; //$NON-NLS-1$
	public static final String ATTR_BOOTPATH = LaunchActivator.getDefault().getUniqueIdentifier() + ".-Xbootclasspath:"; //$NON-NLS-1$
	public static final String ATTR_BOOTPATH_APPEND = LaunchActivator.getDefault().getUniqueIdentifier() + ".-Xbootclasspath/a:"; //$NON-NLS-1$
	public static final String ATTR_USE_START_ON_FIRST_THREAD = LaunchActivator.getDefault().getUniqueIdentifier() + ".ATTR_USE_START_ON_FIRST_THREAD"; //$NON-NLS-1$
	public static final int ERR_UNSPECIFIED_PROJECT = 100;
	public static final int ERR_UNSPECIFIED_MAIN_TYPE = 101;
	public static final int ERR_UNSPECIFIED_VM_INSTALL_TYPE = 102;
	public static final int ERR_UNSPECIFIED_VM_INSTALL = 103;
	public static final int ERR_VM_INSTALL_TYPE_DOES_NOT_EXIST = 104;
	public static final int ERR_VM_INSTALL_DOES_NOT_EXIST = 105;
	public static final int ERR_VM_RUNNER_DOES_NOT_EXIST = 106;
	public static final int ERR_NOT_A_JAVA_PROJECT = 107;
	public static final int ERR_WORKING_DIRECTORY_DOES_NOT_EXIST = 108;
	public static final int ERR_UNSPECIFIED_HOSTNAME = 109;
	public static final int ERR_INVALID_HOSTNAME = 110;
	public static final int ERR_UNSPECIFIED_PORT = 111;
	public static final int ERR_INVALID_PORT = 112;
	public static final int ERR_REMOTE_VM_CONNECTION_FAILED = 113;
	public static final int ERR_SHARED_MEMORY_CONNECTOR_UNAVAILABLE = 114;
	public static final int ERR_WORKING_DIRECTORY_NOT_SUPPORTED = 115;
	public static final int ERR_VM_LAUNCH_ERROR = 116;
	public static final int ERR_VM_CONNECT_TIMEOUT = 117;
	public static final int ERR_NO_SOCKET_AVAILABLE = 118;
	public static final int ERR_CONNECTOR_NOT_AVAILABLE = 119;
	public static final int ERR_CONNECTION_FAILED = 120;
	public static final int ERR_NOT_AN_APPLET = 121;
	public static final int ERR_UNSPECIFIED_LAUNCH_CONFIG = 122;
	public static final int ERR_COULD_NOT_BUILD_HTML = 123;
	public static final int ERR_PROJECT_CLOSED = 124;
	public static final int ERR_INTERNAL_ERROR = 150;
	public static final String DEFAULT_APPLETVIEWER_CLASS = "sun.applet.AppletViewer"; //$NON-NLS-1$
	public static final int DETAIL_CONFIG_READY_TO_ACCEPT_REMOTE_VM_CONNECTION = 1001;

	// ------------------------------------------------------------------------------------
	// see org.eclipse.debug.core.model.IProcess
	// ------------------------------------------------------------------------------------
	public final static String ATTR_CMDLINE = LaunchActivator.getDefault().getUniqueIdentifier() + ".ATTR_CMDLINE"; //$NON-NLS-1$
	public final static String ATTR_PROCESS_TYPE = LaunchActivator.getDefault().getUniqueIdentifier() + ".ATTR_PROCESS_TYPE"; //$NON-NLS-1$
	public final static String ATTR_PROCESS_LABEL = LaunchActivator.getDefault().getUniqueIdentifier() + ".ATTR_PROCESS_LABEL"; //$NON-NLS-1$

}
