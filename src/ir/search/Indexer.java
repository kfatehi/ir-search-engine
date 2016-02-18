package ir.search;

import ir.analysis.Utilities;
import ir.analysis.db.Database;

import java.io.File;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collections;

import java.sql.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.json.simple.parser.ParseException;
 
public final class Indexer {

	public static void main(String[] args) {
		// Map of Document ID to ArrayList of Postings List
		System.out.println("Indexer starting...");
		HashMap<String,HashMap> map = new HashMap<>();
		try {
			Corpus corpus = new Corpus("_corpus");
			while (corpus.next()) { // && corpus.position < 10) {
				String percent = String.format("%.2f", (corpus.position/(float) corpus.size())*100);
				System.out.print("\rProcessing document: "+corpus.position+" of "+corpus.size()+" ("+percent+"%)");
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

			System.out.println(map);

		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
	}
}
