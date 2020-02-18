default: build

build:
	javac --module-path $$PATH_TO_FX --add-modules javafx.controls *.java

run: build
	java --module-path $$PATH_TO_FX --add-modules javafx.controls OXOView

clean:
	rm *.class
