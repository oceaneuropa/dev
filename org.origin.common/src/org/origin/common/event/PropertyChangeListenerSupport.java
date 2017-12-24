package org.origin.common.event;

public class PropertyChangeListenerSupport {

	protected ListenerList listenersList = new ListenerList();

	/**
	 * 
	 * @param listener
	 */
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		if (listener != null) {
			boolean found = false;
			for (Object currListener : this.listenersList.getListeners()) {
				if (listener.equals(currListener)) {
					found = true;
					break;
				}
			}
			if (!found) {
				this.listenersList.add(listener);
			}
		}
	}

	/**
	 * 
	 * @param listener
	 */
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		if (listener != null) {
			this.listenersList.remove(listener);
		}
	}

	/**
	 * 
	 * @param event
	 */
	public void notifyPropertyChangeEvent(PropertyChangeEvent event) {
		Object[] listeners = this.listenersList.getListeners();
		if (listeners != null) {
			for (int i = 0; i < listeners.length; i++) {
				((PropertyChangeListener) listeners[i]).notifyEvent(event);
			}
		}
	}

}

/// **
// *
// * @param source
// * @param name
// * @param oldValue
// * @param newValue
// */
// public void notifyPropertyChangeEvent(Object source, String name, Object oldValue, Object newValue) {
// PropertyChangeEvent event = new PropertyChangeEvent(source, name, oldValue, newValue);
// Object[] listeners = this.listenersList.getListeners();
// if (listeners != null) {
// for (int i = 0; i < listeners.length; i++) {
// ((PropertyChangeListener) listeners[i]).notifyEvent(event);
// }
// }
// }
