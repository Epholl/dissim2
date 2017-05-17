package sk.epholl.dissim.sem3.continualAssistants;

import OSPABA.*;
import OSPABA.Process;
import sk.epholl.dissim.sem3.agents.OfficeAgent;
import sk.epholl.dissim.sem3.simulation.Const;
import sk.epholl.dissim.sem3.simulation.Mc;

//meta! id="128"
public class CancelCustomerProcess extends Process {
	public CancelCustomerProcess(int id, Simulation mySim, CommonAgent myAgent) {
		super(id, mySim, myAgent);
	}

	@Override
	public void prepareReplication() {
		super.prepareReplication();
		// Setup component for the next replication
	}

	//meta! sender="OfficeAgent", id="129", type="Start"
	public void processStart(MessageForm message) {
		double duration = Const.customerWaitForOrderTimeout;
		message.setCode(Mc.finish);
		hold(duration, message);
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message) {
		switch (message.code()) {
			case Mc.finish:
				assistantFinished(message);
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
	public OfficeAgent myAgent() {
		return (OfficeAgent)super.myAgent();
	}

}