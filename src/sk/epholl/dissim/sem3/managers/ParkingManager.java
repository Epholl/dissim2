package sk.epholl.dissim.sem3.managers;

import OSPABA.*;
import sk.epholl.dissim.sem3.agents.ParkingAgent;
import sk.epholl.dissim.sem3.simulation.Mc;

//meta! id="92"
public class ParkingManager extends Manager {
	public ParkingManager(int id, Simulation mySim, Agent myAgent) {
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

	//meta! sender="CarShopModelAgent", id="94", type="Request"
	public void processAcquireParkingSpace(MessageForm message) {
	}

	//meta! sender="CarShopModelAgent", id="95", type="Notice"
	public void processFreeParkingSpace(MessageForm message) {
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
		case Mc.freeParkingSpace:
			processFreeParkingSpace(message);
		break;

		case Mc.acquireParkingSpace:
			processAcquireParkingSpace(message);
		break;

		default:
			processDefault(message);
		break;
		}
	}
	//meta! tag="end"

	@Override
	public ParkingAgent myAgent() {
		return (ParkingAgent)super.myAgent();
	}

}