import java.util.Collections;
import java.util.ArrayList;


import parcs.*;

public class FFT implements AM {
    
    public void run(AMInfo info) {
        ArrayList<Complex> a = (ArrayList<Complex>) info.parent.readObject();
        byte inv = info.parent.readByte();
        
        int n = a.size();
        if (n == 1) {
            info.parent.write(a);
            return;
        }
        
        Complex w_n;
        if (inv == 0)
            w_n = Complex.eImPow(-2*Math.PI/n);
        else
            w_n = Complex.eImPow(2*Math.PI/n);
        
        Complex w = new Complex(1);
        
        ArrayList<Complex> a_0 = new ArrayList<Complex>();
        for (int i = 0; i < n; i += 2) {
            a_0.add(a.get(i));
        }
        
        ArrayList<Complex> a_1 = new ArrayList<Complex>();
        for (int i = 1; i < n; i += 2) {
            a_1.add(a.get(i));
        }
        
        point p1 = info.createPoint();
        channel c1 = p1.createChannel();
        p1.execute("FFT");
        c1.write(a_0);
        c1.write(inv);
        
        point p2 = info.createPoint();
        channel c2 = p2.createChannel();
        p2.execute("FFT");
        c2.write(a_1);
        c2.write(inv);
        
        ArrayList<Complex> y_0 = (ArrayList<Complex>) c1.readObject();
        ArrayList<Complex> y_1 = (ArrayList<Complex>) c2.readObject();
        
        ArrayList<Complex> y = new ArrayList<Complex>(
            Collections.nCopies(n, new Complex()));
            
        for (int k = 0; k < n/2; k++) {
            Complex t = Complex.mul(w, y_1.get(k));
            y.set(k, Complex.add(y_0.get(k), t));
            y.set(k + n/2, Complex.sub(y_0.get(k), t));
            w = Complex.mul(w, w_n);
        }
        
        info.parent.write(y);
    }
}