package sk.epholl.dissim.sem3.agents;

import OSPABA.*;
import sk.epholl.dissim.sem3.continualAssistants.CancelCustomerProcess;
import sk.epholl.dissim.sem3.continualAssistants.ReturnCarProcess;
import sk.epholl.dissim.sem3.continualAssistants.TakeOrderProcess;
import sk.epholl.dissim.sem3.entity.FreeCapacity;
import sk.epholl.dissim.sem3.entity.Place;
import sk.epholl.dissim.sem3.entity.Worker1;
import sk.epholl.dissim.sem3.entity.Vehicle;
import sk.epholl.dissim.sem3.entity.deciders.Worker1Decider;
import sk.epholl.dissim.sem3.entity.deciders.Worker1Decision;
import sk.epholl.dissim.sem3.managers.OfficeManager;
import sk.epholl.dissim.sem3.simulation.*;
import sk.epholl.dissim.util.Pair;
import sk.epholl.dissim.util.StatisticQueue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;

//meta! id="87"
public class OfficeAgent extends BaseAgent {

	private int worker1IdCounter;

	private LinkedList<Worker1> type1FreeWorkers;

	private Worker1[] type1Workers;

	private FreeCapacity lot1FreeParkingSpots;

	private FreeCapacity lot2FreeParkingSpots;

	private StatisticQueue<MyMessage> vehiclesWaitingForOrder;

	private HashSet<MyMessage> vehiclesOrdering;

	private StatisticQueue<MyMessage> vehiclesToBeReturned;

	private Worker1Decider decider;

	public OfficeAgent(int id, MySimulation mySim, Agent parent) {
		super(id, mySim, parent);
		init();

		type1FreeWorkers = new LinkedList<>();
		vehiclesWaitingForOrder = new StatisticQueue<>(getSimulation());
		vehiclesOrdering = new HashSet<>();
		vehiclesToBeReturned = new StatisticQueue<>(getSimulation());
	}

	@Override
	public void prepareReplication() {
		super.prepareReplication();
		// Setup component for the next replication

		worker1IdCounter = 0;
		final int type1Count = getParams().getType1WorkerCount();
		type1Workers = new Worker1[type1Count];
		for (int i = 0; i < type1Workers.length; i++) {
			type1Workers[i] = new Worker1(worker1IdCounter++);
		}
		type1FreeWorkers.clear();
		type1FreeWorkers.addAll(Arrays.asList(type1Workers));
		vehiclesWaitingForOrder.clear();
		vehiclesOrdering.clear();
		vehiclesToBeReturned.clear();

	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	private void init() {
		new OfficeManager(Id.officeManager, mySim(), this);
		new CancelCustomerProcess(Id.cancelCustomerProcess, mySim(), this);
		new TakeOrderProcess(Id.takeOrderProcess, mySim(), this);
		new ReturnCarProcess(Id.returnCarProcess, mySim(), this);
		addOwnMessage(Mc.returnCar);
		addOwnMessage(Mc.init);
		addOwnMessage(Mc.freeWorker1);
		addOwnMessage(Mc.reserveSpot);
		addOwnMessage(Mc.parkingSpotsUpdate);
		addOwnMessage(Mc.takeOrder);
	}
	//meta! tag="end"

	public void onNewCarArrived(MyMessage message) {
		vehiclesWaitingForOrder.add(new Pair<>(mySim().currentTime(), message));
		MyMessage cancelOrder = (MyMessage) message.createCopy();
		cancelOrder.setCode(Mc.start);
		cancelOrder.setAddressee(findAssistant(Id.cancelCustomerProcess));
		manager().notice(cancelOrder);
		findWork();
	}

	public void onCustomerWaitTimeout(MyMessage message) {
		final Vehicle vehicle = message.getVehicle();
		if (!vehiclesWaitingForOrder.isEmpty() && vehiclesWaitingForOrder.peek().second.getVehicle() == vehicle) {
			publishValueContinous(Rst.CONSOLE_LOG, "Vehicle cancels order after waiting too long " + vehicle);
			MyMessage msg = vehiclesWaitingForOrder.dequeue();
			vehicle.addFinsihedState(Vehicle.State.CancelOrder);
			manager().response(msg);
		}
	}

	public void findWork() {
		if (hasFreeWorkers() && hasWork()) {
			if (!hasOrdersToTake()) {
				returnRepairedCar();
			} else if (!hasCarsToReturn()) {
				prepareTakingOrder();
			} else {
				Worker1Decision decision = decider.evaluate();

				if (decision == Worker1Decision.TakeOrder && canTakeNewOrder()) {
					prepareTakingOrder();
				} else {
					returnRepairedCar();
				}
			}
		}
	}

	private void prepareTakingOrder() {
		MyMessage msg = vehiclesWaitingForOrder.dequeue();
		vehiclesOrdering.add(msg);
		msg.getVehicle().setWorker1(assignWorker());

		msg.setCode(Mc.reserveSpot);
		msg.setPlace(Place.ParkingLot1);
		msg.setAddressee(Id.carShopModelAgent);
		manager().request(msg);
	}

	public void startTakingOrder(MyMessage msg) {
		msg.setAddressee(Id.takeOrderProcess);
		manager().startContinualAssistant(msg);
	}

	private void returnRepairedCar() {

	}

	private Worker1 assignWorker() {
		Worker1 worker = type1FreeWorkers.removeLast();
		return worker;
	}

	public void freeWorker(Worker1 worker) {
		if (type1FreeWorkers.contains(worker)) {
			throw new AssertionError("Attempted to free an already free worker: " + worker);
		}
		type1FreeWorkers.add(worker);
		findWork();
	}

	public FreeCapacity getLot1FreeParkingSpots() {
		return lot1FreeParkingSpots;
	}

	public void setLot1FreeParkingSpots(FreeCapacity lot1FreeParkingSpots) {
		this.lot1FreeParkingSpots = lot1FreeParkingSpots;
		findWork();
	}

	public FreeCapacity getLot2FreeParkingSpots() {
		return lot2FreeParkingSpots;
	}

	public void setLot2FreeParkingSpots(FreeCapacity lot2FreeParkingSpots) {
		this.lot2FreeParkingSpots = lot2FreeParkingSpots;
	}

	public boolean hasOrdersToTake() {
		return !vehiclesWaitingForOrder.isEmpty();
	}

	public boolean hasFreeWorkers() {
		return !type1FreeWorkers.isEmpty();
	}

	public boolean canTakeNewOrder() {
		return hasFreeWorkers()
				&& hasOrdersToTake()
				&& lot1FreeParkingSpots.getFreeUnits() > 0;
	}

	public boolean hasCarsToReturn() {
		return !vehiclesToBeReturned.isEmpty();
	}

	public boolean hasWork() {
		return hasCarsToReturn() || hasOrdersToTake();
	}

	public void setDecider(Worker1Decider decider) {
		decider.setAgent(this);
		this.decider = decider;
	}
}