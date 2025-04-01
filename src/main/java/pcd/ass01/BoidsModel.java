package pcd.ass01;

import pcd.ass01.concurrency.MyBarrier;
import pcd.ass01.monitor.FPSMonitor;
import pcd.ass01.monitor.SimulationMonitor;
import pcd.ass01.worker.MultiWorker;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class BoidsModel {
    
    private final List<Boid> boids;
    private double separationWeight; 
    private double alignmentWeight; 
    private double cohesionWeight; 
    private final double width;
    private final double height;
    private final double maxSpeed;
    private final double perceptionRadius;
    private final double avoidRadius;
    private List<MultiWorker> threads;
    private MyBarrier myBarrier;
    private SimulationMonitor simulationMonitor;
    private boolean firstStart = true;
    private FPSMonitor frameMonitor;

    public BoidsModel(int nboids,
                      double initialSeparationWeight,
                      double initialAlignmentWeight,
                      double initialCohesionWeight,
                      double width,
                      double height,
                      double maxSpeed,
                      double perceptionRadius,
                      double avoidRadius,
                      SimulationMonitor simulationMonitor
                      /*FPSMonitor frameMonitor*/) {
        separationWeight = initialSeparationWeight;
        alignmentWeight = initialAlignmentWeight;
        cohesionWeight = initialCohesionWeight;
        this.width = width;
        this.height = height;
        this.maxSpeed = maxSpeed;
        this.perceptionRadius = perceptionRadius;
        this.avoidRadius = avoidRadius;
        this.simulationMonitor = simulationMonitor;
        this.frameMonitor = new FPSMonitor(BoidsSimulation.N_THREADS);
    	boids = new CopyOnWriteArrayList<>();
        threads = new ArrayList<>();
    }

    public void setupThreads(final int nboids) {
        boids.clear();
        if (nboids > 0) {
            threads.clear();
            firstStart = false;
            int nThreads = BoidsSimulation.N_THREADS;
            int nBoidsPerThread = nboids / nThreads;
            int poorBoids = nboids % nThreads;

            myBarrier = new MyBarrier(nThreads);
            int from = 0;
            int to = nBoidsPerThread - 1;

            for (int i = 0; i < nThreads; i++) {
                var b = new ArrayList<Boid>();
                for (int j = from; j <= to; j++) {
                    P2d pos = new P2d(-width / 2 + Math.random() * width, -height / 2 + Math.random() * height);
                    V2d vel = new V2d(Math.random() * maxSpeed / 2 - maxSpeed / 4, Math.random() * maxSpeed / 2 - maxSpeed / 4);
                    b.add(new Boid(pos, vel));
                }

                if (poorBoids != 0) {
                    P2d pos = new P2d(-width / 2 + Math.random() * width, -height / 2 + Math.random() * height);
                    V2d vel = new V2d(Math.random() * maxSpeed / 2 - maxSpeed / 4, Math.random() * maxSpeed / 2 - maxSpeed / 4);
                    b.add(new Boid(pos, vel));
                    poorBoids--;
                }

                var thread = new MultiWorker(b, this, myBarrier, simulationMonitor, frameMonitor);
                threads.add(thread);
                this.boids.addAll(b);
            }
        }

    }

    public List<Boid> getBoids(){
    	return boids;
    }

    public SimulationMonitor getSimulationMonitor() {
        return this.simulationMonitor;
    }

    public FPSMonitor getFrameMonitor() {
        return this.frameMonitor;
    }

    public List<MultiWorker> getThreads(){
        return threads;
    }
    
    public double getMinX() {
    	return -width/2;
    }

    public double getMaxX() {
    	return width/2;
    }

    public double getMinY() {
    	return -height/2;
    }

    public double getMaxY() {
    	return height/2;
    }
    
    public double getWidth() {
    	return width;
    }
 
    public double getHeight() {
    	return height;
    }

    public synchronized void setSeparationWeight(double value) {
    	this.separationWeight = value;
    }

    public synchronized void setAlignmentWeight(double value) {
    	this.alignmentWeight = value;
    }

    public synchronized void setCohesionWeight(double value) {
    	this.cohesionWeight = value;
    }

    public double getSeparationWeight() {
    	return separationWeight;
    }

    public double getCohesionWeight() {
    	return cohesionWeight;
    }

    public double getAlignmentWeight() {
    	return alignmentWeight;
    }
    
    public double getMaxSpeed() {
    	return maxSpeed;
    }

    public double getAvoidRadius() {
    	return avoidRadius;
    }

    public double getPerceptionRadius() {
    	return perceptionRadius;
    }

    public boolean isFirstStart() {
        return firstStart;
    }

    public void resetFirstStart() {
        firstStart = true;
    }
}
