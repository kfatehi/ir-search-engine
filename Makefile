BUILD=_build
JSON=lib/json-simple-1.1.1.jar
CLASSPATH=.:$(JSON):$(BUILD)

SRC= \
		 src/ir/db/*.java \
		 src/ir/search/*.java \

index: compile
	java -classpath $(CLASSPATH) ir.search.Indexer

compile: clean
	javac -g -d $(BUILD) -classpath $(CLASSPATH) $(SRC)

clean:
	@rm -rf $(BUILD)
	@mkdir -p $(BUILD)
