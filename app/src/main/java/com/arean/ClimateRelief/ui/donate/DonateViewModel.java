package com.arean.ClimateRelief.ui.donate;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DonateViewModel extends ViewModel {
    private final MutableLiveData<String> mText;

    public DonateViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Donate fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}