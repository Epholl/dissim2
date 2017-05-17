package sk.epholl.dissim.sem3.managers;

import OSPABA.*;
import sk.epholl.dissim.sem3.agents.RepairAgent;
import sk.epholl.dissim.sem3.entity.FreeCapacity;
import sk.epholl.dissim.sem3.entity.Place;
import sk.epholl.dissim.sem3.simulation.Mc;
import sk.epholl.dissim.sem3.simulation.MyMessage;
import sk.epholl.dissim.sem3.simulation.Rst;

//meta! id="88"
public class RepairManager extends Manager {
	public RepairManager(int id, Simulation mySim, Agent myAgent) {
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

	//meta! sender="RepairCarProcess", id="118", type="Finish"
	public void processFinish(MessageForm message) {
	}

	//meta! sender="CarShopModelAgent", id="96", type="Request"
	public void processRepairWehicle(MessageForm message) {
		MyMessage msg = (MyMessage) message;
		myAgent().publishValueContinous(Rst.CONSOLE_LOG, "Car available for repair: " + msg.getVehicle());
		myAgent().newVehicleArrived(msg);

	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message) {
		switch (message.code()) {
		}
	}

	//meta! sender="CarShopModelAgent", id="157", type="Notice"
	public void processInit(MessageForm message) {
	}

	//meta! sender="CarShopModelAgent", id="168", type="Notice"
	public void processParkingSpotsUpdate(MessageForm message) {
		MyMessage msg = (MyMessage) message;
		FreeCapacity capacity = msg.getCapacity();
		Place parkingLot = msg.getPlace();
		if (parkingLot == Place.ParkingLot2) {
			myAgent().setLot2FreeParkingSpots(capacity);
		}
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	public void init() {
	}

	@Override
	public void processMessage(MessageForm message) {
		switch (message.code()) {
		case Mc.init:
			processInit(message);
		break;

		case Mc.parkingSpotsUpdate:
			processParkingSpotsUpdate(message);
		break;

		case Mc.finish:
			processFinish(message);
		break;

		case Mc.repairWehicle:
			processRepairWehicle(message);
		break;

		default:
			processDefault(message);
		break;
		}
	}
	//meta! tag="end"

	@Override
	public RepairAgent myAgent() {
		return (RepairAgent)super.myAgent();
	}

}