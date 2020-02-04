default: build

build:
	javac GradeConverter.java

run: build
	java GradeConverter

test: build
	javac GradeConverterTester.java
	java -ea GradeConverterTester

clean:
	rm *.class
