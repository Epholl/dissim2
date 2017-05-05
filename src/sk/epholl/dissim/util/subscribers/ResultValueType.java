package sk.epholl.dissim.util.subscribers;

/**
 * Created by Tomáš on 05.05.2017.
 */
public class ResultValueType<T> {

    public final Class<T> clazz;

    public ResultValueType(final Class<T> clazz) {
        this.clazz = clazz;
    }
}
