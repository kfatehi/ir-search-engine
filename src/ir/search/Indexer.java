package ir.search;

import ir.analysis.Utilities;
import ir.analysis.db.Database;

import java.io.File;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collections;

import java.sql.*;
 
public final class Indexer {
	/**
	 * Loops through every page's text content to determine word frequencies.
	 * Initializes a map that will consist of various word frequencies.
	 * Creates a connection to the database and stores that connection.
	 * A query is performed to pull in all the text from the pages within a table schema in the
	 * database. 
	 * After this query is executed, each line in the text is pulled out and is then tokenized.
	 * Each word's frequency is recorded and is outputed to a map. It is then sorted and printed out.
	 * @param args The first element should contain the path to a text file.
	 */
	public static void main(String[] args) {
		// Term frequencies
		HashMap<String, ArrayList> tf = new HashMap<>();

		// Doc frequencies
		HashMap<String, Integer> df = new HashMap<>();

		// Map of Document ID to ArrayList of Postings List
		HashMap<String,HashMap> map = new HashMap<>();
		Database.configure();
		try {
			Connection con = Database.conn;
			PreparedStatement st = con.prepareStatement(
					"SELECT id,text FROM PAGES "
					+"WHERE TEXT IS NOT NULL "
					+"LIMIT 10");
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Integer docId = rs.getInt(1);
				String docText = rs.getString(2);
				List<String> words = Utilities.tokenizeString(docText);

				for (String term : words) {
					HashMap<Integer,Integer> termFreq = map.get(term);

					if (termFreq == null) {
						termFreq = new HashMap<>();
					}


					// for a postings list, we added doc id to the termfreq which was an array list...
					// termFreq.add(docId)
					
					// now we want to have docId:occurences

					int currentValue = termFreq.getOrDefault(docId, 0);
					termFreq.put(docId, currentValue+1);

					map.put(term, termFreq);

				}
			}
			System.out.println(map);

			st.close();
		} catch(SQLException e) {
			System.out.println(e);
		}
	}
}
