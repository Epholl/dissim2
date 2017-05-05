package sk.epholl.dissim.carshop;

import org.junit.Test;
import sk.epholl.dissim.core.Simulation;

/**
 * Created by Tomáš on 07.04.2017.
 */
public class CarShopSimulationTest {

    @Test
    public void testMultipleRuns() {
        CarShopSimulationParameters params = new CarShopSimulationParameters(3000, 10d, 4, 21, 90*24);

        CarShopSimulation simulation = new CarShopSimulation(params, new Simulation.SimulationListener<CarShopSimulation.Result, CarShopSimulation.State>() {
            @Override
            public void onStarted() {

            }

            @Override
            public void onFinished(CarShopSimulation.Result results) {
                System.out.println("IterationResults: " + results);
            }

            @Override
            public void onGuiUpdate(double progress, CarShopSimulation.Result results) {
                System.out.println("Progress: " + progress);
            }

            @Override
            public void onGuiSimulationStatus(CarShopSimulation.State state) {

            }
        });
        simulation.run();
    }

    @Test
    public void testAllPossibilities(){
        for (int i = 1; i < 8; i++) {
            for (int j = 17; j < 25; j++) {
                singleTest(i, j);

            }

        }
    }

    public void singleTest(int type1, int type2) {
        CarShopSimulationParameters params = new CarShopSimulationParameters(
                100,
                10d,
                type1,
                type2,
                90*8);

        CarShopSimulation simulation = new CarShopSimulation(params, new Simulation.SimulationListener<CarShopSimulation.Result, CarShopSimulation.State>() {
            @Override
            public void onStarted() {

            }

            @Override
            public void onFinished(CarShopSimulation.Result results) {
                System.out.println("[" + type1 + ", " + type2 + "]: "
                        + CarShopSimulationCore.TimeUtils.formatTimePeriod(results.averageEntryWait.getMean()) + ", "
                + CarShopSimulationCore.TimeUtils.formatTimePeriod(results.averageTimeFromAcquisitionToReturn.getMean())
                + String.format(", %.2f", results.refusedCustomers.getMean()));
            }

            @Override
            public void onGuiUpdate(double progress, CarShopSimulation.Result results) {

            }

            @Override
            public void onGuiSimulationStatus(CarShopSimulation.State state) {

            }
        });
        simulation.run();
    }

}