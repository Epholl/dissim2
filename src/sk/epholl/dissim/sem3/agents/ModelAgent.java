package sk.epholl.dissim.sem3.agents;

import OSPABA.*;
import sk.epholl.dissim.sem3.managers.ModelManager;
import sk.epholl.dissim.sem3.simulation.Id;
import sk.epholl.dissim.sem3.simulation.Mc;
import sk.epholl.dissim.sem3.simulation.MyMessage;
import sk.epholl.dissim.sem3.simulation.MySimulation;

//meta! id="132"
public class ModelAgent extends BaseAgent {
	public ModelAgent(int id, MySimulation mySim, Agent parent) {
		super(id, mySim, parent);
		init();
	}

	public void startSimulation() {
		MyMessage message = new MyMessage(mySim());
		message.setCode(Mc.init);
		message.setAddressee(Id.surroundingsAgent);
		manager().notice(message);
	}

	@Override
	public void prepareReplication() {
		super.prepareReplication();
		// Setup component for the next replication
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	private void init() {
		new ModelManager(Id.modelManager, mySim(), this);
		addOwnMessage(Mc.customerExit);
		addOwnMessage(Mc.customerEntry);
	}
	//meta! tag="end"
}