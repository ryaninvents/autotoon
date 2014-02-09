all: test

test: src/io/muller/test
	scalac -classpath lib/opencv-248.jar -d bin src/io/muller/test/*.scala

clean:
	rm -rf bin/*
