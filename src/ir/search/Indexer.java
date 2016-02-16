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

	public static int totalDocs = 32739;

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
					+"LIMIT "+totalDocs);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Integer docId = rs.getInt(1);
				String docText = rs.getString(2);
				List<String> words = Utilities.tokenizeString(docText);
				for (String term : words) {
					HashMap<Integer,Integer> termFreq = map.getOrDefault(term, new HashMap<>());
					int currentValue = termFreq.getOrDefault(docId, 0);
					termFreq.put(docId, currentValue+1);
					map.put(term, termFreq);
				}
			}


			// Compute TF-IDF Scores
			for (String term : map.keySet()) {
				HashMap<Integer,Double> tfidfMap = new HashMap<>();
				HashMap<Integer,Integer> termFreq = map.get(term);

				for (Integer docId : termFreq.keySet()) {
					int freq = termFreq.get(docId);
					double wtf = 1 + Math.log(freq);
					double tfidf = wtf * Math.log( totalDocs / termFreq.keySet().size() );

					tfidfMap.put(docId, tfidf);
				}

				map.put(term, tfidfMap);
			}

			System.out.println(map);

			st.close();
		} catch(SQLException e) {
			System.out.println(e);
		}
	}
}
