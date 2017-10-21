package ass2.spec;

public class MathUtil {
    public static double[] crossProduct(double[] a,double[] b){
        double[] product = {a[1]*b[2]-a[2]*b[1],
                            a[2]*b[0]-a[0]*b[2],
                            a[0]*b[1]-a[1]*b[0]};

        return product;
    }

    public static double [] normalise(double [] a){
        double  mag = getMagnitude(a);
        double norm[] = {a[0]/mag,
                         a[1]/mag,
                         a[2]/mag};
        return norm;
    }

    public static double[] normal(double[] a, double[] b, double[] c){
        double [] u = { b[0] - a[0],
                        b[1] - a[1],
                        b[2] - a[2]};
        double [] v = { c[0] - a[0],
                        c[1] - a[1],
                        c[2] - a[2]};

        double [] normal = crossProduct(u,v);
        return normalise(normal);
    }

    public static double getMagnitude(double [] a){
        double mag = a[0]*a[0] + a[1]*a[1] + a[2]*a[2];
        mag = Math.sqrt(mag);
        return mag;
    }

    public static double[] multiply(double[][] a, double[] b) {

        double[] u = new double[4];

        for (int i = 0; i < 4; i++) {
            u[i] = 0;
            for (int j = 0; j < 4; j++) {
                u[i] += a[i][j] * b[j];
            }
        }

        return u;
    }

    public static double normalizeAngle(double angle)
    {
        double newAngle = angle;
        while (newAngle <= -180) newAngle += 360;
        while (newAngle > 180) newAngle -= 360;
        return newAngle;
    }
}
