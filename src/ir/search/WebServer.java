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

			html+="<form action=\"/search\" method=\"get\">";
			html+="  <input type=\"text\" name=\"query\" />";
			html+="  <input type=\"submit\" />";
			html+="</form>";

			html+="<p>Total search results: "+results.size()+"</p>";

			html+="<ul>";

			for (SearchResult result : results) {
				html += "<li>"+result.toString()+"</li>";
			}

			html += "</ul>";

			return html;
		});
    }
}
