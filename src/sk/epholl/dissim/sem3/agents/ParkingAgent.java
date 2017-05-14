package sk.epholl.dissim.sem3.agents;

import OSPABA.*;
import sk.epholl.dissim.sem3.entity.ParkingLot;
import sk.epholl.dissim.sem3.entity.ParkingSpot;
import sk.epholl.dissim.sem3.entity.Place;
import sk.epholl.dissim.sem3.managers.ParkingManager;
import sk.epholl.dissim.sem3.simulation.*;
import sk.epholl.dissim.sem3.entity.Vehicle;

import java.util.HashSet;

//meta! id="92"
public class ParkingAgent extends BaseAgent {

	private HashSet<Vehicle> mainLot;

	private ParkingLot lot1;

	private ParkingLot lot2;

	public ParkingAgent(int id, MySimulation mySim, Agent parent) {
		super(id, mySim, parent);
		init();

		mainLot = new HashSet<>();

		lot1 = new ParkingLot(Const.parkingLot1Capacity);

		lot2 = new ParkingLot(Const.parkingLot2Capacity);
	}

	@Override
	public void prepareReplication() {
		super.prepareReplication();
		// Setup component for the next replication
		mainLot.clear();
		lot1.clear();
		lot2.clear();
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	private void init() {
		new ParkingManager(Id.parkingManager, mySim(), this);
		addOwnMessage(Mc.init);
		addOwnMessage(Mc.reserveSpot);
		addOwnMessage(Mc.parkCar);
	}
	//meta! tag="end"

	public void reserveSpot(final Vehicle vehicle, final Place parkingLot) {
		if (parkingLot == Place.ParkingLot1) {
			lot1.reserve(vehicle);
		} else if (parkingLot == Place.ParkingLot2) {
			lot2.reserve(vehicle);
		}
	}

	public void parkCar(final Vehicle vehicle, final Place parkingLot) {
		if (parkingLot == Place.MainLot) {
			mainLot.add(vehicle);
		} else if (parkingLot == Place.ParkingLot1) {
			ParkingSpot assigned = vehicle.getAssignedParkingSpot();
			assigned.setState(ParkingSpot.State.Occupied);
		} else if (parkingLot == Place.ParkingLot2) {
			ParkingSpot assigned = vehicle.getAssignedParkingSpot();
			assigned.setState(ParkingSpot.State.Occupied);
		}
	}

	public void freeSpot(final Vehicle vehicle, final Place parkingLot) {
		if (parkingLot == Place.MainLot) {
			mainLot.remove(vehicle);
		} else if (parkingLot == Place.ParkingLot1) {
			vehicle.setAssignedParkingSpot(null);
			lot1.free(vehicle);
		} else if (parkingLot == Place.ParkingLot2) {
			vehicle.setAssignedParkingSpot(null);
			lot2.free(vehicle);
		}
	}

	public void initialCapacityNotices() {
        noticeParkingSpaceFreed(Place.ParkingLot1, lot1.getFreeSpotsCount());
        noticeParkingSpaceFreed(Place.ParkingLot2, lot2.getFreeSpotsCount());
    }

	private void noticeParkingSpaceFreed(final Place lot, final int freeSpots) {
        MyMessage message = new MyMessage(mySim());
        message.setCode(Mc.parkingSpotsUpdate);
        message.setPlace(lot);
        message.setVariable(freeSpots);
        message.setAddressee(Id.carShopModelAgent);
        manager().notice(message);
    }
}