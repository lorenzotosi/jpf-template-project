package pcd.ass01.worker;

import pcd.ass01.Boid;
import pcd.ass01.BoidsModel;
import pcd.ass01.concurrency.MyBarrier;
import pcd.ass01.monitor.SimulationMonitor;

import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class MultiWorker extends Thread {

    private final List<Boid> boids;
    private final BoidsModel boidsModel;
    private final MyBarrier phase1Barrier;
    private final CyclicBarrier phase2Barrier;
    private final SimulationMonitor simulationMonitor;

    public MultiWorker(List<Boid> boids, BoidsModel boidsModel, MyBarrier phase1Barrier,
                       CyclicBarrier phase2Barrier, SimulationMonitor simulationMonitor) {
        this.boids = boids;
        this.boidsModel = boidsModel;
        this.phase1Barrier = phase1Barrier;
        this.phase2Barrier = phase2Barrier;
        this.simulationMonitor = simulationMonitor;
    }

    public void run() {
        int i = 0;
        while (i < 3) {
            simulationMonitor.waitIfSimulationIsStopped();
            try {
                boids.forEach(boid -> boid.calculateVelocity(boidsModel));
                phase2Barrier.await();
                boids.forEach(boid -> boid.updateVelocity(boidsModel));
                phase2Barrier.await();
                boids.forEach(boid -> boid.updatePos(boidsModel));
                phase2Barrier.await();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                //break;
            } catch (BrokenBarrierException e) {
                //throw new RuntimeException(e);
                //break;
            }
            i++;
            System.out.println(i);
        }


    }

}
