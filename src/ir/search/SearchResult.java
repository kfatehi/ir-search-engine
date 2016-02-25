package ir.search;

public final class SearchResult {
	private Double score;
	private Document document;

	public SearchResult(Double score, Document doc) {
		this.score = score;
		this.document = doc;
	}	

	public Double getScore() {
		return this.score;
	}

	public String toString() {
		return this.document.toString();
	}
}

