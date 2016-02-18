package ir.search;

import java.io.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public final class Corpus {
	public int position;
	private Document currentDocument;
	private JSONObject jsonObject;
	private String dirName;

	public Corpus(String dirName) throws FileNotFoundException, IOException, ParseException {
		this.position = -1;
		this.dirName = dirName;
		this.currentDocument = null;
        String fileName = dirName+"/html_files.json";
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(new FileReader(fileName));
		this.jsonObject = (JSONObject) obj;
	}

	public boolean next() {
		this.position++;
		try {
			JSONObject doc = (JSONObject) this.jsonObject.get(String.valueOf(this.position));
			String file = (String) doc.get("file");
			String url = (String) doc.get("url");
			this.currentDocument = new Document(dirName+"/Html/"+file, url);
			return true;
		} catch(NullPointerException ex) {
			this.position--;
			return false;
		}
	}

	public int size() {
		return this.jsonObject.size();
	}

	public Document current() {
		return currentDocument;
	}
}
