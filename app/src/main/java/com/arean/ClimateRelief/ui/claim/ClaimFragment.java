package com.arean.ClimateRelief.ui.claim;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.arean.ClimateRelief.databinding.FragmentClaimBinding;

public class ClaimFragment extends Fragment {

    private FragmentClaimBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ClaimViewModel claimViewModel =
                new ViewModelProvider(this).get(ClaimViewModel.class);

        binding = FragmentClaimBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textClaim;
        claimViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}