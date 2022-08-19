package com.galaxy.orbitsatellitevisualizer.connection;

/**
 * This class in charge to fill the status updater
 */
public class StatusUpdater implements Runnable {
    private volatile boolean cancelled;

    private LGConnectionManager lgConnectionManager;

    StatusUpdater(LGConnectionManager lgConnectionManager) {
        this.lgConnectionManager = lgConnectionManager;
    }

    public void run() {
        try {
            while (!cancelled) {
                lgConnectionManager.tick();
                Thread.sleep(200L); //TICKS every 200ms
            }
        } catch (InterruptedException ignored) {

        }
    }

    public void cancel() {
        cancelled = true;
    }
}