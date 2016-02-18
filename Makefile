BUILD=_build
CLASSPATH=.:lib/*:$(BUILD)

SRC= \
		 src/ir/search/*.java \

server: compile
	java -classpath $(CLASSPATH) ir.search.WebServer

# runs indexer but does not save the file and only does first 100 docs
demo: compile
	java -classpath $(CLASSPATH) -Dsave=false -Dlimit=100 ir.search.Indexer

dump: compile
	java -classpath $(CLASSPATH) ir.search.Dumper

index: compile
	java -classpath $(CLASSPATH) -Dsave=true -Dlimit=0 ir.search.Indexer

compile: clean
	javac -g -d $(BUILD) -classpath $(CLASSPATH) $(SRC)

clean:
	@rm -rf $(BUILD)
	@mkdir -p $(BUILD)

deps:
	mvn dependency:copy-dependencies -DoutputDirectory=lib
