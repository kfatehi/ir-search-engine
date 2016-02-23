package ir.search;

import java.util.HashMap;
import java.util.ArrayList;
import java.io.*;

public class SearchEngine {
	private HashMap<String,HashMap> index;

	public void loadIndex(String fileName) {
		try {
			System.out.print("Loading index...");
			File file = new File(fileName);
			FileInputStream stream = new FileInputStream(file);
			ObjectInputStream ois = new ObjectInputStream(stream);
			this.index = (HashMap<String,HashMap>) ois.readObject();
			ois.close();
			System.out.println(" OK.");
		}
		catch(Exception ex) {
			ex.printStackTrace();
			System.exit(1);
		}
	}

	public ArrayList query(String query) {
		ArrayList<SearchResult> results = new ArrayList<>();
		System.out.println("perform query for: "+query);
		// perform the query...
		return results;
	}

    public static void main(String[] args) {
		String query = args[0];
		SearchEngine engine = new SearchEngine();
		engine.loadIndex("index.bin");
		ArrayList results = engine.query(query);
		System.out.println(results);
    }
}
