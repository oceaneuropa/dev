package tutorial.example2;

import java.util.Hashtable;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import tutorial.example2.service.DictionaryService;

/**
 * English dictionary (1.0.0)
 * 
 * http://felix.apache.org/documentation/tutorials-examples-and-presentations/apache-felix-osgi-tutorial/apache-felix-tutorial-example-2.html
 *
 *
 * This class implements a simple bundle that uses the bundle context to register an English language dictionary service with the OSGi framework. The
 * dictionary service interface is defined in a separate class file and is implemented by an inner class.
 */
public class Activator implements BundleActivator {

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	/**
	 * Registers an instance of a dictionary service using the bundle context; attaches properties to the service that can be queried when performing
	 * a service look-up.
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		Activator.context = context;

		Hashtable<String, String> props = new Hashtable<String, String>();
		props.put("Language", "English");
		context.registerService(DictionaryService.class.getName(), new DictionaryImpl(), props);
	}

	/**
	 * Does nothing since the framework will automatically unregister any registered services.
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		// NOTE: The service is automatically unregistered.
		Activator.context = null;
	}

	/**
	 * A private inner class that implements a dictionary service; see DictionaryService for details of the service.
	 **/
	private static class DictionaryImpl implements DictionaryService {
		// The set of words contained in the dictionary.
		String[] m_dictionary = { "welcome", "to", "the", "osgi", "tutorial" };

		/**
		 * Implements DictionaryService.checkWord(). Determines if the passed in word is contained in the dictionary.
		 * 
		 * @param word
		 *            the word to be checked.
		 * @return true if the word is in the dictionary, false otherwise.
		 */
		@Override
		public boolean checkWord(String word) {
			word = word.toLowerCase();
			// This is very inefficient
			for (int i = 0; i < m_dictionary.length; i++) {
				if (m_dictionary[i].equals(word)) {
					return true;
				}
			}
			return false;
		}
	}

}
