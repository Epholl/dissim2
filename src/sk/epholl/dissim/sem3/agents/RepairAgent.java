package sk.epholl.dissim.sem3.agents;

import OSPABA.*;
import sk.epholl.dissim.sem3.continualAssistants.RepairCarProcess;
import sk.epholl.dissim.sem3.entity.FreeCapacity;
import sk.epholl.dissim.sem3.entity.Place;
import sk.epholl.dissim.sem3.entity.Worker2;
import sk.epholl.dissim.sem3.entity.Vehicle;
import sk.epholl.dissim.sem3.managers.RepairManager;
import sk.epholl.dissim.sem3.simulation.Id;
import sk.epholl.dissim.sem3.simulation.Mc;
import sk.epholl.dissim.sem3.simulation.MyMessage;
import sk.epholl.dissim.sem3.simulation.MySimulation;
import sk.epholl.dissim.util.StatisticQueue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;

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
			type2Workers[i] = new Worker2(worker2IdCounter++);
		}
		type2FreeWorkers.clear();
		type2FreeWorkers.addAll(Arrays.asList(type2Workers));
		vehiclesWaitingOnParkingLot.clear();
		vehiclesRepairing.clear();
		vehiclesRepaired.clear();
	}

	public FreeCapacity getLot2FreeParkingSpots() {
		return lot2FreeParkingSpots;
	}

	public void setLot2FreeParkingSpots(FreeCapacity lot2FreeParkingSpots) {
		this.lot2FreeParkingSpots = lot2FreeParkingSpots;
	}

	public void newVehicleArrived(MyMessage message) {

		vehiclesWaitingOnParkingLot.enqueue(message);
		findWork();
	}

	public void findWork() {
		if (canStartWork()) {
			MyMessage message = vehiclesWaitingOnParkingLot.dequeue();
			Worker2 worker = assignWorker();
			Vehicle vehicle = message.getVehicle();
			vehicle.setWorker2(worker);
			MyMessage copy = (MyMessage) message.createCopy();
			copy.setCode(Mc.freeSpot);
			copy.setPlace(Place.ParkingLot1);
			copy.setAddressee(Id.carShopModelAgent);
			manager().notice(copy);
			message.setAddressee(findAssistant(Id.repairCarProcess));
			manager().startContinualAssistant(message);
		}
	}

	public boolean canStartWork() {
		return !vehiclesWaitingOnParkingLot.isEmpty() && !type2FreeWorkers.isEmpty();
	}

	public Worker2 assignWorker() {
		Worker2 worker = type2FreeWorkers.removeLast();
		return worker;
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	private void init() {
		new RepairManager(Id.repairManager, mySim(), this);
		new RepairCarProcess(Id.repairCarProcess, mySim(), this);
		addOwnMessage(Mc.init);
		addOwnMessage(Mc.repairWehicle);
		addOwnMessage(Mc.parkingSpotsUpdate);
	}
	//meta! tag="end"
}