package com.arean.ClimateRelief.ui.account;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.arean.ClimateRelief.databinding.FragmentAccountBinding;
import com.arean.ClimateRelief.ui.activity.LoginActivity;
import com.arean.ClimateRelief.R;
import com.arean.ClimateRelief.ui.activity.RegisterActivity;

public class AccountFragment extends Fragment {



    private FragmentAccountBinding binding;
    private Button buttonRegisterFromAccount;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {




        AccountViewModel accountViewModel =
                new ViewModelProvider(this).get(AccountViewModel.class);

        binding = FragmentAccountBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textAccount;
        accountViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        Button buttonRegisterFromAccount = (Button) root.findViewById(R.id.buttonRegisterFromAccount);

        buttonRegisterFromAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), RegisterActivity.class);
                startActivity(intent);
            }
        });

//        buttonLoginFromAccount


        Button buttonLoginFromAccount = (Button) root.findViewById(R.id.buttonLoginFromAccount);

        buttonLoginFromAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });






        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}