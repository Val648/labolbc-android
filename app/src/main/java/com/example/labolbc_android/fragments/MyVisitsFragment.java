package com.example.labolbc_android.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.example.labolbc_android.R;
import com.example.labolbc_android.SessionManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MyVisitsFragment extends Fragment {

    private SessionManager sessionManager;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        sessionManager = new SessionManager(requireContext());

        if (!sessionManager.isLoggedIn()) {
            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                    .navigate(R.id.navigation_login);
            return null;
        }

        View view = inflater.inflate(R.layout.fragment_my_visits, container, false);
        
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_my_visits);
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setOnRefreshListener(this::refreshData);
        }

        FloatingActionButton fab = view.findViewById(R.id.fab_add_visite);
        fab.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.navigation_add_visite));
        
        return view;
    }

    private void refreshData() {
        swipeRefreshLayout.postDelayed(() -> {
            if (swipeRefreshLayout != null) {
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 1500);
    }
}