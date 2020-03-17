default: build

build:
	javac -classpath ./libs/dot-parser.jar:./libs/json-parser.jar:. StagServer.java StagClient.java

run: build
	java -classpath ./libs/dot-parser.jar:./libs/json-parser.jar:. StagServer data/entities_extended.dot data/actions_extended.json

clean:
	rm *.class
