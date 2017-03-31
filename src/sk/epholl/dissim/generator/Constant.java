package sk.epholl.dissim.generator;

/**
 * Created by Tomáš on 17.03.2017.
 */
public class Constant<T extends Number> extends RandomGenerator<T> {

    private final T value;

    public Constant(T value) {
        this.value = value;
    }

    @Override
    public T nextValue() {
        return value;
    }
}
