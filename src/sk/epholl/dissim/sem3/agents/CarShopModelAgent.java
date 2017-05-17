package sk.epholl.dissim.sem3.agents;

import OSPABA.*;
import sk.epholl.dissim.sem3.continualAssistants.DayEndProcess;
import sk.epholl.dissim.sem3.managers.CarShopModelManager;
import sk.epholl.dissim.sem3.simulation.Id;
import sk.epholl.dissim.sem3.simulation.Mc;
import sk.epholl.dissim.sem3.simulation.MySimulation;

//meta! id="83"
public class CarShopModelAgent extends BaseAgent {


	public CarShopModelAgent(int id, MySimulation mySim, Agent parent) {
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
		addOwnMessage(Mc.init);
		addOwnMessage(Mc.reserveSpot);
		addOwnMessage(Mc.freeSpot);
		addOwnMessage(Mc.parkCar);
		addOwnMessage(Mc.repairWehicle);
		addOwnMessage(Mc.parkingSpotsUpdate);
		addOwnMessage(Mc.customerEntry);
		addOwnMessage(Mc.takeOrder);
		addOwnMessage(Mc.transferVehicle);
	}
	//meta! tag="end"
}