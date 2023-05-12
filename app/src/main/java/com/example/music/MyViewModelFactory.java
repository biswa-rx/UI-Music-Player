package com.example.music;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class MyViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private static SharedViewModel instance;

    public static SharedViewModel getInstance(Application application) {
        if (instance == null) {
            instance = new SharedViewModel();
        }
        return instance;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) instance;
    }
}

