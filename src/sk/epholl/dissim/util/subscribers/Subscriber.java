package sk.epholl.dissim.util.subscribers;

/**
 * Created by Tomáš on 05.05.2017.
 */
public interface Subscriber<T> {
    void onValueEmitted( T value);
}
