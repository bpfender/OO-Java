default: build

test:
	javac -classpath :. Test.java

run_test: clean test
	java -classpath :. Test

build:
	javac -classpath :. DBClient.java DBServer.java

run: clean build
	java -classpath :. DBServer

clean:
	rm *.class ConditionTree/*.class Database/*.class Expression/*.class Parser/*.class
