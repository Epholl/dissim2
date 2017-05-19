package sk.epholl.dissim.sem3.managers;

import OSPABA.*;
import sk.epholl.dissim.sem3.agents.CarShopModelAgent;
import sk.epholl.dissim.sem3.entity.Place;
import sk.epholl.dissim.sem3.entity.Vehicle;
import sk.epholl.dissim.sem3.entity.Worker1;
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
		MyMessage msg = (MyMessage) message;
		msg.setAddressee(Id.transportationAgent);
		msg.setCode(Mc.transferVehicle);
		msg.setPlace(Place.Enterance);
		request(msg);
	}

	//meta! sender="ParkingAgent", id="94", type="Response"
	public void processParkCarParkingAgent(MessageForm message) {
		MyMessage msg = (MyMessage) message;
		MyMessage copy;
		final Vehicle vehicle = msg.getVehicle();
		final Place destination = msg.getPlace();
		switch (destination) {
			case MainLot:
				msg.setCode(Mc.takeOrder);
				msg.setAddressee(Id.officeAgent);
				request(msg);
				break;
			case ParkingLot1:
				msg.setCode(Mc.repairWehicle);
				msg.setAddressee(Id.repairAgent);
				Worker1 worker = vehicle.getWorker1();
				vehicle.setWorker1(null);
				copy = (MyMessage) msg.createCopy();
				copy.setWorker1(worker);
				copy.setAddressee(Id.officeAgent);
				copy.setCode(Mc.freeWorker1);

				request(msg);
				notice(copy);
				break;
			case ParkingLot2:
				response(msg);
		}
	}

	//meta! sender="RepairAgent", id="96", type="Response"
	public void processRepairWehicle(MessageForm message) {
		MyMessage msg = (MyMessage) message;
		msg.setCode(Mc.returnCar);
		msg.setAddressee(Id.officeAgent);
		request(msg);
	}

	//meta! sender="DayEndProcess", id="126", type="Finish"
	public void processFinish(MessageForm message) {
		MyMessage msg = (MyMessage) message;
		myAgent().publishValueContinous(Rst.CONSOLE_LOG, "End day message: " + myAgent().mySim().currentTime());
		msg.setCode(Mc.endOfDay);
		msg.setAddressee(Id.officeAgent);
		notice(msg);
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
		MyMessage msg = (MyMessage) message;
		final Vehicle vehicle = msg.getVehicle();
		if (vehicle.isOrderCancelled() || vehicle.isShopClosed()) {
			msg.setCode(Mc.transferVehicle);
			msg.setPlace(Place.Enterance);
			msg.setAddressee(Id.transportationAgent);
			request(msg);
		} else {
			msg.setCode(Mc.transferVehicle);
			msg.setPlace(Place.ParkingLot1);
			msg.setAddressee(Id.transportationAgent);
			request(msg);
		}
	}

	//meta! sender="TransportationAgent", id="91", type="Response"
	public void processTransferVehicleTransportationAgent(MessageForm message) {
		MyMessage msg = (MyMessage) message;
		Vehicle vehicle = msg.getVehicle();

		myAgent().publishValueContinous(Rst.CONSOLE_LOG, "Transfer finish: " + msg.getVehicle());
		switch (msg.getPlace()) {
			case MainLot:
				if (!vehicle.isRepaired()) {
					msg.setAddressee(Id.parkingAgent);
					msg.setCode(Mc.parkCar);
					request(msg);
				} else {
					response(msg);
				}
				break;
			case Enterance:
				msg.setAddressee(Id.modelAgent);
				msg.setCode(Mc.customerExit);
				msg.getVehicle().addFinsihedState(Vehicle.State.LeaveSystem);
				notice(msg);
				break;
			case ParkingLot1:
				msg.setAddressee(Id.parkingAgent);
				msg.setCode(Mc.parkCar);
				request(msg);
		}
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

		MessageForm dayEndInit = message.createCopy();
		dayEndInit.setAddressee(myAgent().findAssistant(Id.dayEndProcess));
		startContinualAssistant(dayEndInit);
	}

	//meta! sender="ParkingAgent", id="160", type="Response"
	public void processReserveSpotParkingAgent(MessageForm message) {
		response(message);
	}

	//meta! sender="OfficeAgent", id="166", type="Request"
	public void processReserveSpotOfficeAgent(MessageForm message) {
		MyMessage msg = (MyMessage) message;
		msg.setAddressee(Id.parkingAgent);
		request(msg);
	}

	//meta! sender="ParkingAgent", id="167", type="Notice"
	public void processParkingSpotsUpdate(MessageForm message) {
		MyMessage msg = (MyMessage) message;
		msg.setAddressee(Id.officeAgent);
		switch (msg.getPlace()) {
			case ParkingLot1:
				notice(msg);
				break;
			case ParkingLot2:
				MyMessage copy = (MyMessage) msg.createCopy();
				copy.setAddressee(Id.repairAgent);
				notice(msg);
				notice(copy);
				break;
		}
	}

	//meta! sender="TransportationAgent", id="176", type="Notice"
	public void processFreeSpotTransportationAgent(MessageForm message) {
		message.setAddressee(Id.parkingAgent);
		notice(message);
	}

	//meta! sender="RepairAgent", id="175", type="Notice"
	public void processFreeSpotRepairAgent(MessageForm message) {
		message.setAddressee(Id.parkingAgent);
		notice(message);
	}

	//meta! sender="RepairAgent", id="178", type="Request"
	public void processParkCarRepairAgent(MessageForm message) {
		message.setAddressee(Id.parkingAgent);
		request(message);
	}

	//meta! sender="OfficeAgent", id="183", type="Request"
	public void processTransferVehicleOfficeAgent(MessageForm message) {
		MyMessage msg = (MyMessage) message;
		MyMessage copy = (MyMessage) msg.createCopy();
		copy.setCode(Mc.freeSpot);
		copy.setPlace(Place.ParkingLot2);
		copy.setAddressee(Id.parkingAgent);
		notice(copy);
		msg.setPlace(Place.MainLot);
		msg.setAddressee(Id.transportationAgent);
		request(msg);
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	public void init() {
	}

	@Override
	public void processMessage(MessageForm message) {
		switch (message.code()) {
		case Mc.parkCar:
			switch (message.sender().id()) {
			case Id.parkingAgent:
				processParkCarParkingAgent(message);
			break;

			case Id.repairAgent:
				processParkCarRepairAgent(message);
			break;
			}
		break;

		case Mc.reserveSpot:
			switch (message.sender().id()) {
			case Id.officeAgent:
				processReserveSpotOfficeAgent(message);
			break;

			case Id.parkingAgent:
				processReserveSpotParkingAgent(message);
			break;
			}
		break;

		case Mc.finish:
			processFinish(message);
		break;

		case Mc.freeSpot:
			switch (message.sender().id()) {
			case Id.transportationAgent:
				processFreeSpotTransportationAgent(message);
			break;

			case Id.repairAgent:
				processFreeSpotRepairAgent(message);
			break;
			}
		break;

		case Mc.transferVehicle:
			switch (message.sender().id()) {
			case Id.transportationAgent:
				processTransferVehicleTransportationAgent(message);
			break;

			case Id.officeAgent:
				processTransferVehicleOfficeAgent(message);
			break;
			}
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

		case Mc.takeOrder:
			processTakeOrder(message);
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