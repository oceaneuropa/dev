package tutorial.example4;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;

import tutorial.example2.service.DictionaryService;

/**
 * Dynamic dictionary client (1.0.0)
 *
 * http://felix.apache.org/documentation/tutorials-examples-and-presentations/apache-felix-osgi-tutorial/apache-felix-tutorial-example-4.html
 *
 */
public class Activator implements BundleActivator, ServiceListener {

	private static BundleContext context;

	// Bundle's context.
	private BundleContext m_context = null;
	// The service reference being used.
	private ServiceReference m_ref = null;
	// The service object being used.
	private DictionaryService m_dictionary = null;

	static BundleContext getContext() {
		return context;
	}

	/**
	 * Adds itself as a listener for service events, then queries for available dictionary services. If any dictionaries are found it gets a reference
	 * to the first one available and then starts its "word checking loop". If no dictionaries are found, then it just goes directly into its "word
	 * checking loop", but it will not be able to check any words until a dictionary service arrives; any arriving dictionary service will be
	 * automatically used by the client if a dictionary is not already in use. Once it has dictionary, it reads words from standard input and checks
	 * for their existence in the dictionary that it is using.
	 * 
	 * (NOTE: It is very bad practice to use the calling thread to perform a lengthy process like this; this is only done for the purpose of the
	 * tutorial.)
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		// Activator.context = context;

		m_context = context;

		// We synchronize while registering the service listener and performing our initial dictionary service lookup since we don't want to receive
		// service events when looking up the dictionary service, if one exists.
		synchronized (this) {
			// Listen for events pertaining to dictionary services.
			m_context.addServiceListener(this, "(&(objectClass=" + DictionaryService.class.getName() + ")" + "(Language=*))");

			// Query for any service references matching any language.
			ServiceReference[] refs = m_context.getServiceReferences(DictionaryService.class.getName(), "(Language=*)");

			// If we found any dictionary services, then just get a reference to the first one so we can use it.
			if (refs != null) {
				m_ref = refs[0];
				m_dictionary = (DictionaryService) m_context.getService(m_ref);
			}
		}

		try {
			System.out.println("Enter a blank line to exit.");
			String word = "";
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

			// Loop endlessly.
			while (true) {
				// Ask the user to enter a word.
				System.out.print("Enter word: ");
				word = in.readLine();

				if (word.length() == 0) {
					// If the user entered a blank line, then exit the loop.
					break;

				} else if (m_dictionary == null) {
					// If there is no dictionary, then say so.
					System.out.println("No dictionary available.");

				} else {
					// Otherwise print whether the word is correct or not.
					if (m_dictionary.checkWord(word)) {
						System.out.println("Correct.");
					} else {
						System.out.println("Incorrect.");
					}
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Does nothing since the framework will automatically unget any used services.
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		// Activator.context = null;

		// NOTE: The service is automatically released.
	}

	/**
	 * Implements ServiceListener.serviceChanged(). Checks to see if the service we are using is leaving or tries to get a service if we need one.
	 * 
	 * @param event
	 *            the fired service event.
	 **/
	public synchronized void serviceChanged(ServiceEvent event) {
		String[] objectClass = (String[]) event.getServiceReference().getProperty("objectClass");
		if (objectClass != null && objectClass.length > 0 && DictionaryService.class.getName().equals(objectClass[0])) {

			if (event.getType() == ServiceEvent.REGISTERED) {
				// If a dictionary service was registered, see if we need one. If so, get a reference to it.
				if (m_ref == null) {
					// Get a reference to the service object.
					m_ref = event.getServiceReference();
					m_dictionary = (DictionaryService) m_context.getService(m_ref);
				}

			} else if (event.getType() == ServiceEvent.UNREGISTERING) {
				// If a dictionary service was unregistered, see if it was the one we were using. If so, unget the service and try to query to get
				// another one.
				if (event.getServiceReference() == m_ref) {
					// Unget service object and null references.
					m_context.ungetService(m_ref);
					m_ref = null;
					m_dictionary = null;

					// Query to see if we can get another service.
					ServiceReference[] refs = null;
					try {
						refs = m_context.getServiceReferences(DictionaryService.class.getName(), "(Language=*)");
					} catch (InvalidSyntaxException ex) {
						// This will never happen.
					}
					if (refs != null) {
						// Get a reference to the first service object.
						m_ref = refs[0];
						m_dictionary = (DictionaryService) m_context.getService(m_ref);
					}
				}
			}
		}
	}

}
