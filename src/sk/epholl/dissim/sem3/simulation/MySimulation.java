package sk.epholl.dissim.sem3.simulation;

import OSPABA.*;
import sk.epholl.dissim.sem3.agents.*;
import sk.epholl.dissim.util.subscribers.ResultManager;

public class MySimulation extends Simulation {

	private ResultManager resultManager;

	private boolean isContinuous = false;
	private double updateInterval;
	private double updateDuration;

	private SimulationParameters parameters;

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
		if (isContinuous) {
			super.setSimSpeed(updateInterval, updateDuration);
		}

		modelAgent().startSimulation();
	}

	@Override
	public void replicationFinished() {
		// Collect local statistics into global, update UI, etc...
		resultManager.addValue(Rst.REPLICATION_COUNT, currentReplication());
		resultManager.addValue(Rst.CONSOLE_LOG, "Replication finished: " + currentReplication());
		super.replicationFinished();
	}

	@Override
	public void simulationFinished() {
		// Dysplay simulation results

		resultManager.addValue(Rst.CONSOLE_LOG, "Simulation finished.");
		super.simulationFinished();
	}

	@Override
	public void setSimSpeed(double interval, double duration) {
		super.setSimSpeed(interval, duration);
		this.updateInterval = interval;
		this.updateDuration = duration;
		resultManager.addValue(Rst.CONSOLE_LOG, "Sim speed updated: " + interval + ", " + duration);
		isContinuous = true;
	}

	@Override
	public void setMaxSimSpeed() {
		isContinuous = false;
		resultManager.addValue(Rst.CONSOLE_LOG, "Sim speed set to max");
		super.setMaxSimSpeed();
	}

	public void setResultManager(final ResultManager resultManager) {
		this.resultManager = resultManager;
	}

	public ResultManager getResultManager() {
		return resultManager;
	}

	public void execIfContinous(final Runnable r) {
		if (isContinuous) {
			r.run();
		}
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	private void init() {
		setModelAgent(new ModelAgent(Id.modelAgent, this, null));
		setCarShopModelAgent(new CarShopModelAgent(Id.carShopModelAgent, this, modelAgent()));
		setSurroundingsAgent(new SurroundingsAgent(Id.surroundingsAgent, this, modelAgent()));
		setTransportationAgent(new TransportationAgent(Id.transportationAgent, this, carShopModelAgent()));
		setOfficeAgent(new OfficeAgent(Id.officeAgent, this, carShopModelAgent()));
		setRepairAgent(new RepairAgent(Id.repairAgent, this, carShopModelAgent()));
		setParkingAgent(new ParkingAgent(Id.parkingAgent, this, carShopModelAgent()));
	}

	private ModelAgent _modelAgent;

public ModelAgent modelAgent()
	{ return _modelAgent; }

	public void setModelAgent(ModelAgent modelAgent)
	{_modelAgent = modelAgent; }

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

	public void setParameters(SimulationParameters parameters) {
		this.parameters = parameters;
	}

	public SimulationParameters getParameters() {
		return parameters;
	}
}