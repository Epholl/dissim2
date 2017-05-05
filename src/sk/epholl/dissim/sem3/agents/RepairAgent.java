package sk.epholl.dissim.sem3.agents;

import OSPABA.*;
import sk.epholl.dissim.sem3.continualAssistants.RepairCarProcess;
import sk.epholl.dissim.sem3.managers.RepairManager;
import sk.epholl.dissim.sem3.simulation.Id;
import sk.epholl.dissim.sem3.simulation.Mc;

//meta! id="88"
public class RepairAgent extends Agent {
	public RepairAgent(int id, Simulation mySim, Agent parent) {
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
		new RepairManager(Id.repairManager, mySim(), this);
		new RepairCarProcess(Id.repairCarProcess, mySim(), this);
		addOwnMessage(Mc.repairWehicle);
	}
	//meta! tag="end"
}