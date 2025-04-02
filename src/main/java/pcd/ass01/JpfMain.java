package pcd.ass01;

import pcd.ass01.monitor.SimulationMonitor;
import pcd.ass01.worker.MultiWorker;

public class JpfMain {
    final static int N_BOIDS = 2;

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


    public static void main(String[] args) throws InterruptedException {
        var simMonitor = new SimulationMonitor();
        var model = new BoidsModel(
                N_BOIDS,
                SEPARATION_WEIGHT, ALIGNMENT_WEIGHT, COHESION_WEIGHT,
                ENVIRONMENT_WIDTH, ENVIRONMENT_HEIGHT,
                MAX_SPEED,
                PERCEPTION_RADIUS,
                AVOID_RADIUS,
                simMonitor);
        model.getSimulationMonitor().startSimulation();
        model.setupThreads(2);
        model.getThreads().forEach(MultiWorker::start);
        //Thread.sleep(1000);
        model.stopWorkers();
    }
}
