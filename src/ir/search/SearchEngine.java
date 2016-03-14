package ir.search;

import java.util.HashMap;
import java.util.ArrayList;
import java.io.*;
import java.util.Comparator;

public class SearchEngine {
	private HashMap<String,HashMap> index;
	private Corpus corpus;

	public SearchEngine() {
		try {
			this.corpus = new Corpus("_corpus");
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * A comparator used to sort a list of search results.
	 *
	 * <h2>Sort Criteria</h2><ol>
	 * <li>decreasing score</li>
	 */
	public static Comparator<SearchResult> resultComparator = new Comparator<SearchResult>() {
		public int compare(SearchResult a, SearchResult b) {
			return Double.compare(b.getScore(), a.getScore());
		}
	};

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
		System.out.println("perform query for: "+query);
		String[] terms = query.split("\\s+");
		HashMap <Integer, SearchResult> resultScores = new HashMap<>();

		for (String term : terms) {
			HashMap<Integer, Double> termDocs = this.index.get(term.toLowerCase());
			if (termDocs != null) {
				for (Integer docId : termDocs.keySet()) {
					Double score = termDocs.get(docId);
					Document doc = this.corpus.getDocument(docId);
					SearchResult result = resultScores.getOrDefault(docId, new SearchResult(0.0, doc));
					result.increaseScore(score);
					resultScores.put(docId, result);
				}
			}
		}

		System.out.println("Got results -- now about to convert from hashmap to arraylist...");
		ArrayList<SearchResult> results = new ArrayList<SearchResult>(resultScores.values());
		System.out.println("Got results -- now about to sort them...");
		results.sort(resultComparator);
		System.out.println("done");
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
