BUILD=_build
PGSQL=lib/postgresql-9.4.1207.jre6.jar
HIKARI=lib/HikariCP-2.4.3.jar
JDBC=jdbc.drivers=org.postgresql.Driver
CLASSPATH=.:$(PGSQL):$(HIKARI):$(BUILD)

SRC= \
		 src/ir/assignments/three/db/*.java \
		 src/ir/assignments/four/*.java \

build-tf-index: compile
	java -classpath $(CLASSPATH) -D$(JDBC) \
		-Dpg_password=$(shell cat _private/prod_db_password.txt) \
		 ir.assignments.four.Indexer

compile: clean
	@javac -g -d $(BUILD) -classpath $(CLASSPATH) $(SRC)

clean:
	@rm -rf $(BUILD)
	@mkdir -p $(BUILD)
