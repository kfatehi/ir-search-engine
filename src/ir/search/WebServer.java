package ir.search;

import static spark.Spark.*;

public class WebServer {
    public static void main(String[] args) {
        get("/hello", (req, res) -> "Hello World");
    }
}
