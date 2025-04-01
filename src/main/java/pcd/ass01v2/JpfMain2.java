package pcd.ass01v2;

import pcd.ass01v2.monitor.SimulationMonitor;

import java.util.concurrent.CountDownLatch;

public class JpfMain2 {
    final static int N_BOIDS = 1500;

    final static double SEPARATION_WEIGHT = 1.0;
    final static double ALIGNMENT_WEIGHT = 1.0;
    final static double COHESION_WEIGHT = 1.0;

    final static int ENVIRONMENT_WIDTH = 1000;
    final static int ENVIRONMENT_HEIGHT = 1000;
    static final double MAX_SPEED = 4.0;
    static final double PERCEPTION_RADIUS = 50.0;
    static final double AVOID_RADIUS = 20.0;

    final static int SCREEN_WIDTH = 800;
    final static int SCREEN_HEIGHT = 800;


    public static void main(String[] args) {
        var simMonitor = new SimulationMonitor();
        var model = new BoidsModel(
                N_BOIDS,
                SEPARATION_WEIGHT, ALIGNMENT_WEIGHT, COHESION_WEIGHT,
                ENVIRONMENT_WIDTH, ENVIRONMENT_HEIGHT,
                MAX_SPEED,
                PERCEPTION_RADIUS,
                AVOID_RADIUS,
                simMonitor);

        model.setupThreads(2);

        model.getSimulationMonitor().startSimulation();
        CountDownLatch countDownLatch = new CountDownLatch(BoidsModel.N_THREADS);
        try {
            model.execute1(countDownLatch);
            countDownLatch.await();
        } catch (Exception e) {
            System.out.println("Boids simulation interrupted, " + e.getMessage());
        }
        finally {
            countDownLatch = new CountDownLatch(BoidsModel.N_THREADS);
        }
        //model.setupThreads(0);
        try {
            model.execute2(countDownLatch);
            countDownLatch.await();
        } catch (InterruptedException e) {
            System.out.println("Boids simulation interrupted, " + e.getMessage());
        }
        model.getExecutor().shutdown();
        model.getSimulationMonitor().stopSimulation();
        //System.out.println("Boids simulation finished, " + BoidsModel.N_THREADS);

    }
}
