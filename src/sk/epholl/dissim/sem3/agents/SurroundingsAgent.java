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

	private StatisticCounter totalTimeInSystemCounter = new StatisticCounter();
	private StatisticCounter timeCustomerWaitingForRepairCounter = new StatisticCounter();
	private StatisticCounter timeWaitingforOrderCounter = new StatisticCounter();
	private StatisticCounter timeWaitingForRepairCounter = new StatisticCounter();
	private StatisticCounter timeWaitiingForReturnCounter = new StatisticCounter();

	private StatisticCounter balanceCounter = new StatisticCounter();
	private StatisticCounter profitCounter = new StatisticCounter();

	private StatisticCounter averageRepairTimeCounter = new StatisticCounter();
	private StatisticCounter revenuePerCustomerCounter = new StatisticCounter();

	private StatisticCounter balancePerFinishedCustomerCounter = new StatisticCounter();
	private StatisticCounter balancePerAllCustomersCounter = new StatisticCounter();

	private int enteredVehicles;
	private int finishedVehicles;
	private int refusedVehicles;
	private int shopClosedVehicles;

	private double revenue;
	private double balance;
	private double repairTime;

	private StatisticCounter totalTimeInSystem = new StatisticCounter();
	private StatisticCounter timeCustomerWaitingForRepair = new StatisticCounter();
	private StatisticCounter timeWaitingForRepair = new StatisticCounter();
	private StatisticCounter timeWaitingForOrder = new StatisticCounter();
	private StatisticCounter timeWaitingForReturn = new StatisticCounter();


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
		revenue = 0;
		balance = -getParams().getTotalPrice();
		repairTime = 0;

		totalTimeInSystem.clear();
		timeCustomerWaitingForRepair.clear();
		timeWaitingForRepair.clear();
		timeWaitingForOrder.clear();
		timeWaitingForReturn.clear();

		publishValueContinous(Rst.ENTERED_CUSTOMERS, enteredVehicles);
		publishValueContinous(Rst.REFUSED_CUSTOMERS, refusedVehicles);
		publishValueContinous(Rst.SHOP_CLOSED_CUSTOMERS, shopClosedVehicles);
		publishValueContinous(Rst.FINISHED_CUSTOMERS, finishedVehicles);
		publishValueContinous(Rst.PROFIT, revenue);
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
			revenue += vehicleProfit;
			balance += vehicleProfit;
			repairTime += vehicle.getRepairDuratioinInMinutes();
			publishValueContinous(Rst.PROFIT, revenue);
			publishValueContinous(Rst.BALANCE, balance);
			totalTimeInSystem.addValue(vehicle.getTotalTimeInSystem());
			timeCustomerWaitingForRepair.addValue(vehicle.getTimeCustomerWaitForRepair());
			timeWaitingForOrder.addValue(vehicle.getTimeWaitForOrder());
			timeWaitingForRepair.addValue(vehicle.getTimeWaitForRepair());
			timeWaitingForReturn.addValue(vehicle.getTimeWaitForLot2Spot() + vehicle.getTimeWaitForReturn());
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

		totalTimeInSystemCounter.addValue(totalTimeInSystem.getMean());
		timeCustomerWaitingForRepairCounter.addValue(timeCustomerWaitingForRepair.getMean());
		timeWaitingforOrderCounter.addValue(timeWaitingForOrder.getMean());
		timeWaitingForRepairCounter.addValue(timeWaitingForRepair.getMean());
		timeWaitiingForReturnCounter.addValue(timeWaitingForReturn.getMean());

		profitCounter.addValue(revenue);
		balanceCounter.addValue(balance);

		averageRepairTimeCounter.addValue(repairTime / finishedVehicles);
		revenuePerCustomerCounter.addValue(revenue / finishedVehicles);

		balancePerAllCustomersCounter.addValue(balance / (finishedVehicles + refusedVehicles + shopClosedVehicles));
		balancePerFinishedCustomerCounter.addValue(balance / finishedVehicles);

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

		publishValueIfAfterWarmup(Rst.R_CUSTOMER_TOTAL_TIME,
				new Rst.Result(rep(), totalTimeInSystemCounter));
		publishValueIfAfterWarmup(Rst.R_CUSTOMER_WAIT_FOR_REPAIR_TIME,
				new Rst.Result(rep(), timeCustomerWaitingForRepairCounter));
		publishValueIfAfterWarmup(Rst.R_CUSTOMER_ORDER_WAIT_TIME,
				new Rst.Result(rep(), timeWaitingforOrderCounter));
		publishValueIfAfterWarmup(Rst.R_CUSTOMER_REPAIR_WAIT_TIME,
				new Rst.Result(rep(), timeWaitingForRepairCounter));
		publishValueIfAfterWarmup(Rst.R_CUSTOMER_RETURN_WAIT_TIME,
				new Rst.Result(rep(), timeWaitiingForReturnCounter));

		publishValueIfAfterWarmup(Rst.R_MONEY_BALANCE,
				new Rst.Result(rep(), balanceCounter));
		publishValueIfAfterWarmup(Rst.R_MONEY_EARNED,
				new Rst.Result(rep(), profitCounter));

		publishValueIfAfterWarmup(Rst.R_AVERAGE_REPAIR_TIME,
				new Rst.Result(rep(), averageRepairTimeCounter));
		publishValueIfAfterWarmup(Rst.R_REVENUE_PER_FINISHED_CUSTOMERS,
				new Rst.Result(rep(), revenuePerCustomerCounter));

		publishValueIfAfterWarmup(Rst.R_MONEY_PER_ALL_CUSTOMERS,
				new Rst.Result(rep(), balancePerAllCustomersCounter));
		publishValueIfAfterWarmup(Rst.R_MONEY_PER_FINISHED_CUSTOMERS,
				new Rst.Result(rep(), balancePerFinishedCustomerCounter));
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