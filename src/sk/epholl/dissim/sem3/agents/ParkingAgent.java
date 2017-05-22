package sk.epholl.dissim.sem3.agents;

import OSPABA.*;
import sk.epholl.dissim.sem3.entity.*;
import sk.epholl.dissim.sem3.managers.ParkingManager;
import sk.epholl.dissim.sem3.simulation.*;
import sk.epholl.dissim.sem3.simulation.Rst.ParkingSpotState;
import sk.epholl.dissim.util.StatisticCounter;
import sk.epholl.dissim.util.StatisticNumberCounter;

import java.util.HashSet;
import java.util.List;

//meta! id="92"
public class ParkingAgent extends BaseAgent {

    private StatisticCounter mainLotLoadCounter = new StatisticCounter();
    private StatisticCounter lot1LoadCounter = new StatisticCounter();
    private StatisticCounter lot2LoadCounter = new StatisticCounter();

    private StatisticCounter lot1LoadIndexCounter = new StatisticCounter();
    private StatisticCounter lot2LoadIndexCounter = new StatisticCounter();

	private HashSet<Vehicle> mainLot;

	private ParkingLot lot1;

	private ParkingLot lot2;

	private StatisticNumberCounter mainLotStatisticLoad = new StatisticNumberCounter(getSimulation());
    private StatisticNumberCounter lot1StatisticLoad = new StatisticNumberCounter(getSimulation());
    private StatisticNumberCounter lot2StatisticLoad = new StatisticNumberCounter(getSimulation());

	public ParkingAgent(int id, MySimulation mySim, Agent parent) {
		super(id, mySim, parent);
		init();

		mainLot = new HashSet<>();

		lot1 = new ParkingLot(getSimulation(), Const.parkingLot1Capacity, "Lot1");

		lot2 = new ParkingLot(getSimulation(), Const.parkingLot2Capacity, "Lot2");
	}

	@Override
	public void prepareReplication() {
		super.prepareReplication();
		// Setup component for the next replication
		mainLot.clear();
		lot1.clear();
		lot2.clear();
		mainLotStatisticLoad.clear();
		lot1StatisticLoad.clear();
		lot2StatisticLoad.clear();
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	private void init() {
		new ParkingManager(Id.parkingManager, mySim(), this);
		addOwnMessage(Mc.init);
		addOwnMessage(Mc.reserveSpot);
		addOwnMessage(Mc.freeSpot);
		addOwnMessage(Mc.parkCar);
	}
	//meta! tag="end"


    @Override
    public void onGuiUpdate() {
        super.onGuiUpdate();
        List<ParkingSpotState> states = lot1.getSpotsStatus();
        states.addAll(lot2.getSpotsStatus());
        Rst.ParkingUpdate update = new Rst.ParkingUpdate();
        update.spots = states;
        publishValueContinous(Rst.PARKING_STATE, update);
    }

    @Override
    public void onReplicationFinished() {
        super.onReplicationFinished();

        mainLotLoadCounter.addValue(mainLotStatisticLoad.getAverage());
        lot1LoadCounter.addValue(lot1StatisticLoad.getAverage());
        lot2LoadCounter.addValue(lot2StatisticLoad.getAverage());
        lot1LoadIndexCounter.addValue(lot1StatisticLoad.getAverage() / lot1.getCapacity());
        lot2LoadIndexCounter.addValue(lot2StatisticLoad.getAverage() / lot2.getCapacity());

        publishValueIfAfterWarmup(Rst.R_MAIN_LOT_VEHICLES,
                new Rst.Result(rep(), mainLotLoadCounter));
        publishValueIfAfterWarmup(Rst.R_LOT1_TAKEN_SPOTS,
                new Rst.Result(rep(), lot1LoadCounter));
        publishValueIfAfterWarmup(Rst.R_LOT2_TAKEN_SPOTS,
                new Rst.Result(rep(), lot2LoadCounter));
        publishValueIfAfterWarmup(Rst.R_LOT1_LOAD,
                new Rst.Result(rep(), lot1LoadIndexCounter));
        publishValueIfAfterWarmup(Rst.R_LOT2_LOAD,
                new Rst.Result(rep(), lot2LoadIndexCounter));
    }

    public void reserveSpot(MyMessage message) {
	    final Place parkingLot = message.getPlace();
	    final Vehicle vehicle = message.getVehicle();
		if (parkingLot == Place.ParkingLot1) {
			lot1.reserve(vehicle);
			noticeParkingSpotStatus(parkingLot, lot1.getFreeCapacity());
            lot1StatisticLoad.addValue(lot1.getFreeCapacity().getUsedUnits());

		} else if (parkingLot == Place.ParkingLot2) {
			lot2.reserve(vehicle);
            noticeParkingSpotStatus(parkingLot, lot2.getFreeCapacity());
            lot2StatisticLoad.addValue(lot2.getFreeCapacity().getUsedUnits());
        }
	}

	public void parkCar(final Vehicle vehicle, final Place parkingLot) {
        vehicle.setCurrentPlace(parkingLot);
		if (parkingLot == Place.MainLot) {
            mainLot.add(vehicle);
            mainLotStatisticLoad.addValue(mainLot.size());
        } else if (parkingLot == Place.ParkingLot1) {
			ParkingSpot assigned = vehicle.getAssignedParkingSpot();
			if (assigned == null) {
			    lot1.reserve(vehicle);
			    assigned = vehicle.getAssignedParkingSpot();
                noticeParkingSpotStatus(parkingLot, lot1.getFreeCapacity());
                lot1StatisticLoad.addValue(lot1.getFreeCapacity().getUsedUnits());
            }
			assigned.setState(ParkingSpot.State.Occupied);
		} else if (parkingLot == Place.ParkingLot2) {
			ParkingSpot assigned = vehicle.getAssignedParkingSpot();
            if (assigned == null) {
                lot2.reserve(vehicle);
                assigned = vehicle.getAssignedParkingSpot();
                noticeParkingSpotStatus(parkingLot, lot2.getFreeCapacity());
                lot2StatisticLoad.addValue(lot2.getFreeCapacity().getUsedUnits());
            }
			assigned.setState(ParkingSpot.State.Occupied);
		}
	}

	public void freeSpot(final Vehicle vehicle, final Place parkingLot) {
		if (parkingLot == Place.MainLot) {
            mainLotStatisticLoad.addValue(mainLot.size());
			mainLot.remove(vehicle);
		} else if (parkingLot == Place.ParkingLot1) {
			vehicle.setAssignedParkingSpot(null);
			lot1.free(vehicle);
            noticeParkingSpotStatus(Place.ParkingLot1, lot1.getFreeCapacity());
            lot1StatisticLoad.addValue(lot1.getFreeCapacity().getUsedUnits());
		} else if (parkingLot == Place.ParkingLot2) {
			vehicle.setAssignedParkingSpot(null);
			lot2.free(vehicle);
            noticeParkingSpotStatus(Place.ParkingLot2, lot2.getFreeCapacity());
            lot2StatisticLoad.addValue(lot2.getFreeCapacity().getUsedUnits());
		}
	}

	public void initialCapacityNotices() {
        noticeParkingSpotStatus(Place.ParkingLot1, lot1.getFreeCapacity());
        noticeParkingSpotStatus(Place.ParkingLot2, lot2.getFreeCapacity());
    }

	private void noticeParkingSpotStatus(final Place lot, final FreeCapacity freeSpots) {
        MyMessage message = new MyMessage(mySim());
        message.setCode(Mc.parkingSpotsUpdate);
        message.setPlace(lot);
        message.setCapacity(freeSpots);
        message.setAddressee(Id.carShopModelAgent);
        manager().notice(message);
    }
}