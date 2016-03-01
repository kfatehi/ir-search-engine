# ir-search-engine

Run a short indexing demo:

`make demo`

Build index of corpus:

`make index`

Run a search via command line:

`java -classpath .:lib/*:_build ir.search.SearchEngine "your query"`

Start the search engine as a web server:

`make server`

Run a query against the search server via the command line:

`curl "localhost:4567/search?query=your+query"`
