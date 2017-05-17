package sk.epholl.dissim.util.deciders;

/**
 * Created by Tomáš on 16.05.2017.
 */
public interface Comparator {

    Comparator lessThan = new Comparator() {
        @Override
        public boolean compare(double arg1, double arg2) {
            return arg1 < arg2;
        }

        @Override
        public String getName() {
            return "is less than";
        }
    };

    Comparator lessThanOrEquals = new Comparator() {
        @Override
        public boolean compare(double arg1, double arg2) {
            return arg1 < arg2 || Math.abs(arg1 - arg2) < 0.000001d;
        }

        @Override
        public String getName() {
            return "is less or equals";
        }
    };

    Comparator equals = new Comparator() {
        @Override
        public boolean compare(double arg1, double arg2) {
            return Math.abs(arg1 - arg2) < 0.000001d;
        }

        @Override
        public String getName() {
            return "equals";
        }
    };

    public boolean compare(double arg1, double arg2);
    public String getName();
}
