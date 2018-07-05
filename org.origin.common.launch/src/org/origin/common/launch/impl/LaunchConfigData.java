package org.origin.common.launch.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class LaunchConfigData {

	/**
	 * Constants for XML element names and attributes
	 */
	public static final String KEY = "key"; //$NON-NLS-1$
	public static final String VALUE = "value"; //$NON-NLS-1$
	public static final String SET_ENTRY = "setEntry"; //$NON-NLS-1$
	public static final String LAUNCH_CONFIGURATION = "launchConfiguration"; //$NON-NLS-1$
	public static final String MAP_ENTRY = "mapEntry"; //$NON-NLS-1$
	public static final String LIST_ENTRY = "listEntry"; //$NON-NLS-1$
	public static final String SET_ATTRIBUTE = "setAttribute"; //$NON-NLS-1$
	public static final String MAP_ATTRIBUTE = "mapAttribute"; //$NON-NLS-1$
	public static final String LIST_ATTRIBUTE = "listAttribute"; //$NON-NLS-1$
	public static final String BOOLEAN_ATTRIBUTE = "booleanAttribute"; //$NON-NLS-1$
	public static final String INT_ATTRIBUTE = "intAttribute"; //$NON-NLS-1$
	public static final String STRING_ATTRIBUTE = "stringAttribute"; //$NON-NLS-1$
	public static final String TYPE = "type"; //$NON-NLS-1$

	/**
	 * This configurations attribute table. Keys are Strings and values are one of String, Integer, Boolean, Set<String>, List<String> or Map<String, String>.
	 */
	protected Map<String, Object> attributes;
	protected String typeId;

	/**
	 * Constructs a new empty info
	 */
	public LaunchConfigData() {
		setAttributeMap(new LinkedHashMap<String, Object>());
	}

	public Map<String, Object> getAttributeMap() {
		return this.attributes;
	}

	public void setAttributeMap(Map<String, Object> attributes) {
		this.attributes = attributes;
	}

	/**
	 * Sets the attributes in this info to those in the given map.
	 *
	 * @param map
	 *            the {@link Map} of attributes to set
	 */
	public void setAttributes(Map<String, ?> map) {
		if (map == null) {
			setAttributeMap(new LinkedHashMap<String, Object>());
			return;
		}
		setAttributeMap(new LinkedHashMap<String, Object>(map));
	}

	public String getStringAttribute(String key, String defaultValue) throws IOException {
		Object attr = getAttributeMap().get(key);
		if (attr != null) {
			if (attr instanceof String) {
				return (String) attr;
			}
			// throw new DebugException(new Status(IStatus.ERROR, DebugPlugin.getUniqueIdentifier(), DebugException.REQUEST_FAILED,
			// MessageFormat.format(DebugCoreMessages.LaunchConfigurationInfo_Attribute__0__is_not_of_type_java_lang_String__1, new Object[] { key }), null));
			throw new IOException();
		}
		return defaultValue;
	}

	/**
	 * Returns the <code>int</code> attribute with the given key or the given default value if undefined.
	 * 
	 * @param key
	 *            the name of the attribute
	 * @param defaultValue
	 *            the default value to return if the key does not appear in the attribute table
	 *
	 * @return attribute specified by given key or the defaultValue if undefined
	 * @throws CoreException
	 *             if the attribute with the given key exists but is not an <code>int</code>
	 */
	public int getIntAttribute(String key, int defaultValue) throws IOException {
		Object attr = getAttributeMap().get(key);
		if (attr != null) {
			if (attr instanceof Integer) {
				return ((Integer) attr).intValue();
			}
			// throw new DebugException(new Status(IStatus.ERROR, DebugPlugin.getUniqueIdentifier(), DebugException.REQUEST_FAILED,
			// MessageFormat.format(DebugCoreMessages.LaunchConfigurationInfo_Attribute__0__is_not_of_type_int__2, new Object[] { key }), null));
			throw new IOException();
		}
		return defaultValue;
	}

	/**
	 * Returns the <code>boolean</code> attribute with the given key or the given default value if undefined.
	 * 
	 * @param key
	 *            the name of the attribute
	 * @param defaultValue
	 *            the default value to return if the key does not appear in the attribute table
	 *
	 * @return attribute specified by given key or the defaultValue if undefined
	 * @throws CoreException
	 *             if the attribute with the given key exists but is not a <code>boolean</code>
	 */
	public boolean getBooleanAttribute(String key, boolean defaultValue) throws IOException {
		Object attr = getAttributeMap().get(key);
		if (attr != null) {
			if (attr instanceof Boolean) {
				return ((Boolean) attr).booleanValue();
			}
			// throw new DebugException(new Status(IStatus.ERROR, DebugPlugin.getUniqueIdentifier(), DebugException.REQUEST_FAILED,
			// MessageFormat.format(DebugCoreMessages.LaunchConfigurationInfo_Attribute__0__is_not_of_type_boolean__3, new Object[] { key }), null));
			throw new IOException();
		}
		return defaultValue;
	}

	/**
	 * Returns the <code>java.util.List</code> attribute with the given key or the given default value if undefined.
	 * 
	 * @param key
	 *            the name of the attribute
	 * @param defaultValue
	 *            the default value to return if the key does not appear in the attribute table
	 *
	 * @return attribute specified by given key or the defaultValue if undefined
	 * @throws CoreException
	 *             if the attribute with the given key exists but is not a <code>java.util.List</code>
	 */
	@SuppressWarnings("unchecked")
	public List<String> getListAttribute(String key, List<String> defaultValue) throws IOException {
		Object attr = getAttributeMap().get(key);
		if (attr != null) {
			if (attr instanceof List) {
				return (List<String>) attr;
			}
			// throw new DebugException(new Status(IStatus.ERROR, DebugPlugin.getUniqueIdentifier(), DebugException.REQUEST_FAILED,
			// MessageFormat.format(DebugCoreMessages.LaunchConfigurationInfo_Attribute__0__is_not_of_type_java_util_List__1, new Object[] { key }), null));
			throw new IOException();
		}
		return defaultValue;
	}

	/**
	 * Returns the <code>java.util.Set</code> attribute with the given key or the given default value if undefined.
	 *
	 * @param key
	 *            the name of the attribute
	 * @param defaultValue
	 *            the default value to return if the key does not exist in the attribute table
	 *
	 * @return attribute specified by given key or the defaultValue if undefined
	 * @throws CoreException
	 *             if the attribute with the given key exists but is not a <code>java.util.Set</code>
	 *
	 * @since 3.3
	 */
	@SuppressWarnings("unchecked")
	public Set<String> getSetAttribute(String key, Set<String> defaultValue) throws IOException {
		Object attr = getAttributeMap().get(key);
		if (attr != null) {
			if (attr instanceof Set) {
				return (Set<String>) attr;
			}
			// throw new DebugException(new Status(IStatus.ERROR, DebugPlugin.getUniqueIdentifier(), DebugException.REQUEST_FAILED,
			// MessageFormat.format(DebugCoreMessages.LaunchConfigurationInfo_35, new Object[] { key }), null));
			throw new IOException();
		}
		return defaultValue;
	}

	/**
	 * Returns the <code>java.util.Map</code> attribute with the given key or the given default value if undefined.
	 * 
	 * @param key
	 *            the name of the attribute
	 * @param defaultValue
	 *            the default value to return if the key does not exist in the attribute table
	 *
	 * @return attribute specified by given key or the defaultValue if undefined
	 * @throws CoreException
	 *             if the attribute with the given key exists but is not a <code>java.util.Map</code>
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> getMapAttribute(String key, Map<String, String> defaultValue) throws IOException {
		Object attr = getAttributeMap().get(key);
		if (attr != null) {
			if (attr instanceof Map) {
				return (Map<String, String>) attr;
			}
			// throw new DebugException(new Status(IStatus.ERROR, DebugPlugin.getUniqueIdentifier(), DebugException.REQUEST_FAILED,
			// MessageFormat.format(DebugCoreMessages.LaunchConfigurationInfo_Attribute__0__is_not_of_type_java_util_Map__1, new Object[] { key }), null));
			throw new IOException();
		}
		return defaultValue;
	}

	public String getTypeId() {
		return this.typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	/**
	 * Returns a copy of this info object
	 *
	 * @return copy of this info
	 */
	public LaunchConfigData getCopy() {
		LaunchConfigData copy = new LaunchConfigData();
		copy.setTypeId(getTypeId());
		copy.setAttributeMap(getAttributes());
		return copy;
	}

	/**
	 * Returns a copy of this info's attribute map.
	 *
	 * @return a copy of this info's attribute map
	 */
	public Map<String, Object> getAttributes() {
		return new LinkedHashMap<String, Object>(getAttributeMap());
	}

	/**
	 * Sets the given attribute to the given value. Only working copy's should use this API.
	 *
	 * @param key
	 *            attribute key
	 * @param value
	 *            attribute value
	 */
	public void setAttribute(String key, Object value) {
		if (value == null) {
			getAttributeMap().remove(key);
		} else {
			getAttributeMap().put(key, value);
		}
	}

	/**
	 * Helper method that creates a 'key value' element of the specified type with the specified attribute values.
	 * 
	 * @param doc
	 *            the {@link Document}
	 * @param elementType
	 *            the {@link Element} type to create
	 * @param key
	 *            the {@link Element} key
	 * @param value
	 *            the {@link Element} value
	 * @return the new {@link Element}
	 */
	public Element createKeyValueElement(Document doc, String elementType, String key, String value) {
		Element element = doc.createElement(elementType);
		element.setAttribute(KEY, key);
		element.setAttribute(VALUE, value);
		return element;
	}

	/**
	 * Creates a new <code>Element</code> for the specified <code>java.util.List</code>
	 *
	 * @param doc
	 *            the doc to add the element to
	 * @param elementType
	 *            the type of the element
	 * @param listKey
	 *            the key for the element
	 * @param list
	 *            the list to fill the new element with
	 * @return the new element
	 */
	public Element createListElement(Document doc, String elementType, String listKey, List<String> list) {
		Element listElement = doc.createElement(elementType);
		listElement.setAttribute(KEY, listKey);
		for (String value : list) {
			Element element = doc.createElement(LIST_ENTRY);
			element.setAttribute(VALUE, value);
			listElement.appendChild(element);
		}
		return listElement;
	}

	/**
	 * Creates a new <code>Element</code> for the specified <code>java.util.Set</code>
	 *
	 * @param doc
	 *            the doc to add the element to
	 * @param elementType
	 *            the type of the element
	 * @param setKey
	 *            the key for the element
	 * @param set
	 *            the set to fill the new element with
	 * @return the new element
	 *
	 * @since 3.3
	 */
	public Element createSetElement(Document doc, String elementType, String setKey, Set<String> set) {
		Element setElement = doc.createElement(elementType);
		setElement.setAttribute(KEY, setKey);
		// persist in sorted order
		List<String> list = new ArrayList<String>(set);
		Collections.sort(list);
		Element element = null;
		for (String str : list) {
			element = doc.createElement(SET_ENTRY);
			element.setAttribute(VALUE, str);
			setElement.appendChild(element);
		}
		return setElement;
	}

	/**
	 * Creates a new <code>Element</code> for the specified <code>java.util.Map</code>
	 *
	 * @param doc
	 *            the doc to add the element to
	 * @param elementType
	 *            the type of the element
	 * @param mapKey
	 *            the key for the element
	 * @param map
	 *            the map to fill the new element with
	 * @return the new element
	 *
	 */
	public Element createMapElement(Document doc, String elementType, String mapKey, Map<String, String> map) {
		Element mapElement = doc.createElement(elementType);
		mapElement.setAttribute(KEY, mapKey);
		// persist in sorted order based on keys
		List<String> keys = new ArrayList<String>(map.keySet());
		Collections.sort(keys);
		for (String key : keys) {
			String value = map.get(key);
			Element element = doc.createElement(MAP_ENTRY);
			element.setAttribute(KEY, key);
			element.setAttribute(VALUE, value);
			mapElement.appendChild(element);
		}
		return mapElement;
	}

	/**
	 * Loads a <code>String</code> from the specified element into the local attribute mapping
	 * 
	 * @param element
	 *            the element to load from
	 * @throws CoreException
	 *             if a problem is encountered
	 */
	public void setStringAttribute(Element element) throws IOException {
		setAttribute(getKeyAttribute(element), getValueAttribute(element));
	}

	/**
	 * Loads an <code>Integer</code> from the specified element into the local attribute mapping
	 * 
	 * @param element
	 *            the element to load from
	 * @throws CoreException
	 *             if a problem is encountered
	 */
	public void setIntegerAttribute(Element element) throws IOException {
		setAttribute(getKeyAttribute(element), new Integer(getValueAttribute(element)));
	}

	/**
	 * Loads a <code>Boolean</code> from the specified element into the local attribute mapping
	 * 
	 * @param element
	 *            the element to load from
	 * @throws CoreException
	 *             if a problem is encountered
	 */
	public void setBooleanAttribute(Element element) throws IOException {
		setAttribute(getKeyAttribute(element), Boolean.valueOf(getValueAttribute(element)));
	}

	/**
	 * Reads a <code>List</code> attribute from the specified XML node and loads it into the mapping of attributes
	 *
	 * @param element
	 *            the element to read the list attribute from
	 * @throws CoreException
	 *             if the element has an invalid format
	 */
	public void setListAttribute(Element element) throws IOException {
		String listKey = element.getAttribute(KEY);
		NodeList nodeList = element.getChildNodes();
		int entryCount = nodeList.getLength();
		List<String> list = new ArrayList<String>(entryCount);
		Node node = null;
		Element selement = null;
		for (int i = 0; i < entryCount; i++) {
			node = nodeList.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				selement = (Element) node;
				if (!selement.getNodeName().equalsIgnoreCase(LIST_ENTRY)) {
					throw getInvalidFormatDebugException();
				}
				list.add(getValueAttribute(selement));
			}
		}
		setAttribute(listKey, list);
	}

	/**
	 * Reads a <code>Set</code> attribute from the specified XML node and loads it into the mapping of attributes
	 *
	 * @param element
	 *            the element to read the set attribute from
	 * @throws CoreException
	 *             if the element has an invalid format
	 *
	 * @since 3.3
	 */
	public void setSetAttribute(Element element) throws IOException {
		String setKey = element.getAttribute(KEY);
		NodeList nodeList = element.getChildNodes();
		int entryCount = nodeList.getLength();
		Set<String> set = new LinkedHashSet<String>(entryCount);
		Node node = null;
		Element selement = null;
		for (int i = 0; i < entryCount; i++) {
			node = nodeList.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				selement = (Element) node;
				if (!selement.getNodeName().equalsIgnoreCase(SET_ENTRY)) {
					throw getInvalidFormatDebugException();
				}
				set.add(getValueAttribute(selement));
			}
		}
		setAttribute(setKey, set);
	}

	/**
	 * Reads a <code>Map</code> attribute from the specified XML node and loads it into the mapping of attributes
	 *
	 * @param element
	 *            the element to read the map attribute from
	 * @throws CoreException
	 *             if the element has an invalid format
	 */
	public void setMapAttribute(Element element) throws IOException {
		String mapKey = element.getAttribute(KEY);
		NodeList nodeList = element.getChildNodes();
		int entryCount = nodeList.getLength();
		Map<String, String> map = new LinkedHashMap<String, String>(entryCount);
		Node node = null;
		Element selement = null;
		for (int i = 0; i < entryCount; i++) {
			node = nodeList.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				selement = (Element) node;
				if (!selement.getNodeName().equalsIgnoreCase(MAP_ENTRY)) {
					throw getInvalidFormatDebugException();
				}
				map.put(getKeyAttribute(selement), getValueAttribute(selement));
			}
		}
		setAttribute(mapKey, map);
	}

	/**
	 * Returns the <code>String</code> representation of the 'key' attribute from the specified element
	 * 
	 * @param element
	 *            the element to read from
	 * @return the value
	 * @throws CoreException
	 *             if a problem is encountered
	 */
	public String getKeyAttribute(Element element) throws IOException {
		String key = element.getAttribute(KEY);
		if (key == null) {
			throw getInvalidFormatDebugException();
		}
		return key;
	}

	/**
	 * Returns the <code>String</code> representation of the 'value' attribute from the specified element
	 * 
	 * @param element
	 *            the element to read from
	 * @return the value
	 * @throws CoreException
	 *             if a problem is encountered
	 */
	public String getValueAttribute(Element element) throws IOException {
		String value = element.getAttribute(VALUE);
		if (value == null) {
			throw getInvalidFormatDebugException();
		}
		return value;
	}

	/**
	 * Returns an invalid format exception for reuse
	 * 
	 * @return an invalid format exception
	 */
	public IOException getInvalidFormatDebugException() {
		return new IOException("Invalid Format");
	}

	/**
	 * Returns whether the two attribute maps are equal, consulting registered comparator extensions.
	 * 
	 * @param map1
	 * @param map2
	 * @return
	 */
	public boolean compareAttributes(Map<String, Object> map1, Map<String, Object> map2) {
		// LaunchManager manager = (LaunchManager) DebugPlugin.getDefault().getLaunchManager();
		if (map1.size() == map2.size()) {
			Iterator<String> attributes = map1.keySet().iterator();
			while (attributes.hasNext()) {
				String key = attributes.next();
				// Object attr1 = map1.get(key);
				Object attr2 = map2.get(key);
				if (attr2 == null) {
					return false;
				}
				// Comparator<Object> comp = manager.getComparator(key);
				// Comparator<Object> comp = null;
				// if (comp == null) {
				// if (fgIsSun14x) {
				// if (attr2 instanceof String & attr1 instanceof String) {
				// // this is a hack for bug 110215, on SUN 1.4.x, \r
				// // is stripped off when the stream is written to the
				// // DOM
				// // this is not the case for 1.5.x, so to be safe we
				// // are stripping \r off all strings before we
				// // compare for equality
				// attr1 = ((String) attr1).replaceAll("\\r", ""); //$NON-NLS-1$ //$NON-NLS-2$
				// attr2 = ((String) attr2).replaceAll("\\r", ""); //$NON-NLS-1$ //$NON-NLS-2$
				// }
				// }
				// if (!attr1.equals(attr2)) {
				// return false;
				// }
				// } else {
				// if (comp.compare(attr1, attr2) != 0) {
				// return false;
				// }
				// }
			}
			return true;
		}
		return false;
	}

	public boolean hasAttribute(String attributeName) {
		return attributes.containsKey(attributeName);
	}

	public Object removeAttribute(String attributeName) {
		if (attributeName != null) {
			return attributes.remove(attributeName);
		}
		return null;
	}

	/**
	 * Two LaunchConfigurationInfo objects are equal if and only if they have the same type and they have the same set of attributes with the same values.
	 * 
	 */
	@Override
	public boolean equals(Object obj) {
		// Make sure it's a LaunchConfigurationInfo object
		if (!(obj instanceof LaunchConfigData)) {
			return false;
		}

		// Make sure the types are the same
		LaunchConfigData other = (LaunchConfigData) obj;
		if (!typeId.equals(other.getTypeId())) {
			return false;
		}

		// Make sure the attributes are the same
		return compareAttributes(this.attributes, other.getAttributeMap());
	}

	@Override
	public int hashCode() {
		return typeId.hashCode() + attributes.size();
	}

}

// private static boolean fgIsSun14x = false;
//
// static {
// String vendor = System.getProperty("java.vm.vendor"); //$NON-NLS-1$
// if (vendor.startsWith("Sun Microsystems")) { //$NON-NLS-1$
// String version = System.getProperty("java.vm.version"); //$NON-NLS-1$
// if (version.startsWith("1.4")) { //$NON-NLS-1$
// fgIsSun14x = true;
// }
// }
// }
