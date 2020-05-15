default: run

clean:
	rm *.class ConditionTree/*.class Database/*.class Interpreter/*.class Parser/*.class

build:
	javac -classpath :. DBClient.java DBServer.java

run:
	java -classpath :. DBServer

