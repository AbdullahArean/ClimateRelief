package com.arean.ClimateRelief.ui.donate;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.arean.ClimateRelief.databinding.FragmentClaimBinding;
import com.arean.ClimateRelief.databinding.FragmentDonateBinding;
import com.arean.ClimateRelief.databinding.FragmentHomeBinding;
import com.arean.ClimateRelief.ui.claim.ClaimViewModel;
import com.arean.ClimateRelief.ui.home.HomeViewModel;


public class DonateFragment extends Fragment {

    private FragmentDonateBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DonateViewModel donateViewModel =
                new ViewModelProvider(this).get(DonateViewModel.class);

        binding = FragmentDonateBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textDonate;
        donateViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}