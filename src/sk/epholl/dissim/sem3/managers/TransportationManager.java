package sk.epholl.dissim.sem3.managers;

import OSPABA.*;
import sk.epholl.dissim.sem3.entity.Vehicle;
import sk.epholl.dissim.sem3.agents.TransportationAgent;
import sk.epholl.dissim.sem3.entity.Place;
import sk.epholl.dissim.sem3.simulation.Id;
import sk.epholl.dissim.sem3.simulation.Mc;
import sk.epholl.dissim.sem3.simulation.MyMessage;
import sk.epholl.dissim.sem3.simulation.Rst;

//meta! id="86"
public class TransportationManager extends Manager {
	public TransportationManager(int id, Simulation mySim, Agent myAgent) {
		super(id, mySim, myAgent);
		init();
	}

	@Override
	public void prepareReplication() {
		super.prepareReplication();
		// Setup component for the next replication

		if (petriNet() != null) {
			petriNet().clear();
		}
	}

	//meta! sender="MoveCarProcess", id="114", type="Finish"
	public void processFinish(MessageForm message) {
		MyMessage msg = (MyMessage) message;

		if (message.sender() == myAgent().findAssistant(Id.moveCarProcess)) {
			myAgent().publishValueContinous(Rst.CONSOLE_LOG, "Transfer finished: " + msg.getVehicle());
			msg.setCode(Mc.transferVehicle);
			response(msg);
		}
	}

	//meta! sender="CarShopModelAgent", id="91", type="Request"
	public void processTransferVehicle(MessageForm message) {
		final MyMessage msg = (MyMessage) message;
		final Vehicle vehicle = msg.getVehicle();
		final Place current = vehicle.getCurrentPlace();
		final Place destination = msg.getDestination();
		myAgent().publishValueContinous(Rst.CONSOLE_LOG, "Requested transport: " + current + " to " + destination + " for " + vehicle);
		vehicle.setCurrentRoad(myAgent().getRoadModel().getRoad(current, destination));

		msg.setAddressee(myAgent().findAssistant(Id.moveCarProcess));
		startContinualAssistant(msg);
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message) {
		switch (message.code()) {
		}
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	public void init() {
	}

	@Override
	public void processMessage(MessageForm message) {
		switch (message.code()) {
		case Mc.finish:
			processFinish(message);
		break;

		case Mc.transferVehicle:
			processTransferVehicle(message);
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