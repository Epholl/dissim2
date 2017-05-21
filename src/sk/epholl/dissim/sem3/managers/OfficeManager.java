package sk.epholl.dissim.sem3.managers;

import OSPABA.*;
import sk.epholl.dissim.sem3.entity.FreeCapacity;
import sk.epholl.dissim.sem3.entity.Place;
import sk.epholl.dissim.sem3.entity.Vehicle;
import sk.epholl.dissim.sem3.agents.OfficeAgent;
import sk.epholl.dissim.sem3.entity.Worker1;
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
		MyMessage msg = (MyMessage) message;
		myAgent().onRepairedCarReady(msg);
		myAgent().publishValueContinous(Rst.CONSOLE_LOG, "Requested returning vehicle: " + msg.getVehicle());
	}

	//meta! sender="CancelCustomerProcess", id="129", type="Finish"
	public void processFinishCancelCustomerProcess(MessageForm message) {
		MyMessage msg = (MyMessage) message;
		myAgent().onCustomerWaitTimeout(msg);
	}

	//meta! sender="TakeOrderProcess", id="121", type="Finish"
	public void processFinishTakeOrderProcess(MessageForm message) {
		MyMessage msg = (MyMessage) message;
		final Vehicle vehicle = msg.getVehicle();
		msg.setCode(Mc.takeOrder);

		MyMessage copy = (MyMessage) msg.createCopy();
		copy.setAddressee(Id.carShopModelAgent);
		copy.setCode(Mc.freeSpot);
		copy.setPlace(Place.MainLot);
		notice(copy);
		myAgent().publishValueContinous(Rst.CONSOLE_LOG, "Order taking finished: " + vehicle);
		response(msg);
	}

	//meta! sender="ReturnCarProcess", id="123", type="Finish"
	public void processFinishReturnCarProcess(MessageForm message) {
		myAgent().onCarReturned((MyMessage) message);
	}

	//meta! sender="CarShopModelAgent", id="93", type="Request"
	public void processTakeOrder(MessageForm message) {
		MyMessage msg = (MyMessage) message;
		final Vehicle vehicle = msg.getVehicle();
		myAgent().onNewCarArrived(msg);

		myAgent().publishValueContinous(Rst.CONSOLE_LOG, "Vehicle waiting to take order: " + vehicle);
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
		MyMessage msg = (MyMessage) message;
		if (!msg.getVehicle().isRepaired()) {
			myAgent().startTakingOrder(msg);
		}
	}

	//meta! sender="CarShopModelAgent", id="169", type="Notice"
	public void processParkingSpotsUpdate(MessageForm message) {
		MyMessage msg = (MyMessage) message;
		FreeCapacity capacity = msg.getCapacity();
		Place parkingLot = msg.getPlace();
		if (parkingLot == Place.ParkingLot1) {
			myAgent().setLot1FreeParkingSpots(capacity);
			myAgent().findWork();
		} else if (parkingLot == Place.ParkingLot2) {
			myAgent().setLot2FreeParkingSpots(capacity);
			myAgent().findWork();
		}

		myAgent().publishValueContinous(Rst.CONSOLE_LOG, "Parking spot capacity notice: " + msg.getPlace() + ": " + capacity);
	}

	//meta! sender="CarShopModelAgent", id="172", type="Notice"
	public void processFreeWorker1(MessageForm message) {
		MyMessage msg = (MyMessage) message;
		Worker1 worker = msg.getWorker1();
		myAgent().publishValueContinous(Rst.CONSOLE_LOG, "Worker freed: " + worker);
		myAgent().freeWorker(worker);
	}

	//meta! sender="CarShopModelAgent", id="183", type="Response"
	public void processTransferVehicle(MessageForm message) {
		myAgent().onReadyToRetrieveCar((MyMessage) message);
	}

	//meta! sender="CarShopModelAgent", id="186", type="Notice"
	public void processEndOfDay(MessageForm message) {
		MyMessage msg = (MyMessage) message;
		myAgent().onEndOfDay(msg);
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

		case Mc.freeWorker1:
			processFreeWorker1(message);
		break;

		case Mc.init:
			processInit(message);
		break;

		case Mc.parkingSpotsUpdate:
			processParkingSpotsUpdate(message);
		break;

		case Mc.endOfDay:
			processEndOfDay(message);
		break;

		case Mc.takeOrder:
			processTakeOrder(message);
		break;

		case Mc.returnCar:
			processReturnCar(message);
		break;

		case Mc.reserveSpot:
			processReserveSpot(message);
		break;

		case Mc.transferVehicle:
			processTransferVehicle(message);
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