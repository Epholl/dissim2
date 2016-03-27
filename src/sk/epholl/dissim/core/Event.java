package sk.epholl.dissim.core;

/**
 * Created by Tomáš on 26.03.2016.
 */
public abstract class Event {

    private float occurTime;

    private Event(float occurTime) {
        this.occurTime = occurTime;
    }

    public abstract void onOccur();
}
