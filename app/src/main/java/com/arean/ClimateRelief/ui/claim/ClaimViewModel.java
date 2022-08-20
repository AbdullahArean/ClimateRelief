package com.arean.ClimateRelief.ui.claim;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ClaimViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public ClaimViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Claim fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}