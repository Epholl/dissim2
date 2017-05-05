package sk.epholl.dissim.sem3.simulation;

import OSPABA.*;
import sk.epholl.dissim.sem3.agents.*;

public class MySimulation extends Simulation {
	public MySimulation() {
		init();
	}

	@Override
	public void prepareSimulation() {
		super.prepareSimulation();
		// Create global statistcis
	}

	@Override
	public void prepareReplication() {
		super.prepareReplication();
		// Reset entities, queues, local statistics, etc...
	}

	@Override
	public void replicationFinished() {
		// Collect local statistics into global, update UI, etc...
		super.replicationFinished();
	}

	@Override
	public void simulationFinished() {
		// Dysplay simulation results
		super.simulationFinished();
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	private void init() {
		setCarShopModelAgent(new CarShopModelAgent(Id.carShopModelAgent, this, null));
		setSurroundingsAgent(new SurroundingsAgent(Id.surroundingsAgent, this, carShopModelAgent()));
		setTransportationAgent(new TransportationAgent(Id.transportationAgent, this, carShopModelAgent()));
		setOfficeAgent(new OfficeAgent(Id.officeAgent, this, carShopModelAgent()));
		setRepairAgent(new RepairAgent(Id.repairAgent, this, carShopModelAgent()));
		setParkingAgent(new ParkingAgent(Id.parkingAgent, this, carShopModelAgent()));
	}

	private CarShopModelAgent _carShopModelAgent;

public CarShopModelAgent carShopModelAgent()
	{ return _carShopModelAgent; }

	public void setCarShopModelAgent(CarShopModelAgent carShopModelAgent)
	{_carShopModelAgent = carShopModelAgent; }

	private SurroundingsAgent _surroundingsAgent;

public SurroundingsAgent surroundingsAgent()
	{ return _surroundingsAgent; }

	public void setSurroundingsAgent(SurroundingsAgent surroundingsAgent)
	{_surroundingsAgent = surroundingsAgent; }

	private TransportationAgent _transportationAgent;

public TransportationAgent transportationAgent()
	{ return _transportationAgent; }

	public void setTransportationAgent(TransportationAgent transportationAgent)
	{_transportationAgent = transportationAgent; }

	private OfficeAgent _officeAgent;

public OfficeAgent officeAgent()
	{ return _officeAgent; }

	public void setOfficeAgent(OfficeAgent officeAgent)
	{_officeAgent = officeAgent; }

	private RepairAgent _repairAgent;

public RepairAgent repairAgent()
	{ return _repairAgent; }

	public void setRepairAgent(RepairAgent repairAgent)
	{_repairAgent = repairAgent; }

	private ParkingAgent _parkingAgent;

public ParkingAgent parkingAgent()
	{ return _parkingAgent; }

	public void setParkingAgent(ParkingAgent parkingAgent)
	{_parkingAgent = parkingAgent; }
	//meta! tag="end"
}