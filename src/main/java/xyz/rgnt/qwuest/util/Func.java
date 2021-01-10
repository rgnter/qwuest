package xyz.rgnt.qwuest.util;

/**
 * Function
 * @param <X> Parameter type
 * @param <R> Return type
 * @param <E> Exception
 */
public interface Func<X, R, E extends Throwable> {
    R apply(X var0) throws E;
}
