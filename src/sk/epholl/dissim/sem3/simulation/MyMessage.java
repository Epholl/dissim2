package sk.epholl.dissim.sem3.simulation;

import OSPABA.*;
import sk.epholl.dissim.sem3.entity.FreeCapacity;
import sk.epholl.dissim.sem3.entity.Place;
import sk.epholl.dissim.sem3.entity.Vehicle;
import sk.epholl.dissim.sem3.entity.Worker1;

public class MyMessage extends MessageForm {

	private Vehicle vehicle;

	private Place place;

	private FreeCapacity capacity;

	private Worker1 worker1;

	public MyMessage(Simulation sim) {
		super(sim);
	}

	public MyMessage(MyMessage original) {
		super(original);
		// copy() is called in superclass
	}

	@Override
	public MessageForm createCopy() {
		return new MyMessage(this);
	}

	@Override
	protected void copy(MessageForm message) {
		super.copy(message);
		MyMessage original = (MyMessage)message;
		vehicle = original.vehicle;
		place = original.place;
		capacity = original.capacity;
		// Copy attributes
	}

	public Vehicle getVehicle() {
		return vehicle;
	}

	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}

	public Place getPlace() {
		return place;
	}

	public void setPlace(Place place) {
		this.place = place;
	}

	public FreeCapacity getCapacity() {
		return capacity;
	}

	public void setCapacity(final FreeCapacity object) {
		this.capacity = object;
	}

	public Worker1 getWorker1() {
		return worker1;
	}

	public void setWorker1(Worker1 worker1) {
		this.worker1 = worker1;
	}
}