package sk.epholl.dissim.sem3.continualAssistants;

import OSPABA.*;
import OSPABA.Process;
import sk.epholl.dissim.generator.RandomGenerator;
import sk.epholl.dissim.sem3.agents.RepairAgent;
import sk.epholl.dissim.sem3.entity.Vehicle;
import sk.epholl.dissim.sem3.simulation.Mc;
import sk.epholl.dissim.sem3.simulation.MyMessage;
import sk.epholl.dissim.sem3.simulation.Rst;

//meta! id="117"
public class RepairCarProcess extends Process {

	private RandomGenerator<Integer> amountOfRepairsGenerator;

	private RandomGenerator<Integer> repairDurationGenerator;

	public RepairCarProcess(int id, Simulation mySim, CommonAgent myAgent) {
		super(id, mySim, myAgent);
	}

	@Override
	public void prepareReplication() {
		super.prepareReplication();
		// Setup component for the next replication
		amountOfRepairsGenerator = myAgent().getParams().getGenerators().getAmountOfRepairsGenerator();
		repairDurationGenerator = myAgent().getParams().getGenerators().getRepairDurationMinutesGenerator();
	}

	//meta! sender="RepairAgent", id="118", type="Start"
	public void processStart(MessageForm message) {
		MyMessage msg = (MyMessage) message;
		Vehicle vehicle = msg.getVehicle();
		int repairsInMinutes = 0;
		int repairsCount = amountOfRepairsGenerator.nextValue();
		for (int i = 0; i < repairsCount; i++) {
			repairsInMinutes += repairDurationGenerator.nextValue();
		}
		vehicle.setRepairDuratioinInMinutes(repairsInMinutes);
		vehicle.addFinsihedState(Vehicle.State.StartRepairing);
		myAgent().publishValueContinous(Rst.CONSOLE_LOG, "Starting " + repairsCount + " repairs for " + repairsInMinutes + " minutes for " + vehicle);
		double duration = repairsInMinutes * 60;
		vehicle.setCurrentState(Vehicle.State.Repair, myAgent().mySim().currentTime() + duration);
		msg.setCode(Mc.finish);
		hold(duration, msg);
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message) {
		MyMessage msg = (MyMessage) message;
		Vehicle vehicle = msg.getVehicle();
		vehicle.persistCurrentState();
		myAgent().publishValueContinous(Rst.CONSOLE_LOG, "Repair finished for " + vehicle);
		switch (message.code()) {
			case Mc.finish:
				assistantFinished(message);
				break;
		}
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	@Override
	public void processMessage(MessageForm message) {
		switch (message.code()) {
		case Mc.start:
			processStart(message);
		break;

		default:
			processDefault(message);
		break;
		}
	}
	//meta! tag="end"

	@Override
	public RepairAgent myAgent() {
		return (RepairAgent)super.myAgent();
	}

}