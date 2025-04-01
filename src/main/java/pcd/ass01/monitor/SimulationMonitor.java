package pcd.ass01.monitor;

public class SimulationMonitor {

    private boolean simulationIsRunning = false;
    private boolean endSimulation = false;


    public synchronized boolean isSimulationRunning(){
        return simulationIsRunning;
    }

    public synchronized void startSimulation(){
        endSimulation = false;
        simulationIsRunning = true;
        notifyAll();
    }

    public synchronized void stopSimulation(){
        simulationIsRunning = false;
    }

    public synchronized void endSimulation(){
        endSimulation = true;
    }

    public synchronized void waitIfSimulationIsStopped() {
        while (!this.simulationIsRunning) {
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println("Simulation interrupted, " + e.getMessage());
            }
        }
    }

    public synchronized boolean isEndSimulation() {
        return endSimulation;
    }
}
