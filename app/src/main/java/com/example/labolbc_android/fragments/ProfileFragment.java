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
import com.example.labolbc_android.SessionManager;
import com.example.labolbc_android.entity.User;

public class ProfileFragment extends Fragment {

    private SessionManager sessionManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        sessionManager = new SessionManager(requireContext());

        if (!sessionManager.isLoggedIn()) {
            // Rediriger vers la connexion si non connecté
            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                    .navigate(R.id.navigation_login);
            return null;
        }

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        TextView tvProfileInfo = view.findViewById(R.id.tv_profile_info); // Assurez-vous d'avoir cet ID dans le XML
        Button btnLogout = view.findViewById(R.id.btn_logout);

        User user = sessionManager.getUser();
        if (user != null && tvProfileInfo != null) {
            tvProfileInfo.setText("Bienvenue, " + user.getNom() + "\nEmail: " + user.getEmail());
        }

        btnLogout.setOnClickListener(v -> {
            sessionManager.logout();
            // Rediriger vers la page de connexion après déconnexion
            Navigation.findNavController(v).navigate(R.id.navigation_login);
        });

        return view;
    }
}