package ir.search;

import static spark.Spark.*;

public class WebServer {
    public static void main(String[] args) {
		staticFileLocation("/public");

		get("/search", (req, res) -> {
			String query = req.queryParams("query");

			return "results of "+query+" as templated html";
		});
    }
}
