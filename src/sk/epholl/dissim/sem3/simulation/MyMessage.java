package sk.epholl.dissim.sem3.simulation;

import OSPABA.*;
import sk.epholl.dissim.sem3.entity.Place;
import sk.epholl.dissim.sem3.entity.Vehicle;

public class MyMessage extends MessageForm {

	private Vehicle vehicle;

	private Place destination;

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
		((MyMessage) message).vehicle = original.vehicle;
		// Copy attributes
	}

	public Vehicle getVehicle() {
		return vehicle;
	}

	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}

	public Place getDestination() {
		return destination;
	}

	public void setDestination(Place destination) {
		this.destination = destination;
	}
}