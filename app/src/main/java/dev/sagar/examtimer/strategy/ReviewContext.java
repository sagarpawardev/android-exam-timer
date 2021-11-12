package dev.sagar.examtimer.strategy;

import android.app.Activity;

public class ReviewContext {
    private final ReviewStrategy strategy;

    public ReviewContext(ReviewStrategy strategy) {
        this.strategy = strategy;
    }

    public boolean execute(Activity activity){
        return strategy.show(activity);
    }
}
