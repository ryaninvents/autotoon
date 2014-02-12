all: test

test:
	mkdir -p bin
	scalac -classpath lib/opencv-248.jar -d bin src/org/autotoon/*.scala

clean:
	rm -rf bin/*
