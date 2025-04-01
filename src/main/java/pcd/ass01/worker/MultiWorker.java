package pcd.ass01.worker;

import pcd.ass01.Boid;
import pcd.ass01.BoidsModel;
import pcd.ass01.concurrency.MyBarrier;
import pcd.ass01.monitor.FPSMonitor;
import pcd.ass01.monitor.SimulationMonitor;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class MultiWorker extends Thread {

    private final List<Boid> boids;
    private final BoidsModel boidsModel;
    private final MyBarrier myBarrier;
    private final SimulationMonitor simulationMonitor;
    private final FPSMonitor fpsMonitor;
    private AtomicBoolean isRunning = new AtomicBoolean(true);

    public MultiWorker(List<Boid> boids, BoidsModel boidsModel,
                       MyBarrier myBarrier, SimulationMonitor simulationMonitor,
                       FPSMonitor fpsMonitor) {
        this.boids = boids;
        this.boidsModel = boidsModel;
        this.myBarrier = myBarrier;
        this.simulationMonitor = simulationMonitor;
        this.fpsMonitor = fpsMonitor;
    }

    public void run() {
        while (isRunning.get()) {
            simulationMonitor.waitIfSimulationIsStopped();
            try {
                boids.forEach(boid -> boid.calculateVelocity(boidsModel));
                myBarrier.await();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Ripristina lo stato di interrupt
                break;
            }
            try {
                boids.forEach(boid -> boid.updateVelocity(boidsModel));
                boids.forEach(boid -> boid.updatePos(boidsModel));
                myBarrier.await();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Ripristina lo stato di interrupt
                break;
            }
            fpsMonitor.workerFinishWork();
        }

    }

    @Override
    public void interrupt() {
        this.isRunning.compareAndSet(true, false);
        myBarrier.breakBarrier();
        super.interrupt();
    }

}
