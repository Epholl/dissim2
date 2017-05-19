package sk.epholl.dissim.util.deciders;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tomáš on 16.05.2017.
 */
public abstract class Comparator {

    public static final Comparator[] values() {
        return new Comparator[] {lessThan, lessThanOrEquals, moreThan, moreThanOrEquals, equals, not};
    }

    public static final Comparator lessThan = new Comparator() {
        @Override
        public boolean compare(double arg1, double arg2) {
            return arg1 < arg2;
        }

        @Override
        public String toString() {
            return "<";
        }
    };

    public static final Comparator moreThanOrEquals = new Comparator() {
        @Override
        public boolean compare(double arg1, double arg2) {
            return arg1 > arg2 || Math.abs(arg1 - arg2) < 0.000001d;
        }

        @Override
        public String toString() {
            return ">=";
        }
    };

    public static final Comparator moreThan = new Comparator() {
        @Override
        public boolean compare(double arg1, double arg2) {
            return arg1 > arg2;
        }

        @Override
        public String toString() {
            return ">";
        }
    };

    public static final Comparator lessThanOrEquals = new Comparator() {
        @Override
        public boolean compare(double arg1, double arg2) {
            return arg1 < arg2 || Math.abs(arg1 - arg2) < 0.000001d;
        }

        @Override
        public String toString() {
            return "<=";
        }
    };

    public static final Comparator equals = new Comparator() {
        @Override
        public boolean compare(double arg1, double arg2) {
            return Math.abs(arg1 - arg2) < 0.000001d;
        }

        @Override
        public String toString() {
            return "==";
        }
    };

    public static final Comparator not = new Comparator() {
        @Override
        public boolean compare(double arg1, double arg2) {
            return !(Math.abs(arg1 - arg2) < 0.000001d);
        }

        @Override
        public String toString() {
            return "!=";
        }
    };

    public abstract boolean compare(double arg1, double arg2);
}
