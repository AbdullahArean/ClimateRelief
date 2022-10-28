package com.arean.ClimateRelief.model;

public class recyclerViewModel {
    final Integer stepImages;
    final String stepNames;

    public recyclerViewModel(Integer stepImages, String stepNames)
    {
        this.stepImages = stepImages;
        this.stepNames = stepNames;
    }

    public Integer getStepImages() {
        return stepImages;
    }

    public String getStepNames() {
        return stepNames;
    }
}
