package sk.epholl.dissim.sem3.agents;

import OSPABA.*;
import sk.epholl.dissim.sem3.continualAssistants.MoveCarProcess;
import sk.epholl.dissim.sem3.managers.TransportationManager;
import sk.epholl.dissim.sem3.simulation.Id;
import sk.epholl.dissim.sem3.simulation.Mc;

//meta! id="86"
public class TransportationAgent extends Agent {
	public TransportationAgent(int id, Simulation mySim, Agent parent) {
		super(id, mySim, parent);
		init();
	}

	@Override
	public void prepareReplication() {
		super.prepareReplication();
		// Setup component for the next replication
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	private void init() {
		new TransportationManager(Id.transportationManager, mySim(), this);
		new MoveCarProcess(Id.moveCarProcess, mySim(), this);
		addOwnMessage(Mc.transferVehicle);
	}
	//meta! tag="end"
}