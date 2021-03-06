package sk.epholl.dissim.sem3.managers;

import OSPABA.*;
import sk.epholl.dissim.sem3.agents.ModelAgent;
import sk.epholl.dissim.sem3.simulation.Id;
import sk.epholl.dissim.sem3.simulation.Mc;

//meta! id="132"
public class ModelManager extends Manager {
	public ModelManager(int id, Simulation mySim, Agent myAgent) {
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

	//meta! sender="CarShopModelAgent", id="90", type="Notice"
	public void processCustomerExit(MessageForm message) {
		message.setAddressee(Id.surroundingsAgent);
		notice(message);
	}

	//meta! sender="SurroundingsAgent", id="89", type="Notice"
	public void processCustomerEntry(MessageForm message) {
		message.setAddressee(Id.carShopModelAgent);
		notice(message);
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
		case Mc.customerEntry:
			processCustomerEntry(message);
		break;

		case Mc.customerExit:
			processCustomerExit(message);
		break;

		default:
			processDefault(message);
		break;
		}
	}
	//meta! tag="end"

	@Override
	public ModelAgent myAgent() {
		return (ModelAgent)super.myAgent();
	}

}