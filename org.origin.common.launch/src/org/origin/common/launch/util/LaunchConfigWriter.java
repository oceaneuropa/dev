package org.origin.common.launch.util;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.origin.common.io.FileUtil;
import org.origin.common.launch.impl.LaunchConfigAttributes;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class LaunchConfigWriter {

	/**
	 * 
	 * @param configData
	 * @param file
	 */
	public void write(LaunchConfigAttributes configData, File file) {
		String xml = null;
		try {
			xml = getAsXML(configData);

			if (xml != null) {
				if (!file.getParentFile().exists()) {
					file.getParentFile().mkdirs();
				}

				if (!file.exists()) {
					file.createNewFile();
				}

				FileUtil.copyBytesToFile(xml.getBytes(), file);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns the content of this info as XML.
	 * 
	 * @see LaunchConfigurationInfo
	 * 
	 * @param data
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws TransformerException
	 *             * @return
	 */
	@SuppressWarnings("unchecked")
	public String getAsXML(LaunchConfigAttributes data) throws IOException, ParserConfigurationException, TransformerException {
		Document doc = LaunchConfigPersistence.INSTANCE.getDocument();
		Element configRootElement = doc.createElement(LaunchConfigAttributes.LAUNCH_CONFIGURATION);
		doc.appendChild(configRootElement);

		configRootElement.setAttribute(LaunchConfigAttributes.TYPE, data.getTypeId());

		for (String key : data.getAttributeMap().keySet()) {
			if (key == null) {
				// throw new DebugException(new Status(IStatus.ERROR, DebugPlugin.getUniqueIdentifier(), DebugException.REQUEST_FAILED,
				// DebugCoreMessages.LaunchConfigurationInfo_36, null));
				throw new IOException("Current attribute key is null.");
			}
			Object value = data.getAttributeMap().get(key);
			if (value == null) {
				continue;
			}

			Element element = null;
			String valueString = null;
			if (value instanceof String) {
				valueString = (String) value;
				element = data.createKeyValueElement(doc, LaunchConfigAttributes.STRING_ATTRIBUTE, key, valueString);
			} else if (value instanceof Integer) {
				valueString = ((Integer) value).toString();
				element = data.createKeyValueElement(doc, LaunchConfigAttributes.INT_ATTRIBUTE, key, valueString);
			} else if (value instanceof Boolean) {
				valueString = ((Boolean) value).toString();
				element = data.createKeyValueElement(doc, LaunchConfigAttributes.BOOLEAN_ATTRIBUTE, key, valueString);
			} else if (value instanceof List) {
				element = data.createListElement(doc, LaunchConfigAttributes.LIST_ATTRIBUTE, key, (List<String>) value);
			} else if (value instanceof Map) {
				element = data.createMapElement(doc, LaunchConfigAttributes.MAP_ATTRIBUTE, key, (Map<String, String>) value);
			} else if (value instanceof Set) {
				element = data.createSetElement(doc, LaunchConfigAttributes.SET_ATTRIBUTE, key, (Set<String>) value);
			}
			configRootElement.appendChild(element);
		}

		return LaunchConfigPersistence.INSTANCE.serializeDocument(doc);
	}

}
