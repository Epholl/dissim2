package sk.epholl.dissim.sem3.agents;

import OSPABA.*;
import sk.epholl.dissim.sem3.managers.ParkingManager;
import sk.epholl.dissim.sem3.simulation.Id;
import sk.epholl.dissim.sem3.simulation.Mc;
import sk.epholl.dissim.sem3.simulation.MySimulation;

//meta! id="92"
public class ParkingAgent extends BaseAgent {
	public ParkingAgent(int id, MySimulation mySim, Agent parent) {
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
		new ParkingManager(Id.parkingManager, mySim(), this);
		addOwnMessage(Mc.acquireParkingSpace);
		addOwnMessage(Mc.freeParkingSpace);
	}
	//meta! tag="end"
}