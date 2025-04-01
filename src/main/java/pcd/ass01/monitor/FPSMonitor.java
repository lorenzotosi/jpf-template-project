package pcd.ass01.monitor;

public class FPSMonitor {
    private final int nWorkers;
    private int threadIsDone = 0;

    public FPSMonitor(int nWorkers) {
        this.nWorkers = nWorkers;
    }

    public synchronized void workerFinishWork() {
        try {
            threadIsDone++;
            if (threadIsDone == nWorkers) {
                notifyAll();
            }
            while (threadIsDone != 0) {
                wait();
            }
        } catch (InterruptedException e) {
            System.out.println("FPSMonitor thread interrupted");
        }
    }

    public synchronized void resetAndRestart() {
        threadIsDone = 0;
        notifyAll();
    }

    public synchronized void waitWorkDone() {
        try {
            while (threadIsDone < nWorkers) {
                wait();
            }
        } catch (InterruptedException e) {
            System.out.println("FPSMonitor thread interrupted");
        }
    }
}
