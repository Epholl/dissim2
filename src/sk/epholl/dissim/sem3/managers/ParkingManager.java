package sk.epholl.dissim.sem3.managers;

import OSPABA.*;
import sk.epholl.dissim.sem3.agents.ParkingAgent;
import sk.epholl.dissim.sem3.entity.Place;
import sk.epholl.dissim.sem3.simulation.Mc;
import sk.epholl.dissim.sem3.simulation.MyMessage;
import sk.epholl.dissim.sem3.simulation.Rst;

//meta! id="92"
public class ParkingManager extends Manager {

	public ParkingManager(int id, Simulation mySim, Agent myAgent) {
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

	//meta! sender="CarShopModelAgent", id="94", type="Request"
	public void processParkCar(MessageForm message) {
		MyMessage msg = (MyMessage) message;
		Place destination = msg.getPlace();
		myAgent().publishValueContinous(Rst.CONSOLE_LOG, "Parking car: " + destination + " for " + msg.getVehicle());
		myAgent().parkCar(msg.getVehicle(), destination);
		response(msg);
	}

	//meta! userInfo="Removed from model"
	public void processFreeParkingSpace(MessageForm message) {
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message) {
		switch (message.code()) {
		}
	}

	//meta! sender="CarShopModelAgent", id="156", type="Notice"
	public void processInit(MessageForm message) {
		myAgent().initialCapacityNotices();
	}

	//meta! sender="CarShopModelAgent", id="160", type="Request"
	public void processReserveSpot(MessageForm message) {
		MyMessage msg = (MyMessage) message;
		myAgent().publishValueContinous(Rst.CONSOLE_LOG, "Requested parking spot on " + msg.getPlace() + " by " + msg.getVehicle());
		myAgent().reserveSpot(msg);
		response(msg);
	}

	//meta! sender="CarShopModelAgent", id="174", type="Notice"
	public void processFreeSpot(MessageForm message) {
		MyMessage msg = (MyMessage) message;
		myAgent().publishValueContinous(Rst.CONSOLE_LOG, "Freeing parking spot " + msg.getPlace() + " from " + msg.getVehicle());
		myAgent().freeSpot(msg.getVehicle(), msg.getPlace());
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	public void init() {
	}

	@Override
	public void processMessage(MessageForm message) {
		switch (message.code()) {
		case Mc.reserveSpot:
			processReserveSpot(message);
		break;

		case Mc.parkCar:
			processParkCar(message);
		break;

		case Mc.freeSpot:
			processFreeSpot(message);
		break;

		case Mc.init:
			processInit(message);
		break;

		default:
			processDefault(message);
		break;
		}
	}
	//meta! tag="end"

	@Override
	public ParkingAgent myAgent() {
		return (ParkingAgent)super.myAgent();
	}

}