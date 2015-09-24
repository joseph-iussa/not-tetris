package net.jiussa.nottetris;

import java.util.ArrayList;
import java.util.List;

public class DelayedTasks {
    public final int maxSize;
    private List<DelayedTask> freeList;
    private List<DelayedTask> activeList;

    public DelayedTasks(int maxSize) {
        this.maxSize = maxSize;
        freeList = new ArrayList<>(maxSize);
        activeList = new ArrayList<>(maxSize);
    }

    public void add(float delayFor, Task task) {
        DelayedTask delayedTask;
        if (freeList.size() == 0) {
            delayedTask = new DelayedTask();
        } else {
            delayedTask = freeList.remove(freeList.size() - 1);
        }
        delayedTask.init(delayFor, task);
        activeList.add(delayedTask);
    }

    public void update(float dt) {
        for (int i = activeList.size() - 1; i >= 0; --i) {
            DelayedTask t = activeList.get(i);
            boolean completed = t.update(dt);
            if (completed) {
                activeList.remove(i);
                returnTask(t);
            }
        }
    }

    private void returnTask(DelayedTask t) {
        if (freeList.size() < maxSize) {
            freeList.add(t);
        }
    }
}