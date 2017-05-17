package sk.epholl.dissim.sem3.continualAssistants;

import OSPABA.*;
import OSPABA.Process;
import sk.epholl.dissim.sem3.agents.TransportationAgent;
import sk.epholl.dissim.sem3.entity.Road;
import sk.epholl.dissim.sem3.entity.Vehicle;
import sk.epholl.dissim.sem3.simulation.Mc;
import sk.epholl.dissim.sem3.simulation.MyMessage;

//meta! id="113"
public class MoveCarProcess extends Process {
	public MoveCarProcess(int id, Simulation mySim, CommonAgent myAgent) {
		super(id, mySim, myAgent);
	}

	@Override
	public void prepareReplication() {
		super.prepareReplication();
		// Setup component for the next replication
	}

	//meta! sender="TransportationAgent", id="114", type="Start"
	public void processStart(MessageForm message) {
		MyMessage msg = (MyMessage) message;
		final Vehicle vehicle = msg.getVehicle();
		final Road road = vehicle.getCurrentRoad();
		final double duration = road.getTotalDuration();
		vehicle.setCurrentState(road.getVehicleState(), mySim().currentTime() + duration);

		msg.setCode(Mc.finish);
		hold(duration, msg);
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message) {
		switch (message.code()) {
			case Mc.finish:
				MyMessage msg = (MyMessage) message;
				final Vehicle vehicle = msg.getVehicle();
				vehicle.persistCurrentState();
				vehicle.setCurrentPlace(msg.getPlace());
				assistantFinished(msg);
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
	public TransportationAgent myAgent() {
		return (TransportationAgent)super.myAgent();
	}

}