package sk.epholl.dissim.sem3.agents;

import OSPABA.*;
import sk.epholl.dissim.sem3.continualAssistants.RepairCarProcess;
import sk.epholl.dissim.sem3.entity.FreeCapacity;
import sk.epholl.dissim.sem3.entity.Place;
import sk.epholl.dissim.sem3.entity.Worker2;
import sk.epholl.dissim.sem3.entity.Vehicle;
import sk.epholl.dissim.sem3.managers.RepairManager;
import sk.epholl.dissim.sem3.simulation.*;
import sk.epholl.dissim.util.StatisticQueue;

import java.util.*;

//meta! id="88"
public class RepairAgent extends BaseAgent {

	private int worker2IdCounter = 0;

	private LinkedList<Worker2> type2FreeWorkers;

	private Worker2[] type2Workers;

	private StatisticQueue<MyMessage> vehiclesWaitingOnParkingLot;

	private HashSet<MyMessage> vehiclesRepairing;

	private StatisticQueue<MyMessage> vehiclesRepaired;

	private FreeCapacity lot2FreeParkingSpots;

	public RepairAgent(int id, MySimulation mySim, Agent parent) {
		super(id, mySim, parent);
		init();

		type2FreeWorkers = new LinkedList<>();
		vehiclesWaitingOnParkingLot = new StatisticQueue<>(getSimulation());
		vehiclesRepairing = new HashSet<>();
		vehiclesRepaired = new StatisticQueue<>(getSimulation());
	}

	@Override
	public void prepareReplication() {
		super.prepareReplication();
		// Setup component for the next replication

		worker2IdCounter = 0;
		final int type2Count = getParams().getType2WorkerCount();
		type2Workers = new Worker2[type2Count];
		for (int i = 0; i < type2Count; i++) {
			type2Workers[i] = new Worker2(getSimulation(), worker2IdCounter++);
		}
		type2FreeWorkers.clear();
		type2FreeWorkers.addAll(Arrays.asList(type2Workers));
		vehiclesWaitingOnParkingLot.clear();
		vehiclesRepairing.clear();
		vehiclesRepaired.clear();
	}

	@Override
	public void onGuiUpdate() {
		super.onGuiUpdate();
		List<Rst.WorkerState> states = new ArrayList<>();
		for (Worker2 worker: type2Workers) {
			states.add(worker.getWorkerState());
		}
		Rst.WorkerUpdate update = new Rst.WorkerUpdate();
		update.states = states;
		publishValueContinous(Rst.WORKER2_STATE, update);
	}

	public FreeCapacity getLot2FreeParkingSpots() {
		return lot2FreeParkingSpots;
	}

	public void setLot2FreeParkingSpots(FreeCapacity lot2FreeParkingSpots) {
		this.lot2FreeParkingSpots = lot2FreeParkingSpots;
		findWork();
	}

	public void newVehicleArrived(MyMessage message) {

		vehiclesWaitingOnParkingLot.enqueue(message);
		Vehicle vehicle = ((MyMessage)message).getVehicle();
		vehicle.setCurrentState(Vehicle.State.WaitForRepair);
		findWork();
	}

	public void repairFinished(MyMessage message) {
		vehiclesRepairing.remove(message);
		final Vehicle vehicle = message.getVehicle();
		vehicle.setCurrentState(Vehicle.State.WaitingForLot2Spot);
		vehicle.getWorker2().setState(Worker2.State.WaitingToReturnCar);
		vehiclesRepaired.enqueue(message);
		findWork();
	}

	public void carReparked(MyMessage message) {
		Vehicle vehicle = message.getVehicle();
		Worker2 worker = vehicle.getWorker2();
		freeWorker(worker);
		vehicle.setWorker2(null);
		vehicle.persistCurrentState();
		vehicle.setCurrentState(Vehicle.State.WaitingOnLot2);
		findWork();
		message.setCode(Mc.repairWehicle);
		manager().response(message);
	}

	public void findWork() {
		if (canStartWork()) {
			MyMessage message = vehiclesWaitingOnParkingLot.dequeue();
			vehiclesRepairing.add(message);
			Worker2 worker = assignWorker();
			Vehicle vehicle = message.getVehicle();
			worker.setState(Worker2.State.Repairing);
			worker.setVehicle(vehicle);
			vehicle.persistCurrentState();
			vehicle.setWorker2(worker);
			MyMessage copy = (MyMessage) message.createCopy();
			copy.setCode(Mc.freeSpot);
			copy.setPlace(Place.ParkingLot1);
			copy.setAddressee(Id.carShopModelAgent);
			vehicle.setCurrentPlace(Place.RepairShop);
			manager().notice(copy);
			message.setAddressee(findAssistant(Id.repairCarProcess));
			manager().startContinualAssistant(message);
		}
		if (canReparkCar()) {
			MyMessage message = vehiclesRepaired.dequeue();
			message.setCode(Mc.parkCar);
			message.setPlace(Place.ParkingLot2);
			message.setAddressee(Id.carShopModelAgent);
			manager().request(message);
		}
	}

	public boolean canStartWork() {
		return !vehiclesWaitingOnParkingLot.isEmpty() && !type2FreeWorkers.isEmpty();
	}

	public boolean canReparkCar() {
		return lot2FreeParkingSpots.getFreeUnits() > 0 && !vehiclesRepaired.isEmpty();
	}

	public Worker2 assignWorker() {
		Worker2 worker = type2FreeWorkers.removeLast();
		return worker;
	}

	public void freeWorker(Worker2 worker) {
		type2FreeWorkers.add(worker);
		worker.setState(Worker2.State.Idle);
		worker.setVehicle(null);
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	private void init() {
		new RepairManager(Id.repairManager, mySim(), this);
		new RepairCarProcess(Id.repairCarProcess, mySim(), this);
		addOwnMessage(Mc.init);
		addOwnMessage(Mc.repairWehicle);
		addOwnMessage(Mc.parkCar);
		addOwnMessage(Mc.parkingSpotsUpdate);
	}
	//meta! tag="end"
}