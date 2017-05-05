package sk.epholl.dissim.sem3.managers;

import OSPABA.*;
import sk.epholl.dissim.sem3.agents.RepairAgent;
import sk.epholl.dissim.sem3.simulation.Mc;

//meta! id="88"
public class RepairManager extends Manager {
	public RepairManager(int id, Simulation mySim, Agent myAgent) {
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

	//meta! sender="RepairCarProcess", id="118", type="Finish"
	public void processFinish(MessageForm message) {
	}

	//meta! sender="CarShopModelAgent", id="96", type="Request"
	public void processRepairWehicle(MessageForm message) {
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

		case Mc.repairWehicle:
			processRepairWehicle(message);
		break;

		default:
			processDefault(message);
		break;
		}
	}
	//meta! tag="end"

	@Override
	public RepairAgent myAgent() {
		return (RepairAgent)super.myAgent();
	}

}