import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

import parcs.*;


public class PolynomialMultiplication {

    public static void main(String[] args) {
        task curtask = new task();
        curtask.addJarFile("FFT.jar");
        AMInfo info = new AMInfo(curtask, null);

        ArrayList<Complex> a = readPolynomial("a1000.txt");
        ArrayList<Complex> b = readPolynomial("b1000.txt");

        int padSize = 2 * nextPowerOf2(Math.max(a.size(), b.size()));

        System.out.println("Start calculation");
        long startTime = System.nanoTime();

        point p1 = info.createPoint();
        channel c1 = p1.createChannel();
        p1.execute("FFT");
        c1.write(pad(a, padSize));
        c1.write((byte) 0);

        point p2 = info.createPoint();
        channel c2 = p2.createChannel();
        p2.execute("FFT");
        c2.write(pad(b, padSize));
        c2.write((byte) 0);

        ArrayList<Complex> af = (ArrayList<Complex>) c1.readObject();
        ArrayList<Complex> bf = (ArrayList<Complex>) c2.readObject();
        ArrayList<Complex> cf = polyMul(af, bf);

        point p3 = info.createPoint();
        channel c3 = p3.createChannel();
        p3.execute("FFT");
        c3.write(cf);
        c3.write((byte) 1);

        ArrayList<Complex> c = (ArrayList<Complex>) c3.readObject();
        int cSize = c.size();

        int nDiff = c.size() - (a.size() + b.size() - 1);

        for (int i = 0; i < nDiff; i++) {
            c.remove(c.size() - 1);
        }

        for (int i = 0; i < c.size(); i++) {
            c.set(i, Complex.div(c.get(i), cSize));
        }

        double estimatedTime = (double) (System.nanoTime() - startTime) / 1000000000;
        System.out.println("Time total (excluding IO): " + estimatedTime);
        writePolynomial("c1000.txt", c);

        curtask.end();
    }

    public static ArrayList<Complex> readPolynomial(String filename) {
        ArrayList<Complex> poly = new ArrayList<Complex>();
        try {
            Scanner scan = new Scanner(new File(filename));
            while (scan.hasNextDouble()) {
                poly.add(new Complex(scan.nextDouble()));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return poly;
    }

    public static void writePolynomial(String filename, ArrayList<Complex> poly) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
            for (Complex c : poly) {
                writer.write(String.valueOf(c.r) + ' ');
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Complex> polyMul(ArrayList<Complex> a, ArrayList<Complex> b) {
        ArrayList<Complex> c = new ArrayList<Complex>();
        for (int i = 0; i < a.size(); i++) {
            c.add(Complex.mul(a.get(i), b.get(i)));
        }
        return c;
    }


    public static int nextPowerOf2(int n) {
        int count = 0;

        if (n > 0 && (n & (n - 1)) == 0)
            return n;

        while (n != 0) {
            n >>= 1;
            count += 1;
        }

        return 1 << count;
    }


    public static ArrayList<Complex> pad(ArrayList<Complex> a, int padSize) {
        ArrayList<Complex> aNew = new ArrayList<Complex>(a);
        while (aNew.size() < padSize) {
            aNew.add(new Complex());
        }
        return aNew;
    }

}