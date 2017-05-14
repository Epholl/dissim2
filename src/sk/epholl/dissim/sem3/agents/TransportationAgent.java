package sk.epholl.dissim.sem3.agents;

import OSPABA.*;
import sk.epholl.dissim.sem3.continualAssistants.MoveCarProcess;
import sk.epholl.dissim.sem3.entity.RoadModel;
import sk.epholl.dissim.sem3.managers.TransportationManager;
import sk.epholl.dissim.sem3.simulation.Id;
import sk.epholl.dissim.sem3.simulation.Mc;
import sk.epholl.dissim.sem3.simulation.MySimulation;

//meta! id="86"
public class TransportationAgent extends BaseAgent {

	private RoadModel roadModel;

	public TransportationAgent(int id, MySimulation mySim, Agent parent) {
		super(id, mySim, parent);
		init();
	}

	@Override
	public void prepareReplication() {
		super.prepareReplication();
		// Setup component for the next replication
		roadModel = getSimulation().getParameters().getRoadModel();
	}

	public RoadModel getRoadModel() {
		return roadModel;
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	private void init() {
		new TransportationManager(Id.transportationManager, mySim(), this);
		new MoveCarProcess(Id.moveCarProcess, mySim(), this);
		addOwnMessage(Mc.init);
		addOwnMessage(Mc.transferVehicle);
	}
	//meta! tag="end"
}