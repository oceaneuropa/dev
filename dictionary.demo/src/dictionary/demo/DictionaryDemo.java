package dictionary.demo;

import dictionary.DictionaryService;

public class DictionaryDemo {

	public static void main(String[] args) {
		DictionaryService dictionary = DictionaryService.getInstance();
		System.out.println(DictionaryDemo.lookup(dictionary, "book"));
		System.out.println(DictionaryDemo.lookup(dictionary, "editor"));
		System.out.println(DictionaryDemo.lookup(dictionary, "xml"));
		System.out.println(DictionaryDemo.lookup(dictionary, "REST"));
	}

	public static String lookup(DictionaryService dictionary, String word) {
		String outputString = word + ": ";
		String definition = dictionary.getDefinition(word);
		if (definition == null) {
			return outputString + "Cannot find definition for this word.";
		} else {
			return outputString + definition;
		}
	}

}
