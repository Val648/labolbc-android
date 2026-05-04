package com.example.labolbc_android.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.labolbc_android.R;

public class AddVisiteFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Pour l'instant on utilise un layout simple, à personnaliser plus tard
        return inflater.inflate(R.layout.fragment_all_visits, container, false);
    }
}