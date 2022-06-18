package chapter2;

/**
 * @author lili
 * @date 2022/6/19 1:41
 */
public class Fraction {                             // Fragments
    protected final long numerator;
    protected final long denominator;

    public Fraction(long num, long den) {
        // normalize:
        boolean sameSign = (num >= 0) == (den >= 0);
        long n = (num >= 0) ? num : -num;
        long d = (den >= 0) ? den : -den;
        long g = gcd(n, d);
        numerator = (sameSign) ? n / g : -n / g;
        denominator = d / g;
    }

    static long gcd(long a, long b) {
        // ... compute greatest common divisor ...
        return 1;
    }

    public Fraction plus(Fraction f) {
        return new Fraction(numerator * f.denominator +
                f.numerator * denominator,
                denominator * f.denominator);
    }

    public boolean equals(Object other) { // override default
        if (!(other instanceof Fraction)) return false;
        Fraction f = (Fraction) (other);
        return numerator * f.denominator ==
                denominator * f.numerator;
    }

    public int hashCode() {              // override default
        return (int) (numerator ^ denominator);
    }
}
