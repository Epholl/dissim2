package sk.epholl.dissim.sem3.managers;

import OSPABA.*;
import sk.epholl.dissim.sem3.agents.CarShopModelAgent;
import sk.epholl.dissim.sem3.simulation.Mc;

//meta! id="83"
public class CarShopModelManager extends Manager {
	public CarShopModelManager(int id, Simulation mySim, Agent myAgent) {
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

	//meta! sender="OfficeAgent", id="97", type="Response"
	public void processReturnCar(MessageForm message) {
	}

	//meta! sender="ParkingAgent", id="94", type="Response"
	public void processAcquireParkingSpace(MessageForm message) {
	}

	//meta! sender="RepairAgent", id="96", type="Response"
	public void processRepairWehicle(MessageForm message) {
	}

	//meta! sender="DayEndProcess", id="126", type="Finish"
	public void processFinish(MessageForm message) {
	}

	//meta! sender="ModelAgent", id="134", type="Notice"
	public void processCustomerEntry(MessageForm message) {
	}

	//meta! sender="OfficeAgent", id="93", type="Response"
	public void processTakeOrder(MessageForm message) {
	}

	//meta! sender="TransportationAgent", id="91", type="Response"
	public void processTransferVehicle(MessageForm message) {
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
		case Mc.transferVehicle:
			processTransferVehicle(message);
		break;

		case Mc.takeOrder:
			processTakeOrder(message);
		break;

		case Mc.acquireParkingSpace:
			processAcquireParkingSpace(message);
		break;

		case Mc.returnCar:
			processReturnCar(message);
		break;

		case Mc.repairWehicle:
			processRepairWehicle(message);
		break;

		case Mc.customerEntry:
			processCustomerEntry(message);
		break;

		case Mc.finish:
			processFinish(message);
		break;

		default:
			processDefault(message);
		break;
		}
	}
	//meta! tag="end"

	@Override
	public CarShopModelAgent myAgent() {
		return (CarShopModelAgent)super.myAgent();
	}

}