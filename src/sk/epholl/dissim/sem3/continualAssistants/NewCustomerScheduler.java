package sk.epholl.dissim.sem3.continualAssistants;

import OSPABA.*;
import sk.epholl.dissim.generator.RandomGenerator;
import sk.epholl.dissim.sem2.entity.Car;
import sk.epholl.dissim.sem3.agents.SurroundingsAgent;
import sk.epholl.dissim.sem3.entity.Place;
import sk.epholl.dissim.sem3.simulation.Mc;
import sk.epholl.dissim.sem3.entity.Vehicle;
import sk.epholl.dissim.sem3.simulation.MyMessage;
import sk.epholl.dissim.sem3.simulation.MySimulation;

//meta! id="109"
public class NewCustomerScheduler extends Scheduler {

	private RandomGenerator<Double> newCustomerGenerator;

	private long idCounter;

	public NewCustomerScheduler(int id, Simulation mySim, CommonAgent myAgent) {
		super(id, mySim, myAgent);
	}



	@Override
	public void prepareReplication() {
		super.prepareReplication();
		newCustomerGenerator = myAgent().getSimulation().getParameters().getGenerators().getCustomerEntryRandom();
		idCounter = 0;
		// Setup component for the next replication
	}

	//meta! sender="SurroundingsAgent", id="110", type="Start"
	public void processStart(MessageForm message) {
		double duration = newCustomerGenerator.nextValue();
		message.setCode(Mc.customerEntry);
		hold(duration, message);
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message) {
		switch (message.code()) {
			case Mc.customerEntry:
				double duration = newCustomerGenerator.nextValue();
				MyMessage copy = (MyMessage) message.createCopy();
				final Vehicle vehicle = new Vehicle((MySimulation) mySim(), idCounter++);
				vehicle.addFinsihedState(Vehicle.State.EnterSystem);
				vehicle.setCurrentPlace(Place.Enterance);
				copy.setVehicle(vehicle);
				hold(duration, message);

				assistantFinished(copy);
		}
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	@Override
	public void processMessage(MessageForm message) {
		switch (message.code()) {
		case Mc.start:
			processStart(message);
		break;

		default:
			processDefault(message);
		break;
		}
	}
	//meta! tag="end"

	@Override
	public SurroundingsAgent myAgent() {
		return (SurroundingsAgent)super.myAgent();
	}

}