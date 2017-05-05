package sk.epholl.dissim.sem3.agents;

import OSPABA.*;
import sk.epholl.dissim.sem3.continualAssistants.DayEndProcess;
import sk.epholl.dissim.sem3.managers.CarShopModelManager;
import sk.epholl.dissim.sem3.simulation.Id;
import sk.epholl.dissim.sem3.simulation.Mc;

//meta! id="83"
public class CarShopModelAgent extends Agent {
	public CarShopModelAgent(int id, Simulation mySim, Agent parent) {
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
		new CarShopModelManager(Id.carShopModelManager, mySim(), this);
		new DayEndProcess(Id.dayEndProcess, mySim(), this);
		addOwnMessage(Mc.returnCar);
		addOwnMessage(Mc.acquireParkingSpace);
		addOwnMessage(Mc.repairWehicle);
		addOwnMessage(Mc.customerEntry);
		addOwnMessage(Mc.takeOrder);
		addOwnMessage(Mc.transferVehicle);
	}
	//meta! tag="end"
}