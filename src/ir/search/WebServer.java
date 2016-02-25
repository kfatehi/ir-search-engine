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
			ArrayList results = engine.query(query);
			System.out.println(results);
			return results;
		});
    }
}
