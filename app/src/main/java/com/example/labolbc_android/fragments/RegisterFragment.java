package com.example.labolbc_android.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.example.labolbc_android.R;

public class RegisterFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        Button btnRegister = view.findViewById(R.id.btn_register);
        TextView tvLogin = view.findViewById(R.id.tv_login_link);

        btnRegister.setOnClickListener(v -> {
            // Logique d'inscription simplifiée
            Navigation.findNavController(v).navigate(R.id.navigation_profile);
        });

        tvLogin.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.navigation_login);
        });

        return view;
    }
}