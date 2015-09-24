package net.jiussa.nottetris;

public class Timer {
    private float countUpTo;
    private float counter;

    public Timer() {
        this(0);
    }

    public Timer(float countUpTo) {
        init(countUpTo);
    }

    public void init(float countUpTo) {
        this.countUpTo = countUpTo;
        reset();
    }

    public void reset() {
        counter = 0;
    }

    public void reset(float countUpTo) {
        this.countUpTo = countUpTo;
        counter = 0;
    }

    public boolean update(float dt) {
        counter += dt;
        return counter >= countUpTo;
    }
}