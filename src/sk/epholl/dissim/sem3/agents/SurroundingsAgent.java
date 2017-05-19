package sk.epholl.dissim.sem3.agents;

import OSPABA.*;
import sk.epholl.dissim.sem3.continualAssistants.NewCustomerScheduler;
import sk.epholl.dissim.sem3.managers.SurroundingsManager;
import sk.epholl.dissim.sem3.entity.Vehicle;
import sk.epholl.dissim.sem3.simulation.*;
import sk.epholl.dissim.util.StatisticCounter;

import java.util.ArrayList;
import java.util.List;

//meta! id="85"
public class SurroundingsAgent extends BaseAgent {

	private List<Vehicle> vehicles;

	private StatisticCounter totalCustomersCounter = new StatisticCounter();
	private StatisticCounter finishedCustomersCounter = new StatisticCounter();
	private StatisticCounter refusedCustomersCounter = new StatisticCounter();

	private StatisticCounter finishedCustomersRatioCounter = new StatisticCounter();
	private StatisticCounter refusedCustomersRatioCounter = new StatisticCounter();


	private int enteredVehicles;
	private int finishedVehicles;
	private int refusedVehicles;
	private int shopClosedVehicles;

	private double profit;
	private double balance;

	public SurroundingsAgent(int id, MySimulation mySim, Agent parent) {
		super(id, mySim, parent);
		init();
		addOwnMessage(Mc.customerEntry);
		vehicles = new ArrayList<>();
	}

	@Override
	public void prepareReplication() {
		super.prepareReplication();
		// Setup component for the next replication
		vehicles.clear();
		enteredVehicles = 0;
		finishedVehicles = 0;
		refusedVehicles = 0;
		shopClosedVehicles = 0;
		profit = 0;
		balance = -getParams().getTotalPrice();
		publishValueContinous(Rst.ENTERED_CUSTOMERS, enteredVehicles);
		publishValueContinous(Rst.REFUSED_CUSTOMERS, refusedVehicles);
		publishValueContinous(Rst.SHOP_CLOSED_CUSTOMERS, shopClosedVehicles);
		publishValueContinous(Rst.FINISHED_CUSTOMERS, finishedVehicles);
		publishValueContinous(Rst.PROFIT, profit);
		publishValueContinous(Rst.BALANCE, balance);
	}

	public void onVehicleArrived(Vehicle vehicle) {
		enteredVehicles++;
		publishValueContinous(Rst.ENTERED_CUSTOMERS, enteredVehicles);
		vehicles.add(vehicle);
	}

	public void onVehicleLeaving(Vehicle vehicle) {
		vehicles.remove(vehicle);
		if (vehicle.isOrderCancelled()) {
			refusedVehicles++;
			publishValueContinous(Rst.REFUSED_CUSTOMERS, refusedVehicles);
		} else if (vehicle.isShopClosed()) {
			shopClosedVehicles++;
			publishValueContinous(Rst.SHOP_CLOSED_CUSTOMERS, shopClosedVehicles);
		} else if (vehicle.isRepaired()) {
			finishedVehicles++;
			publishValueContinous(Rst.FINISHED_CUSTOMERS, finishedVehicles);
			double vehicleProfit = (vehicle.getRepairDuratioinInMinutes() / 60.0) * Const.repairShopHourlyRate;
			profit += vehicleProfit;
			balance += vehicleProfit;
			publishValueContinous(Rst.PROFIT, profit);
			publishValueContinous(Rst.BALANCE, balance);
		}
	}

	@Override
	public void onGuiUpdate() {
		super.onGuiUpdate();
		List<Rst.VehicleState> states = new ArrayList<>();
		for (Vehicle vehicle: this.vehicles) {
			states.add(vehicle.getVehicleState());
		}
		Rst.VehicleUpdate update = new Rst.VehicleUpdate();
		update.simTime = mySim().currentTime();
		update.states = states;

		publishValueContinous(Rst.VEHICLE_STATE, update);
	}

	@Override
	public void onReplicationFinished() {
		super.onReplicationFinished();
		totalCustomersCounter.addValue(finishedVehicles + refusedVehicles + shopClosedVehicles);
		finishedCustomersCounter.addValue(finishedVehicles);
		refusedCustomersCounter.addValue(refusedVehicles + shopClosedVehicles);
		finishedCustomersRatioCounter.addValue((double)finishedVehicles / (finishedVehicles + refusedVehicles + shopClosedVehicles));
		refusedCustomersRatioCounter.addValue((double)(refusedVehicles+shopClosedVehicles) / (finishedVehicles + refusedVehicles + shopClosedVehicles));
		publishValueIfAfterWarmup(Rst.R_ALL_CUSTOMERS,
				new Rst.Result(rep(), totalCustomersCounter));
		publishValueIfAfterWarmup(Rst.R_FINISHED_CUSTOMERS,
				new Rst.Result(rep(), finishedCustomersCounter));
		publishValueIfAfterWarmup(Rst.R_REFUSED_CUSTOMERS,
				new Rst.Result(rep(), refusedCustomersCounter));
		publishValueIfAfterWarmup(Rst.R_FINISHED_RATIO,
				new Rst.Result(rep(), finishedCustomersRatioCounter));
		publishValueIfAfterWarmup(Rst.R_REFUSED_RATIO,
				new Rst.Result(rep(), refusedCustomersRatioCounter));
	}



	//meta! userInfo="Generated code: do not modify", tag="begin"
	private void init() {
		new SurroundingsManager(Id.surroundingsManager, mySim(), this);
		new NewCustomerScheduler(Id.newCustomerScheduler, mySim(), this);
		addOwnMessage(Mc.init);
		addOwnMessage(Mc.customerExit);
	}
	//meta! tag="end"
}