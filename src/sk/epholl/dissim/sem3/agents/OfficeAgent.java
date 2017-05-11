package sk.epholl.dissim.sem3.agents;

import OSPABA.*;
import sk.epholl.dissim.sem3.continualAssistants.CancelCustomerProcess;
import sk.epholl.dissim.sem3.continualAssistants.ReturnCarProcess;
import sk.epholl.dissim.sem3.continualAssistants.TakeOrderProcess;
import sk.epholl.dissim.sem3.managers.OfficeManager;
import sk.epholl.dissim.sem3.simulation.Id;
import sk.epholl.dissim.sem3.simulation.Mc;
import sk.epholl.dissim.sem3.simulation.MySimulation;

//meta! id="87"
public class OfficeAgent extends BaseAgent {
	public OfficeAgent(int id, MySimulation mySim, Agent parent) {
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
		new OfficeManager(Id.officeManager, mySim(), this);
		new CancelCustomerProcess(Id.cancelCustomerProcess, mySim(), this);
		new TakeOrderProcess(Id.takeOrderProcess, mySim(), this);
		new ReturnCarProcess(Id.returnCarProcess, mySim(), this);
		addOwnMessage(Mc.returnCar);
		addOwnMessage(Mc.takeOrder);
	}
	//meta! tag="end"
}