package pcd.ass01;

import pcd.ass01.worker.MultiWorker;

import java.util.Optional;

public class BoidsSimulator {

    private static final int FRAMERATE = 60;
    private final BoidsModel model;
    private Optional<BoidsView> view;
    
    private int framerate;
    
    public BoidsSimulator(BoidsModel model) {
        this.model = model;
        view = Optional.empty();
    }

    public void attachView(BoidsView view) {
    	this.view = Optional.of(view);
    }
      
    public void runSimulation() {
        while (true) {

            var t0 = System.currentTimeMillis();

            model.getFrameMonitor().waitWorkDone();

            if (view.isPresent()) {
                view.get().update(framerate);
                var t1 = System.currentTimeMillis();
                var dtElapsed = t1 - t0;
                var framratePeriod = 1000/FRAMERATE;

                if (dtElapsed < framratePeriod) {
                    try {
                        Thread.sleep(framratePeriod - dtElapsed);
                    } catch (Exception ex) {}
                    framerate = FRAMERATE;
                } else {
                    framerate = (int) (1000/dtElapsed);
                }
            }
            if(model.getSimulationMonitor().isEndSimulation()){
                model.getThreads().forEach(MultiWorker::interrupt);
            }
            model.getFrameMonitor().resetAndRestart();

        }
    }

}
