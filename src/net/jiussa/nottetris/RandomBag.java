package net.jiussa.nottetris;

import java.util.Collections;
import java.util.List;

public class RandomBag<T> {
    private List<T> items;
    private int currentIdx;

    public RandomBag(List<T> items) {
        this.items = items;
        currentIdx = 0;
        shuffle();
    }

    private void shuffle() {
        Collections.shuffle(items);
    }

    public T nextItem() {
        if (currentIdx < items.size()) {
            return items.get(currentIdx++);
        } else {
            shuffle();
            currentIdx = 0;
            return items.get(currentIdx++);
        }
    }
}