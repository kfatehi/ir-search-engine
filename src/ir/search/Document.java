package ir.search;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.Charset;
import java.io.IOException;

public final class Document {
	String url;
	String fileName;

	public Document(String fileName, String url) {
		this.url = url;
		this.fileName = fileName;
	}

	public String getText() throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(this.fileName));
		return new String(encoded, Charset.defaultCharset());
	}

	public String toString() {
		return this.url;
	}
}

