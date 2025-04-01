package pcd.ass01.concurrency;

public class MyBarrier {
    private final int nWorkers;
    private int nTotal;
    private boolean isBroken = false;

    public MyBarrier(int nWorkers) {
        this.nWorkers = nWorkers;
        this.nTotal = nWorkers;
    }

    public synchronized void await() throws InterruptedException {
        if (isBroken) {
            throw new InterruptedException("Barrier is broken");
        }

        nTotal--;

        if (nTotal > 0) {
            while (nTotal > 0 && !isBroken) {
                wait();
            }
        } else {
            nTotal = nWorkers; // Reset per il riutilizzo
            notifyAll(); // Sveglia tutti i thread in attesa
        }

        if (isBroken) {
            throw new InterruptedException("Barrier was broken");
        }
    }

    // Metodo per rompere la barriera (es. quando endSimulation = true)
    public synchronized void breakBarrier() {
        isBroken = true;
        notifyAll(); // Sveglia tutti i thread bloccati
    }
}