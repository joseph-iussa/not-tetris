package net.jiussa.nottetris;

public class DelayedTask {
    private Timer timer;
    private Task task;

    public DelayedTask() {
        timer = new Timer(0);
    }

    public void init(float delayFor, Task task) {
        timer.init(delayFor);
        this.task = task;
    }

    public boolean update(float dt) {
        if (timer.update(dt)) {
            task.execute();
            return true;
        }
        return false;
    }

    public void resetTimer() {
        timer.reset();
    }
}