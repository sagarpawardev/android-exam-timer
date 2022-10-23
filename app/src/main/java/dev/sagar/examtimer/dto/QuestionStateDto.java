package dev.sagar.examtimer.dto;

import java.io.Serializable;

public class QuestionStateDto implements Serializable {
    private long timeSpent;
    private boolean isRunning = false;
    private boolean isVisited = false;

    public long getTimeSpent() {
        return timeSpent;
    }

    public void setTimeSpent(long timeSpent) {
        this.timeSpent = timeSpent;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    public boolean isVisited() {
        return isVisited;
    }

    public void setVisited(boolean visited) {
        isVisited = visited;
    }
}
