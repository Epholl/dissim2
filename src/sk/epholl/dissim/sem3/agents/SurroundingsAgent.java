package sk.epholl.dissim.sem3.agents;

import OSPABA.*;
import sk.epholl.dissim.sem3.continualAssistants.NewCustomerScheduler;
import sk.epholl.dissim.sem3.managers.SurroundingsManager;
import sk.epholl.dissim.sem3.simulation.Id;
import sk.epholl.dissim.sem3.simulation.Mc;
import sk.epholl.dissim.sem3.simulation.MySimulation;

//meta! id="85"
public class SurroundingsAgent extends BaseAgent {
	public SurroundingsAgent(int id, MySimulation mySim, Agent parent) {
		super(id, mySim, parent);
		init();
		addOwnMessage(Mc.customerEntry);
	}

	@Override
	public void prepareReplication() {
		super.prepareReplication();
		// Setup component for the next replication
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	private void init() {
		new SurroundingsManager(Id.surroundingsManager, mySim(), this);
		new NewCustomerScheduler(Id.newCustomerScheduler, mySim(), this);
		addOwnMessage(Mc.init);
		addOwnMessage(Mc.customerExit);
	}
	//meta! tag="end"
}