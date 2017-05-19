package sk.epholl.dissim.sem3.entity;

/**
 * Created by Tomáš on 16.05.2017.
 */
public class FreeCapacity {

    private int capacity;
    private int freeUnits;

    public FreeCapacity(final int capacity, final int freeUnits) {

        if (freeUnits > capacity || capacity < 1) {
            throw new AssertionError("incorrect input data entered: " + capacity + ", " + freeUnits);
        }

        this.capacity = capacity;
        this.freeUnits = freeUnits;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getFreeUnits() {
        return freeUnits;
    }

    public int getUsedUnits() {
        return capacity - freeUnits;
    }

    public double getLoadFactor() {
        return ((double) capacity - freeUnits) / capacity;
    }

    public void occupySingleSpot() {
        if (freeUnits == 0) {
            throw new AssertionError("Attempted to occupy a free spot with none available");
        }
        freeUnits--;
    }

    @Override
    public String toString() {
        return getUsedUnits() + " / " + capacity + ", load: " + String.format("%.2f", getLoadFactor());
    }
}
