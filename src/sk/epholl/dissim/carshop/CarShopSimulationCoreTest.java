package sk.epholl.dissim.carshop;

import org.junit.Test;
import sk.epholl.dissim.core.SimulationCore;

/**
 * Created by Tomáš on 26.03.2017.
 */
public class CarShopSimulationCoreTest {

    @Test
    public void testRun() {
        CarShopSimulationParameters params = new CarShopSimulationParameters(100, 10d, 100, 100, 90*24);
        CarShopSimulationCore sim = new CarShopSimulationCore(params);
        //sim.setContinuousRun(true);
        //sim.setContinuousSpeed(60);
        sim.addListener(new SimulationCore.ResultListener<CarShopSimulation.IterationResults, CarShopSimulation.State>() {
            @Override
            public void onReplicationFinished(CarShopSimulation.IterationResults result) {
                System.out.println("IterationResults: " + result);
            }

            @Override
            public void onContinuousUpdate(CarShopSimulation.State state) {
                System.out.println("Continous update" + CarShopSimulationCore.TimeUtils.formatDayTime(state.simulationTime));
                for (String s: state.cars) {
                    System.out.println(s);
                }
            }
        });
        sim.singleIteration();
    }

}