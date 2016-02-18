package ir.search;

import java.util.HashMap;

import java.io.*;
 
public final class Dumper {

	public static void main(String[] args) {
		try {
			File file = new File("index.bin");
			FileInputStream stream = new FileInputStream(file);
			ObjectInputStream ois = new ObjectInputStream(stream);
			HashMap<String, HashMap> map = new HashMap<>();
		   	map = (HashMap<String, HashMap>) ois.readObject();
			ois.close();

			int counter = 0;

			for ( String term : map.keySet() ) {
				System.out.println(term+":"+map.get(term));
				counter ++;

				if ( counter == 100) {
					System.out.println("Stopping after 100 terms.");
					System.out.println("total count: "+map.size());
					System.exit(0);
				}
			}
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
	}
}
