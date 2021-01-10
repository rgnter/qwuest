package xyz.rgnt.qwuest.util;

/**
 * Function
 * @param <X> Parameter type
 * @param <Y> Parameter type
 * @param <R> Return type
 * @param <E> Exception
 */
public interface BiFunc<X, Y, R, E extends Throwable> {
    R apply(X var0, Y var1) throws E;
}
