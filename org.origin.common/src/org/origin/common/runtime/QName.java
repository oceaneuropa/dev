package org.origin.common.runtime;

public class QName {

	public static final String DEFAULT_QUALIFIER = "";
	protected String qualifier = null;
	protected String localName = null;

	/**
	 * 
	 * @param qualifier
	 * @param localName
	 */
	public QName(String qualifier, String localName) {
		assert (localName != null && !localName.isEmpty()) : "localName is empty";
		if (qualifier == null) {
			qualifier = DEFAULT_QUALIFIER;
		}
		this.qualifier = qualifier;
		this.localName = localName;
	}

	public String getQualifier() {
		return qualifier;
	}

	public String getLocalName() {
		return localName;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof QName)) {
			return false;
		}

		QName qName = (QName) obj;

		if (qualifier == null && qName.getQualifier() != null) {
			return false;
		}
		if (qualifier != null && !qualifier.equals(qName.getQualifier())) {
			return false;
		}

		return localName.equals(qName.getLocalName());
	}

	@Override
	public int hashCode() {
		return (qualifier == null ? 0 : qualifier.hashCode()) + localName.hashCode();
	}

	@Override
	public String toString() {
		return (getQualifier() == null ? "" : getQualifier() + ':') + getLocalName();
	}

}
