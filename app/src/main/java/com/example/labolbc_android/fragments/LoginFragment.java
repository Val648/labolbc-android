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

public class LoginFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        Button btnLogin = view.findViewById(R.id.btn_login);
        TextView tvRegister = view.findViewById(R.id.tv_register_link);

        btnLogin.setOnClickListener(v -> {
            // Logique de connexion simplifiée
            Navigation.findNavController(v).navigate(R.id.navigation_profile);
        });

        tvRegister.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.navigation_register);
        });

        return view;
    }
}