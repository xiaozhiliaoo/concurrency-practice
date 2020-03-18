package queueutils;

/**
 * Created by lili on 2017/7/23
 */
public interface KeyFactory<T,K> {
    public K generateKey(T source);

    public static class HashKeyFactory implements KeyFactory{

        @Override
        public Integer generateKey(Object source) {
            return source.hashCode();
        }
    }
}
