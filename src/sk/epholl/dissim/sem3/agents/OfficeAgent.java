package sk.epholl.dissim.sem3.agents;

import OSPABA.*;
import sk.epholl.dissim.entity.Vehicle;
import sk.epholl.dissim.sem3.continualAssistants.CancelCustomerProcess;
import sk.epholl.dissim.sem3.continualAssistants.ReturnCarProcess;
import sk.epholl.dissim.sem3.continualAssistants.TakeOrderProcess;
import sk.epholl.dissim.sem3.entity.Worker1;
import sk.epholl.dissim.sem3.managers.OfficeManager;
import sk.epholl.dissim.sem3.simulation.Const;
import sk.epholl.dissim.sem3.simulation.Id;
import sk.epholl.dissim.sem3.simulation.Mc;
import sk.epholl.dissim.sem3.simulation.MySimulation;
import sk.epholl.dissim.util.StatisticQueue;

import java.util.Arrays;
import java.util.LinkedList;

//meta! id="87"
public class OfficeAgent extends BaseAgent {

	private LinkedList<Worker1> type1FreeWorkers;

	private Worker1[] type1Workers;

	private int lot1FreeParkingSpots;

	private StatisticQueue<Vehicle> vehiclesWaitingForOrder;

	private StatisticQueue<Vehicle> vehiclesOrdering;

	public OfficeAgent(int id, MySimulation mySim, Agent parent) {
		super(id, mySim, parent);
		init();

		type1FreeWorkers = new LinkedList<>();
		vehiclesWaitingForOrder = new StatisticQueue<>(getSimulation());
		vehiclesOrdering = new StatisticQueue<>(getSimulation());
	}

	@Override
	public void prepareReplication() {
		super.prepareReplication();
		// Setup component for the next replication

		final int type1Count = getParams().getType1WorkerCount();
		type1Workers = new Worker1[type1Count];
		lot1FreeParkingSpots = Const.parkingLot1Capacity;
		for (int i = 0; i < type1Workers.length; i++) {
			type1Workers[i] = new Worker1();
		}
		type1FreeWorkers.clear();
		type1FreeWorkers.addAll(Arrays.asList(type1Workers));
		vehiclesWaitingForOrder.clear();
		vehiclesOrdering.clear();

	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	private void init() {
		new OfficeManager(Id.officeManager, mySim(), this);
		new CancelCustomerProcess(Id.cancelCustomerProcess, mySim(), this);
		new TakeOrderProcess(Id.takeOrderProcess, mySim(), this);
		new ReturnCarProcess(Id.returnCarProcess, mySim(), this);
		addOwnMessage(Mc.returnCar);
		addOwnMessage(Mc.init);
		addOwnMessage(Mc.reserveSpot);
		addOwnMessage(Mc.parkingSpotsUpdate);
		addOwnMessage(Mc.takeOrder);
	}
	//meta! tag="end"


	public int getLot1FreeParkingSpots() {
		return lot1FreeParkingSpots;
	}

	public void setLot1FreeParkingSpots(int lot1FreeParkingSpots) {
		this.lot1FreeParkingSpots = lot1FreeParkingSpots;
	}
}