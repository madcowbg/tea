package expressions;

public class Gaussian implements Term {
    public final double mu;
    public final double sigma;

    private Gaussian(double mu, double sigma) {
        this.mu = mu;
        this.sigma = sigma;
    }

    public static Gaussian W(double mu, double sigma) {
        return new Gaussian(mu, sigma);
    }

    @Override
    public String print() {
        return "N(" + mu + ", " + sigma + ")";
    }
}
