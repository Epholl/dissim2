package sk.epholl.dissim.sem3.managers;

import OSPABA.*;
import sk.epholl.dissim.sem3.entity.Vehicle;
import sk.epholl.dissim.sem3.agents.SurroundingsAgent;
import sk.epholl.dissim.sem3.simulation.Id;
import sk.epholl.dissim.sem3.simulation.Mc;
import sk.epholl.dissim.sem3.simulation.MyMessage;
import sk.epholl.dissim.sem3.simulation.Rst;
import sk.epholl.dissim.util.Pair;
import sk.epholl.dissim.util.TimeUtils;

import java.util.List;

//meta! id="85"
public class SurroundingsManager extends Manager {
	public SurroundingsManager(int id, Simulation mySim, Agent myAgent) {
		super(id, mySim, myAgent);
		init();
	}

	@Override
	public void prepareReplication() {
		super.prepareReplication();
		// Setup component for the next replication

		if (petriNet() != null) {
			petriNet().clear();
		}
	}

	//meta! sender="NewCustomerScheduler", id="110", type="Finish"
	public void processFinish(MessageForm message) {
		MyMessage msg = (MyMessage) message;
		myAgent().publishValueContinous(Rst.CONSOLE_LOG, "New customer entry: " + msg.getVehicle());
		msg.setCode(Mc.customerEntry);
		msg.setAddressee(mySim().findAgent(Id.modelAgent));
		notice(msg);
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message) {
		switch (message.code()) {
		}
	}

	//meta! sender="ModelAgent", id="135", type="Notice"
	public void processCustomerExit(MessageForm message) {
		MyMessage msg = (MyMessage) message;
		myAgent().publishValueContinous(Rst.CONSOLE_LOG, "Customer exit: " + msg.getVehicle());
		List<Pair<Double, Vehicle.State>> history = msg.getVehicle().getHistory();
		System.out.println("\n" + msg.getVehicle());
		for (Pair<Double, Vehicle.State> state: history) {
			System.out.println(TimeUtils.formatDayTime(state.first) + ": " + state.second);
		}
	}

	//meta! sender="ModelAgent", id="139", type="Notice"
	public void processInit(MessageForm message) {
		myAgent().publishValueContinous(Rst.CONSOLE_LOG, "Init message received in SurroundingsAgent");
		message.setCode(Mc.start);
		message.setAddressee(myAgent().findAssistant(Id.newCustomerScheduler));
		notice(message);
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	public void init() {
	}

	@Override
	public void processMessage(MessageForm message) {
		switch (message.code()) {
		case Mc.init:
			processInit(message);
		break;

		case Mc.finish:
			processFinish(message);
		break;

		case Mc.customerExit:
			processCustomerExit(message);
		break;

		default:
			processDefault(message);
		break;
		}
	}
	//meta! tag="end"

	@Override
	public SurroundingsAgent myAgent() {
		return (SurroundingsAgent)super.myAgent();
	}

}