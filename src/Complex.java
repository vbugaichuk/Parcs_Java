import java.io.Serializable;


public class Complex implements Serializable {

    public final double r, i;

    public Complex() {
        this.r = 0;
        this.i = 0;
    }

    public Complex(double r) {
        this.r = r;
        this.i = 0;
    }

    public Complex(double r, double i) {
        this.r = r;
        this.i = i;
    }

    public static Complex add(Complex c1, Complex c2) {
        return new Complex(c1.r + c2.r, c1.i + c2.i);
    }

    public static Complex sub(Complex c1, Complex c2) {
        return new Complex(c1.r - c2.r, c1.i - c2.i);
    }

    public static Complex mul(Complex c1, Complex c2) {
        return new Complex(c1.r * c2.r - c1.i * c2.i, c1.r * c2.i + c1.i * c2.r);
    }

    public static Complex div(Complex c1, double x) {
        return new Complex(c1.r / x, c1.i / x);
    }

    public static Complex eImPow(double x) {
        return new Complex(Math.cos(x), Math.sin(x));
    }

    public String toString() {
        return r + " + " + i + "i";
    }
}