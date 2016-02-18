/**
 * CS 121: Milestone 1 -- Indexer
 *
 * Keyvan Fatehi 63393716
 * Joanna Kim 32393047
 * Caroline Fan 22863530 
 * Ali Aijaz 82181685
 **/
package ir.search;
import ir.analysis.Utilities;
import java.util.HashMap;
import java.util.ArrayList;
import java.io.*;
 
/**
 * Class that indexes a corpus of documents using TF-IDF for scoring.
 */
public final class Indexer {

	/**
	 * Runs the indexer.
	 *
	 * Starts the indexer which maps the terms to document ID with respect to frequency,
	 * and then calculates TF-IDF for each term-document in the map. Finally, the index
	 * is written to index.bin for reuse by a search engine.
	 */
	public static void main(String[] args) {
		System.out.println("Indexer starting...");
		// Load document index limit System property.
		int limit = Integer.valueOf(System.getProperty("limit"));
		HashMap<String,HashMap> map = new HashMap<>();
		try {
			// Load the corpus
			Corpus corpus = new Corpus("_corpus");
			while (corpus.next()) {
				// Determine if we should only index a subset of documents.
				if (limit != 0 && corpus.position > limit) {
					break;
				}
				String percent = String.format("%.2f", (corpus.position/(float) corpus.size())*100);
				System.out.print("\rProcessing document: "+corpus.position+" of "+corpus.size()+" ("+percent+"%) index size: "+map.size());
				Integer docId = corpus.position;
				String docText = corpus.current().getText();
				ArrayList<String> words = Utilities.tokenizeString(docText);
				for (String term : words) {
					HashMap<Integer,Integer> termFreq = map.getOrDefault(term, new HashMap<>());
					int currentValue = termFreq.getOrDefault(docId, 0);
					termFreq.put(docId, currentValue+1);
					map.put(term, termFreq);
				}
			}

			System.out.println("\nCreated index. Computing scores now...");

			/**
			 * Iterate through the map to compute TF-IDF scores.
			 * Places the TF-IDF scores along with their respective terms and documents
			 **/
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

			/**
			 * Save index to disk if specified via System property.
			 * */
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
