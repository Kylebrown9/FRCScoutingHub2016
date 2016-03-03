package org.ncfrcteams.frcscoutinghub2016.network;

/**
 * Created by Admin on 3/2/2016.
 */
public abstract class Job extends Thread {
    private boolean alive = true;
    private boolean loops;

    public Job(boolean loops) {
        this.loops = loops;
    }

    public void run() {
        init();
        while (alive && loops) {
//            try {
//                Thread.sleep(1);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            periodic();
        }
    }

    public void kill() {
        alive = false;
    }

    public abstract void init();
    public abstract void periodic();
}
