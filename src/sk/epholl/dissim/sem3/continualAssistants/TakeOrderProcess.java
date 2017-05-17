package sk.epholl.dissim.sem3.continualAssistants;

import OSPABA.*;
import OSPABA.Process;
import sk.epholl.dissim.generator.RandomGenerator;
import sk.epholl.dissim.sem3.agents.OfficeAgent;
import sk.epholl.dissim.sem3.entity.Vehicle;
import sk.epholl.dissim.sem3.simulation.Mc;
import sk.epholl.dissim.sem3.simulation.MyMessage;

//meta! id="120"
public class TakeOrderProcess extends Process {

	private RandomGenerator<Double> orderTakingGenerator;
	private RandomGenerator<Double> carEntrollmentGeneratior;

	public TakeOrderProcess(int id, Simulation mySim, CommonAgent myAgent) {
		super(id, mySim, myAgent);
	}

	@Override
	public void prepareReplication() {
		super.prepareReplication();

		orderTakingGenerator = myAgent().getSimulation().getParameters().getGenerators().getTakingOrderTimeGenerator();
		carEntrollmentGeneratior = myAgent().getSimulation().getParameters().getGenerators().getEnrollingCarTimeGenerator();
		// Setup component for the next replication
	}

	//meta! sender="OfficeAgent", id="121", type="Start"
	public void processStart(MessageForm message) {
		MyMessage msg = (MyMessage) message;
		double duration = orderTakingGenerator.nextValue() + carEntrollmentGeneratior.nextValue();
		msg.getVehicle().setCurrentState(Vehicle.State.TakeOrder, mySim().currentTime() + duration);
		msg.setCode(Mc.finish);
		hold(duration, msg);
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message) {
		MyMessage msg = (MyMessage) message;
		msg.getVehicle().persistCurrentState();
		switch (message.code()) {
			case Mc.finish:
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