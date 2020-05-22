package com.exercise.models;

public class GenerateCriteria {

    private Integer goal;

    private Integer step;

    public GenerateCriteria(final Integer goal, final Integer step) {
        this.goal = goal;
        this.step = step;
    }

    public int getGoal() {
        return goal;
    }

    public void setGoal(int goal) {
        this.goal = goal;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }
}
