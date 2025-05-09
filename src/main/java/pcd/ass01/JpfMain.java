package pcd.ass01;

import pcd.ass01.monitor.SimulationMonitor;
import pcd.ass01.worker.MultiWorker;

import java.util.List;
import java.util.concurrent.CyclicBarrier;

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
        model.getThreads().forEach(Thread::start);
        model.stopWorkers();
        
//        var width = SCREEN_WIDTH;
//        var height = SCREEN_HEIGHT;
//        var maxSpeed = MAX_SPEED;
//
//        P2d pos = new P2d(-width / 2 + 1 * width, -height / 2 + 1 * height);
//        V2d vel = new V2d(1 * maxSpeed / 2 - maxSpeed / 4, 1 * maxSpeed / 2 - maxSpeed / 4);
//
//        var barrier = new CyclicBarrier(2, () -> {
//            SpatialHashGrid grid = model.getGrid();
//            grid.clear();
//            for (Boid boid : model.getBoids()) {
//                grid.insert(boid);
//            }
//        });
//        var x1 = new MultiWorker(List.of(new Boid(pos, vel)), model, null, barrier, simMonitor);
//        var x2 = new MultiWorker(List.of(new Boid(pos, vel)), model, null, barrier, simMonitor);
//
//        x1.start();
//        x2.start();
//
//
//        x1.interrupt();
//        x2.interrupt();
//        x1.join();
//        x2.join();

    }
}
