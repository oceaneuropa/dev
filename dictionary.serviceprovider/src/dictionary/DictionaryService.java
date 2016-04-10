package dictionary;

import java.util.Iterator;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;

import dictionary.spi.Dictionary;

public class DictionaryService {

	private static DictionaryService service;
	private ServiceLoader<Dictionary> loader;

	private DictionaryService() {
		loader = ServiceLoader.load(Dictionary.class);
	}

	public static synchronized DictionaryService getInstance() {
		if (service == null) {
			service = new DictionaryService();
		}
		return service;
	}

	public String getDefinition(String word) {
		String definition = null;
		try {
			Iterator<Dictionary> dictionaries = loader.iterator();
			while (definition == null && dictionaries.hasNext()) {
				Dictionary d = dictionaries.next();
				definition = d.getDefinition(word);
			}
		} catch (ServiceConfigurationError serviceError) {
			definition = null;
			serviceError.printStackTrace();
		}
		return definition;
	}

}
