package ir.analysis;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 * A collection of utility methods for text processing.
 */
public class Utilities {
	static HashSet stopWordSet;

	static {
		initStopWords("stopwords.txt");
	}

	static void initStopWords(String fileName) {
		stopWordSet = new HashSet<String>();
		try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
			stream.forEach((String str)-> {
				stopWordSet.add(str);
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Reads the input text file and splits it into alphanumeric tokens.
	 * Returns an ArrayList of these tokens, ordered according to their
	 * occurrence in the original text file.
	 * 
	 * Non-alphanumeric characters delineate tokens, and are discarded.
	 *
	 * Words are also normalized to lower case. 
	 * 
	 * Example:
	 * 
	 * Given this input string
	 * "An input string, this is! (or is it?)"
	 * 
	 * The output list of strings should be
	 * ["an", "input", "string", "this", "is", "or", "is", "it"]
	 * 
	 * @param input The file to read in and tokenize.
	 * @return The list of tokens (words) from the input file, ordered by occurrence.
	 */
	public static ArrayList<String> tokenizeString(String input) {
    	ArrayList<String> out = new ArrayList<>();
		final Scanner sc;
		sc = new Scanner(input).useDelimiter(" ");
		while (sc.hasNext()) {
			String token = sc.next().toLowerCase();

			if (! token.matches("^\\w+$")) {
				continue;
			}

			if ( stopWordSet.contains(token)) {
				continue;
			}

			out.add(token);
		}
		return out;
	}
}
