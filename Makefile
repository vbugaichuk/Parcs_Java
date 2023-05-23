all: run

clean:
	rm -f out/PolynomialMultiplication.jar out/FFT.jar

out/PolynomialMultiplication.jar: out/parcs.jar src/PolynomialMultiplication.java src/Complex.java
	@javac -cp out/parcs.jar src/PolynomialMultiplication.java src/Complex.java
	@jar cf out/PolynomialMultiplication.jar -C src PolynomialMultiplication.class -C src Complex.class
	@rm -f src/PolynomialMultiplication.class src/Complex.class

out/FFT.jar: out/parcs.jar src/FFT.java src/Complex.java
	@javac -cp out/parcs.jar src/FFT.java src/Complex.java
	@jar cf out/FFT.jar -C src FFT.class -C src Complex.class
	@rm -f src/FFT.class src/Complex.class

build: out/PolynomialMultiplication.jar out/FFT.jar

run: out/PolynomialMultiplication.jar out/FFT.jar
	@cd out && java -cp 'parcs.jar:PolynomialMultiplication.jar' PolynomialMultiplication