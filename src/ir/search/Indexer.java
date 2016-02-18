package ir.search;

import ir.analysis.Utilities;
import ir.analysis.db.Database;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import java.sql.*;
import java.io.*;
 
public final class Indexer {

	public static void main(String[] args) {
		int limit = Integer.valueOf(System.getProperty("limit"));

		// Map of Document ID to ArrayList of Postings List
		System.out.println("Indexer starting...");
		HashMap<String,HashMap> map = new HashMap<>();
		try {
			Corpus corpus = new Corpus("_corpus");
			while (corpus.next()) {
				if (limit != 0 && corpus.position > limit) {
					break;
				}
				String percent = String.format("%.2f", (corpus.position/(float) corpus.size())*100);
				System.out.print("\rProcessing document: "+corpus.position+" of "+corpus.size()+" ("+percent+"%) index size: "+map.size());
				Integer docId = corpus.position;
				String docText = corpus.current().getText();
				List<String> words = Utilities.tokenizeString(docText);
				for (String term : words) {
					HashMap<Integer,Integer> termFreq = map.getOrDefault(term, new HashMap<>());
					int currentValue = termFreq.getOrDefault(docId, 0);
					termFreq.put(docId, currentValue+1);
					map.put(term, termFreq);
				}
			}

			System.out.println("\nCreated index. Computing scores now...");

			// Compute TF-IDF Scores
			for (String term : map.keySet()) {
				HashMap<Integer,Double> tfidfMap = new HashMap<>();
				HashMap<Integer,Integer> termFreq = map.get(term);

				for (Integer docId : termFreq.keySet()) {
					int freq = termFreq.get(docId);
					double wtf = 1 + Math.log(freq);
					double tfidf = wtf * Math.log( (float) corpus.size() / termFreq.keySet().size() );

					tfidfMap.put(docId, tfidf);
				}

				map.put(term, tfidfMap);
			}

			boolean save = Boolean.valueOf(System.getProperty("save"));

			if (save) {
				System.out.println("Saving index to index.bin");
				File file = new File("index.bin");
				FileOutputStream stream = new FileOutputStream(file);
				ObjectOutputStream oos = new ObjectOutputStream(stream);
				oos.writeObject(map);
				oos.flush();
				oos.close();
			} else {
				// Doing a demo, dump the map
				for ( String term : map.keySet() ) {
					System.out.println(term+":"+map.get(term));
				}
			}
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
	}
}
