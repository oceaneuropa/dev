package org.orbit.component.connector.tier3.transferagent;

import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import org.orbit.component.api.tier3.domain.TransferAgentConfig;
import org.orbit.component.api.tier3.transferagent.TransferAgentConnector;
import org.origin.mgm.client.api.IndexService;
import org.origin.mgm.client.connector.ServiceConnectorFactory;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

/**
 * https://stackoverflow.com/questions/36861785/multiple-osgi-services
 *
 */
public class TransferAgentConnectorFactoryImpl implements ServiceConnectorFactory<TransferAgentConnector> {

	protected AtomicBoolean isStarted = new AtomicBoolean(false);
	protected IndexService indexService;
	protected ServiceRegistration<?> serviceRegistration;

	/**
	 * 
	 * @param indexService
	 */
	public TransferAgentConnectorFactoryImpl(IndexService indexService) {
		this.indexService = indexService;
	}

	/**
	 * 
	 * @param bundleContext
	 */
	public synchronized void start(BundleContext bundleContext) {
		if (this.isStarted.get()) {
			return;
		}
		this.isStarted.set(true);

		Hashtable<String, Object> props = new Hashtable<String, Object>();
		props.put("type", TransferAgentConnector.class.getName());
		this.serviceRegistration = bundleContext.registerService(ServiceConnectorFactory.class, this, props);
	}

	/**
	 * 
	 * @param bundleContext
	 */
	public synchronized void stop(BundleContext bundleContext) {
		if (!this.isStarted.compareAndSet(true, false)) {
			return;
		}

		if (this.serviceRegistration != null) {
			this.serviceRegistration.unregister();
			this.serviceRegistration = null;
		}
	}

	@Override
	public TransferAgentConnector create(Map<Object, Object> properties) {
		TransferAgentConfig transferAgentConfig = null;
		Object object = properties.get(TransferAgentConfig.class.getName());
		if (object instanceof TransferAgentConfig) {
			transferAgentConfig = (TransferAgentConfig) object;
		}
		if (transferAgentConfig == null) {
			System.err.println(getClass().getSimpleName() + ".getServiceConnector() TransferAgentConfig cannot be found from properties.");
			return null;
		}
		return new TransferAgentConnectorImpl(this.indexService, transferAgentConfig);
	}

}

// public void start(BundleContext context) throws Exception {
// Hashtable excelProperty = new Hashtable();
// excelProperty.put("type", "excel");
// excelServiceRegistration = context.registerService(ExportService.class.getName(), new ExcelExportServiceImpl(), excelProperty);
// Hashtable pdfProperty = new Hashtable();
// pdfProperty.put("type", "pdf");
// pdfServiceRegistration = context.registerService(ExportService.class.getName(), new PdfExportServiceImpl(), pdfProperty);
// }

// public static void startBundle(BundleContext context) throws InvalidSyntaxException {
// String EXCEL_FILTER_STRING = "(&(" + Constants.OBJECTCLASS + "=com.stpl.excel.api.ExportService)" + "(type=excel))";
// String PDF_FILTER_STRING = "(&(" + Constants.OBJECTCLASS + "=com.stpl.excel.api.ExportService)" + "(type=pdf))";
// Filter excelFilter = context.createFilter(EXCEL_FILTER_STRING);
// Filter pdfFilter = context.createFilter(PDF_FILTER_STRING);
// ServiceTracker excelService = new ServiceTracker(context, excelFilter, null);
// ServiceTracker pdfService = new ServiceTracker(context, pdfFilter, null);
// excelService.open();
// pdfService.open();
// }

// String machineId = getMachineId(properties);
// String transferAgentId = getTransferAgentId(properties);
// if (machineId == null) {
// System.err.println(getClass().getSimpleName() + ".getServiceConnector() machineId is null.");
// return null;
// }
// if (transferAgentId == null) {
// System.err.println(getClass().getSimpleName() + ".getServiceConnector() transferAgentId is null.");
// return null;
// }

/// **
// *
// * @param properties
// * @return
// */
// protected String getMachineId(Map<Object, Object> properties) {
// String machineId = null;
// Object object = properties.get("machineId");
// if (object != null) {
// machineId = object.toString();
// }
// return machineId;
// }
//
/// **
// *
// * @param properties
// * @return
// */
// protected String getTransferAgentId(Map<Object, Object> properties) {
// String transferAgentId = null;
// Object object = properties.get("transferAgentId");
// if (object != null) {
// transferAgentId = object.toString();
// }
// return transferAgentId;
// }

// /**
// *
// * @param properties
// * @return
// */
// protected TransferAgentConfig getTransferAgentConfig(Map<Object, Object> properties) {
// TransferAgentConfig transferAgentConfig = null;
// Object object = properties.get(TransferAgentConfig.class.getName());
// if (object instanceof TransferAgentConfig) {
// transferAgentConfig = (TransferAgentConfig) object;
// }
// return transferAgentConfig;
// }