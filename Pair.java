/**
 * A simple generic class that represents a pair of two values.
 *
 * @param <U> The type of the first value.
 * @param <V> The type of the second value.
 */
public class Pair<U, V> {
    public final U first;
    public final V second;

    /**
     * Constructs a new Pair with the provided values.
     *
     * @param first  The first value of the pair.
     * @param second The second value of the pair.
     */
    public Pair(U first, V second) {
        this.first = first;
        this.second = second;
    }

    /**
     * Creates a new Pair with the provided values.
     *
     * @param a The first value of the pair.
     * @param b The second value of the pair.
     * @param <U> The type of the first value.
     * @param <V> The type of the second value.
     * @return A new Pair with the specified values.
     */
    public static <U, V> Pair<U, V> of(U a, V b) {
        return new Pair<>(a, b);
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     *
     * @param obj The object to compare with this Pair.
     * @return True if the two Pairs are equal; false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Pair<?, ?> pair = (Pair<?, ?>) obj;
        return first.equals(pair.first) && second.equals(pair.second);
    }

    /**
     * Returns a hash code value for the Pair.
     *
     * @return The hash code of the Pair.
     */
    @Override
    public int hashCode() {
        return 31 * first.hashCode() + second.hashCode();
    }
}
