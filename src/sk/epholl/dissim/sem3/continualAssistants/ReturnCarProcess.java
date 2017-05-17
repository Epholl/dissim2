package sk.epholl.dissim.sem3.continualAssistants;

import OSPABA.*;
import OSPABA.Process;
import sk.epholl.dissim.generator.RandomGenerator;
import sk.epholl.dissim.sem3.agents.OfficeAgent;
import sk.epholl.dissim.sem3.entity.Vehicle;
import sk.epholl.dissim.sem3.simulation.Mc;
import sk.epholl.dissim.sem3.simulation.MyMessage;
import sk.epholl.dissim.sem3.simulation.Rst;

//meta! id="122"
public class ReturnCarProcess extends Process {

	private RandomGenerator<Double> returnCarTimeGenerator;

	public ReturnCarProcess(int id, Simulation mySim, CommonAgent myAgent) {
		super(id, mySim, myAgent);
	}

	@Override
	public void prepareReplication() {
		super.prepareReplication();
		// Setup component for the next replication
		returnCarTimeGenerator = myAgent().getParams().getGenerators().getReturningCarTimeGenerator();
	}

	//meta! sender="OfficeAgent", id="123", type="Start"
	public void processStart(MessageForm message) {
		MyMessage msg = (MyMessage) message;
		Vehicle vehicle = msg.getVehicle();
		myAgent().publishValueContinous(Rst.CONSOLE_LOG, "Starting car return: " + vehicle);
		double duration = returnCarTimeGenerator.nextValue();
		vehicle.setCurrentState(Vehicle.State.RetrieveCar, mySim().currentTime() + duration);
		msg.setCode(Mc.finish);
		hold(duration, msg);
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message) {
		switch (message.code()) {
			case Mc.finish:
				MyMessage msg = (MyMessage) message;
				Vehicle vehicle = msg.getVehicle();
				myAgent().publishValueContinous(Rst.CONSOLE_LOG, "Car returned to customer: " + vehicle);
				vehicle.persistCurrentState();
				assistantFinished(message);
				break;
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
	public OfficeAgent myAgent() {
		return (OfficeAgent)super.myAgent();
	}

}