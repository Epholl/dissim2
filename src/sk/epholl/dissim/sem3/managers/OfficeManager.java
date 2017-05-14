package sk.epholl.dissim.sem3.managers;

import OSPABA.*;
import sk.epholl.dissim.sem3.agents.OfficeAgent;
import sk.epholl.dissim.sem3.simulation.Id;
import sk.epholl.dissim.sem3.simulation.Mc;
import sk.epholl.dissim.sem3.simulation.MyMessage;
import sk.epholl.dissim.sem3.simulation.Rst;

//meta! id="87"
public class OfficeManager extends Manager {
	public OfficeManager(int id, Simulation mySim, Agent myAgent) {
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

	//meta! sender="CarShopModelAgent", id="97", type="Request"
	public void processReturnCar(MessageForm message) {
	}

	//meta! sender="CancelCustomerProcess", id="129", type="Finish"
	public void processFinishCancelCustomerProcess(MessageForm message) {
	}

	//meta! sender="TakeOrderProcess", id="121", type="Finish"
	public void processFinishTakeOrderProcess(MessageForm message) {
	}

	//meta! sender="ReturnCarProcess", id="123", type="Finish"
	public void processFinishReturnCarProcess(MessageForm message) {
	}

	//meta! sender="CarShopModelAgent", id="93", type="Request"
	public void processTakeOrder(MessageForm message) {
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message) {
		switch (message.code()) {
		}
	}

	//meta! sender="CarShopModelAgent", id="158", type="Notice"
	public void processInit(MessageForm message) {
	}

	//meta! sender="CarShopModelAgent", id="166", type="Response"
	public void processReserveSpot(MessageForm message) {
	}

	//meta! sender="CarShopModelAgent", id="169", type="Notice"
	public void processParkingSpotsUpdate(MessageForm message) {
		MyMessage msg = (MyMessage) message;
		int freeSpots = (Integer) msg.getVariable();
		myAgent().setLot1FreeParkingSpots(freeSpots);
		myAgent().publishValueContinous(Rst.CONSOLE_LOG, "Parking spot capacity notice: " + msg.getPlace() + ": " + freeSpots);
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	public void init() {
	}

	@Override
	public void processMessage(MessageForm message) {
		switch (message.code()) {
		case Mc.finish:
			switch (message.sender().id()) {
			case Id.takeOrderProcess:
				processFinishTakeOrderProcess(message);
			break;

			case Id.returnCarProcess:
				processFinishReturnCarProcess(message);
			break;

			case Id.cancelCustomerProcess:
				processFinishCancelCustomerProcess(message);
			break;
			}
		break;

		case Mc.takeOrder:
			processTakeOrder(message);
		break;

		case Mc.init:
			processInit(message);
		break;

		case Mc.parkingSpotsUpdate:
			processParkingSpotsUpdate(message);
		break;

		case Mc.returnCar:
			processReturnCar(message);
		break;

		case Mc.reserveSpot:
			processReserveSpot(message);
		break;

		default:
			processDefault(message);
		break;
		}
	}
	//meta! tag="end"

	@Override
	public OfficeAgent myAgent() {
		return (OfficeAgent)super.myAgent();
	}

}