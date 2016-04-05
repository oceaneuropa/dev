package dictionary;

import java.util.SortedMap;
import java.util.TreeMap;

import dictionary.spi.Dictionary;

public class GeneralDictionary implements Dictionary {

	private SortedMap<String, String> map;

	/** Creates a new instance of GeneralDictionary */
	public GeneralDictionary() {
		map = new TreeMap<String, String>();
		map.put("book", "a set of written or printed pages, usually bound with a protective cover");
		map.put("editor", "a person who edits");
	}

	@Override
	public String getDefinition(String word) {
		return map.get(word);
	}

}
