package sk.epholl.dissim.sem3.continualAssistants;

import OSPABA.*;
import OSPABA.Process;
import sk.epholl.dissim.sem3.agents.CarShopModelAgent;
import sk.epholl.dissim.sem3.simulation.Mc;

//meta! id="125"
public class DayEndProcess extends Process {
	public DayEndProcess(int id, Simulation mySim, CommonAgent myAgent) {
		super(id, mySim, myAgent);
	}

	@Override
	public void prepareReplication() {
		super.prepareReplication();
		// Setup component for the next replication
	}

	//meta! sender="CarShopModelAgent", id="126", type="Start"
	public void processStart(MessageForm message) {
		double duration = 8 * 60 * 60;
		message.setCode(Mc.finish);
		hold(duration, message);
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message) {
		switch (message.code()) {
			case Mc.finish:
				MessageForm copy = message.createCopy();
				processStart(message);
				assistantFinished(copy);
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
	public CarShopModelAgent myAgent() {
		return (CarShopModelAgent)super.myAgent();
	}

}