package sk.epholl.dissim.util.deciders;

/**
 * Created by Tomáš on 16.05.2017.
 */
public abstract class Condition<T> {

    protected ValueProvider arg1Provider;
    protected ValueProvider arg2Provider;
    protected Comparator comparator;
    protected T returnValue;

    public Condition(ValueProvider arg1Provider, Comparator comparator, ValueProvider arg2Provider, T returnValue) {
        this.arg1Provider = arg1Provider;
        this.arg2Provider = arg2Provider;
        this.comparator = comparator;
        this.returnValue = returnValue;
    }

    public boolean evaluate() {
        final double arg1 = arg1Provider.getValue();
        final double arg2 = arg2Provider.getValue();
        return comparator.compare(arg1, arg2);
    }

    public T getReturnValue() {
        return returnValue;
    }

    public void setValue1Provider(ValueProvider provider) {
        this.arg1Provider = provider;
    }

    public void setValue2Provider(ValueProvider provider) {
        this.arg2Provider = provider;
    }

    public void setComparator(Comparator comparator) {
        this.comparator = comparator;
    }

    public void setReturnValue(T returnValue) {
        this.returnValue = returnValue;
    }

    @Override
    public String toString() {
        return arg1Provider +
                " " + comparator.toString() +
                " " + arg2Provider +
                ": " + getReturnValue();
    }
}
