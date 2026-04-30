package com.example.labolbc_android.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.labolbc_android.R;
import com.example.labolbc_android.api.ApiClient;
import com.example.labolbc_android.api.ApiService;
import com.example.labolbc_android.api.VisiteAdapter;
import com.example.labolbc_android.entity.Visite;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllVisitsFragment extends Fragment {

    private RecyclerView recyclerView;
    private VisiteAdapter adapter;
    private List<Visite> visiteList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_visits, container, false);

        recyclerView = view.findViewById(R.id.rv_visites);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new VisiteAdapter(visiteList);
        recyclerView.setAdapter(adapter);

        fetchVisites();

        return view;
    }

    private void fetchVisites() {
        ApiService apiService = ApiClient.getService(getContext());
        apiService.getVisites().enqueue(new Callback<List<Visite>>() {
            @Override
            public void onResponse(Call<List<Visite>> call, Response<List<Visite>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    visiteList.clear();
                    visiteList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), "Erreur lors de la récupération des visites", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Visite>> call, Throwable t) {
                Log.e("AllVisitsFragment", "Failure: " + t.getMessage());
                Toast.makeText(getContext(), "Connexion impossible au serveur", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
