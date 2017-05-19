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
import sk.epholl.dissim.util.StatisticCounter;
import sk.epholl.dissim.util.StatisticQueue;

import java.util.*;

//meta! id="87"
public class OfficeAgent extends BaseAgent {

	private StatisticCounter avgFreeWorkersCounter = new StatisticCounter();
	private StatisticCounter avgWorkerLoadCounter = new StatisticCounter();

	private int worker1IdCounter;

	private StatisticQueue<Worker1> type1FreeWorkers;

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

		type1FreeWorkers = new StatisticQueue<>(getSimulation());
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
		type1FreeWorkers.clear();
		for (int i = 0; i < type1Workers.length; i++) {
			type1Workers[i] = new Worker1(getSimulation(), worker1IdCounter++);
			type1FreeWorkers.enqueue(type1Workers[i]);
		}
		setDecider(getParams().getWorker1Decider());
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
		addOwnMessage(Mc.endOfDay);
		addOwnMessage(Mc.returnCar);
		addOwnMessage(Mc.init);
		addOwnMessage(Mc.freeWorker1);
		addOwnMessage(Mc.reserveSpot);
		addOwnMessage(Mc.parkingSpotsUpdate);
		addOwnMessage(Mc.takeOrder);
		addOwnMessage(Mc.transferVehicle);
	}
	//meta! tag="end"


	@Override
	public void onGuiUpdate() {
		super.onGuiUpdate();
		List<Rst.WorkerState> states = new ArrayList<>();
		for (Worker1 worker: type1Workers) {
			states.add(worker.getWorkerState());
		}
		Rst.WorkerUpdate update = new Rst.WorkerUpdate();
		update.states = states;
		publishValueContinous(Rst.WORKER1_STATE, update);

	}

	@Override
	public void onReplicationFinished() {
		super.onReplicationFinished();

		avgFreeWorkersCounter.addValue(type1FreeWorkers.getAverageQueueLength());
		double workerLoad = 0d;
		for (Worker1 worker: type1Workers) {
			workerLoad += worker.getWorkLoadCoeficient();
		}
		workerLoad /= type1Workers.length;
		avgWorkerLoadCounter.addValue(workerLoad);


		publishValueIfAfterWarmup(Rst.R_AVERAGE_FREE_WORKERS_1,
				new Rst.Result(rep(), avgFreeWorkersCounter));
		publishValueIfAfterWarmup(Rst.R_AVERAGE_LOAD_WORKERS_1,
				new Rst.Result(rep(), avgWorkerLoadCounter));
	}

	public void onNewCarArrived(MyMessage message) {
		vehiclesWaitingForOrder.add(new Pair<>(mySim().currentTime(), message));
		message.getVehicle().setCurrentState(Vehicle.State.WaitForOrder);
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
			vehicle.persistCurrentState();
			vehicle.addFinsihedState(Vehicle.State.CancelOrder);
			manager().response(msg);
		}
	}

	public void onRepairedCarReady(MyMessage message) {
		vehiclesToBeReturned.enqueue(message);
		findWork();
	}

	public void onReadyToRetrieveCar(MyMessage message) {
		message.setAddressee(findAssistant(Id.returnCarProcess));
		manager().startContinualAssistant(message);
	}

	public void onCarReturned(MyMessage message) {
		Vehicle vehicle = message.getVehicle();
		Worker1 worker = vehicle.getWorker1();
		freeWorker(worker);
		vehicle.setWorker1(null);
		findWork();
		message.setCode(Mc.returnCar);
		manager().response(message);
	}

	public void onEndOfDay(MyMessage message) {
		int waitingVehiclesCount = vehiclesWaitingForOrder.size();
		publishValueContinous(Rst.CONSOLE_LOG, "End of day cancels " + waitingVehiclesCount + " + customers.");
		while (!vehiclesWaitingForOrder.isEmpty()) {
			MyMessage msg = vehiclesWaitingForOrder.dequeue();
			Vehicle vehicle = msg.getVehicle();
			vehicle.persistCurrentState();
			vehicle.addFinsihedState(Vehicle.State.ShopClosed);
			manager().response(msg);
		}

	}

	public void findWork() {
		if (hasFreeWorkers() && hasWork()) {
			if (!hasOrdersToTake() || !canTakeNewOrder()) {
				returnRepairedCar();
			} else if (!hasCarsToReturn()) {
				prepareTakingOrder();
			} else {
				Worker1Decision decision = decider.evaluate();

				if (decision == Worker1Decision.TakeOrder) {
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
		Worker1 worker = assignWorker();
		worker.setState(Worker1.State.TakingOrder);
		msg.getVehicle().setWorker1(worker);
		msg.getVehicle().persistCurrentState();
		lot1FreeParkingSpots.occupySingleSpot();

		msg.setCode(Mc.reserveSpot);
		msg.setPlace(Place.ParkingLot1);
		msg.setAddressee(Id.carShopModelAgent);
		manager().request(msg);
	}

	public void startTakingOrder(MyMessage msg) {
		msg.getVehicle().addFinsihedState(Vehicle.State.StartTakingOrder);
		msg.setAddressee(Id.takeOrderProcess);
		manager().startContinualAssistant(msg);
	}

	private void returnRepairedCar() {
		MyMessage msg = vehiclesToBeReturned.dequeue();
		Worker1 worker = assignWorker();
		worker.setState(Worker1.State.ReturningVehicle);
		Vehicle vehicle = msg.getVehicle();
		vehicle.setWorker1(worker);
		vehicle.persistCurrentState();
		vehicle.addFinsihedState(Vehicle.State.LeaveFromLot2);
		msg.setCode(Mc.transferVehicle);
		msg.setAddressee(Id.carShopModelAgent);
		manager().request(msg);
	}

	private Worker1 assignWorker() {
		Worker1 worker = type1FreeWorkers.dequeue();
		return worker;
	}

	public void freeWorker(Worker1 worker) {
		if (type1FreeWorkers.contains(worker)) {
			throw new AssertionError("Attempted to free an already free worker: " + worker);
		}
		worker.setState(Worker1.State.Idle);
		worker.setVehicle(null);
		type1FreeWorkers.enqueue(worker);
		findWork();
	}

	public FreeCapacity getLot1FreeParkingSpots() {
		return lot1FreeParkingSpots;
	}

	public void setLot1FreeParkingSpots(FreeCapacity lot1FreeParkingSpots) {
		this.lot1FreeParkingSpots = lot1FreeParkingSpots;
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
		return hasCarsToReturn() || (hasOrdersToTake() && lot1FreeParkingSpots.getFreeUnits() > 0);
	}

	public void setDecider(Worker1Decider decider) {
		decider.setAgent(this);
		this.decider = decider;
	}
}