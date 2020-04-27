default: build

test:
	javac -classpath :. Test.java

run_test: clean test
	java -classpath :. Test

build:
	javac -classpath :. StagServer.java StagClient.java

run: build
	java -classpath :. StagServer data/entities_extended.dot data/actions_extended.json

clean:
	rm *.class ConditionTree/*.class Database/*.class Expression/*.class Parser/*.class
