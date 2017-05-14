package sk.epholl.dissim.sem3.managers;

import OSPABA.*;
import sk.epholl.dissim.sem3.agents.CarShopModelAgent;
import sk.epholl.dissim.sem3.entity.Place;
import sk.epholl.dissim.sem3.simulation.Id;
import sk.epholl.dissim.sem3.simulation.Mc;
import sk.epholl.dissim.sem3.simulation.MyMessage;
import sk.epholl.dissim.sem3.simulation.Rst;

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
	public void processParkCar(MessageForm message) {
	}

	//meta! sender="RepairAgent", id="96", type="Response"
	public void processRepairWehicle(MessageForm message) {
	}

	//meta! sender="DayEndProcess", id="126", type="Finish"
	public void processFinish(MessageForm message) {
		MyMessage msg = (MyMessage) message;

	}

	//meta! sender="ModelAgent", id="134", type="Notice"
	public void processCustomerEntry(MessageForm message) {
		MyMessage msg = (MyMessage) message;
		msg.setCode(Mc.transferVehicle);
		msg.setPlace(Place.MainLot);
		msg.setAddressee(Id.transportationAgent);
		request(msg);
	}

	//meta! sender="OfficeAgent", id="93", type="Response"
	public void processTakeOrder(MessageForm message) {
	}

	//meta! sender="TransportationAgent", id="91", type="Response"
	public void processTransferVehicle(MessageForm message) {
		MyMessage msg = (MyMessage) message;

		myAgent().publishValueContinous(Rst.CONSOLE_LOG, "Transfer finish: " + msg.getVehicle());
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message) {
		switch (message.code()) {
		}
	}

	//meta! sender="ModelAgent", id="152", type="Notice"
	public void processInit(MessageForm message) {

		message.setCode(Mc.init);
		MessageForm transportationAgentInit = message.createCopy();
		transportationAgentInit.setAddressee(Id.transportationAgent);
		notice(transportationAgentInit);

		MessageForm parkingAgentInit = message.createCopy();
		parkingAgentInit.setAddressee(Id.parkingAgent);
		notice(parkingAgentInit);

		MessageForm repairAgentInit = message.createCopy();
		repairAgentInit.setAddressee(Id.repairAgent);
		notice(repairAgentInit);

		MessageForm officeAgentInit = message.createCopy();
		officeAgentInit.setAddressee(Id.officeAgent);
		notice(officeAgentInit);

	}

	//meta! sender="ParkingAgent", id="160", type="Request"
	public void processReserveSpotParkingAgent(MessageForm message) {
	}

	//meta! sender="OfficeAgent", id="166", type="Request"
	public void processReserveSpotOfficeAgent(MessageForm message) {
	}

	//meta! sender="ParkingAgent", id="167", type="Notice"
	public void processParkingSpotsUpdate(MessageForm message) {
		MyMessage msg = (MyMessage) message;
		switch (msg.getPlace()) {
			case ParkingLot1:
				msg.setAddressee(Id.officeAgent);
				notice(msg);
				break;
			case ParkingLot2:
				msg.setAddressee(Id.repairAgent);
				notice(msg);
				break;
		}
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	public void init() {
	}

	@Override
	public void processMessage(MessageForm message) {
		switch (message.code()) {
		case Mc.finish:
			processFinish(message);
		break;

		case Mc.transferVehicle:
			processTransferVehicle(message);
		break;

		case Mc.repairWehicle:
			processRepairWehicle(message);
		break;

		case Mc.customerEntry:
			processCustomerEntry(message);
		break;

		case Mc.parkingSpotsUpdate:
			processParkingSpotsUpdate(message);
		break;

		case Mc.reserveSpot:
			switch (message.sender().id()) {
			case Id.parkingAgent:
				processReserveSpotParkingAgent(message);
			break;

			case Id.officeAgent:
				processReserveSpotOfficeAgent(message);
			break;
			}
		break;

		case Mc.takeOrder:
			processTakeOrder(message);
		break;

		case Mc.parkCar:
			processParkCar(message);
		break;

		case Mc.init:
			processInit(message);
		break;

		case Mc.returnCar:
			processReturnCar(message);
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