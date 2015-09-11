//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.lorentzos.flingswipe;

class LinearRegression {
    private final int N;
    private final double alpha;
    private final double beta;
    private final double R2;
    private final double svar;
    private final double svar0;
    private final double svar1;

    public LinearRegression(float[] x, float[] y) {
        if(x.length != y.length) {
            throw new IllegalArgumentException("array lengths are not equal");
        } else {
            this.N = x.length;
            double sumx = 0.0D;
            double sumy = 0.0D;
            double sumx2 = 0.0D;

            int xbar;
            for(xbar = 0; xbar < this.N; ++xbar) {
                sumx += (double)x[xbar];
            }

            for(xbar = 0; xbar < this.N; ++xbar) {
                sumx2 += (double)(x[xbar] * x[xbar]);
            }

            for(xbar = 0; xbar < this.N; ++xbar) {
                sumy += (double)y[xbar];
            }

            double var26 = sumx / (double)this.N;
            double ybar = sumy / (double)this.N;
            double xxbar = 0.0D;
            double yybar = 0.0D;
            double xybar = 0.0D;

            for(int rss = 0; rss < this.N; ++rss) {
                xxbar += ((double)x[rss] - var26) * ((double)x[rss] - var26);
                yybar += ((double)y[rss] - ybar) * ((double)y[rss] - ybar);
                xybar += ((double)x[rss] - var26) * ((double)y[rss] - ybar);
            }

            this.beta = xybar / xxbar;
            this.alpha = ybar - this.beta * var26;
            double var27 = 0.0D;
            double ssr = 0.0D;

            int degreesOfFreedom;
            for(degreesOfFreedom = 0; degreesOfFreedom < this.N; ++degreesOfFreedom) {
                double fit = this.beta * (double)x[degreesOfFreedom] + this.alpha;
                var27 += (fit - (double)y[degreesOfFreedom]) * (fit - (double)y[degreesOfFreedom]);
                ssr += (fit - ybar) * (fit - ybar);
            }

            degreesOfFreedom = this.N - 2;
            this.R2 = ssr / yybar;
            this.svar = var27 / (double)degreesOfFreedom;
            this.svar1 = this.svar / xxbar;
            this.svar0 = this.svar / (double)this.N + var26 * var26 * this.svar1;
        }
    }

    public double intercept() {
        return this.alpha;
    }

    public double slope() {
        return this.beta;
    }

    public double R2() {
        return this.R2;
    }

    public double interceptStdErr() {
        return Math.sqrt(this.svar0);
    }

    public double slopeStdErr() {
        return Math.sqrt(this.svar1);
    }

    public double predict(double x) {
        return this.beta * x + this.alpha;
    }

    public String toString() {
        String s = "";
        s = s + String.format("%.2f N + %.2f", new Object[]{Double.valueOf(this.slope()), Double.valueOf(this.intercept())});
        return s + "  (R^2 = " + String.format("%.3f", new Object[]{Double.valueOf(this.R2())}) + ")";
    }
}
