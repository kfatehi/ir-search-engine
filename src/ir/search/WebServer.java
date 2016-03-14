package ir.search;

import java.util.ArrayList;
import static spark.Spark.*;

public class WebServer {
    public static void main(String[] args) {
		staticFileLocation("/public");

		SearchEngine engine = new SearchEngine();
		engine.loadIndex("index.bin");

		get("/search", (req, res) -> {
			String query = req.queryParams("query");
			ArrayList<SearchResult> results = engine.query(query);

			String html = "";

			html+="<link rel=\"stylesheet\" type=\"text/css\" href=\"style.css\">";
			html+="<form action=\"/search\" method=\"get\">";
			html+="  <input type=\"text\" name=\"query\" />";
			html+="  <input type=\"submit\" />";
			html+="</form>";

			html+="<p>Total search results: "+results.size()+", showing only the top results.</p>";

			html+="<ul>";

			int count = 0;

			for (SearchResult result : results) {
				html += "<li><a href=\""+result+"\">"+result+"</a></li>";
				count++;
				if (count == 20) break;
			}

			html += "</ul>";

			return html;
		});
    }
}
