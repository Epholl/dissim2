package sk.epholl.dissim.util.subscribers;

/**
 * Created by Tomáš on 05.05.2017.
 */
public class ValueType<T> {

    public final Class<T> clazz;

    public ValueType(final Class<T> clazz) {
        this.clazz = clazz;
    }
}
