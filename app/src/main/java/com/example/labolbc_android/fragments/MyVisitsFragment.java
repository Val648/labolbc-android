package com.example.labolbc_android.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.example.labolbc_android.R;
import com.example.labolbc_android.SessionManager;
import com.example.labolbc_android.api.ApiClient;
import com.example.labolbc_android.api.ApiService;
import com.example.labolbc_android.api.VisiteAdapter;
import com.example.labolbc_android.entity.Visite;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyVisitsFragment extends Fragment {

    private RecyclerView recyclerView;
    private VisiteAdapter adapter;
    private List<Visite> visiteList = new ArrayList<>();
    private List<Visite> filteredList = new ArrayList<>();
    private SearchView searchView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private SessionManager sessionManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        sessionManager = new SessionManager(requireContext());
        
        // Redirection si non connecté (doublon de sécurité avec MainActivity)
        if (!sessionManager.isLoggedIn()) {
            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                    .navigate(R.id.navigation_login);
            return null;
        }

        View view = inflater.inflate(R.layout.fragment_my_visits, container, false);

        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_my_visits);
        recyclerView = view.findViewById(R.id.rv_my_visits);
        searchView = view.findViewById(R.id.search_view_my_visits);
        FloatingActionButton fabAdd = view.findViewById(R.id.fab_add_visite);

        // Configuration du SwipeRefresh
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setOnRefreshListener(this::fetchMyVisites);
            swipeRefreshLayout.setColorSchemeResources(R.color.blue_primary);
        }

        // Configuration du RecyclerView avec le mode personnel activé
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new VisiteAdapter(filteredList, true); // true = mode personnel
        recyclerView.setAdapter(adapter);

        // Gestion de la suppression
        adapter.setOnDeleteClickListener(visite -> deleteVisite(visite));

        setupSearchView();

        // Bouton Ajouter
        fabAdd.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.navigation_add_visite);
        });

        fetchMyVisites();

        return view;
    }

    private void setupSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) { return false; }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });
    }

    private void filter(String text) {
        filteredList.clear();
        String query = text.toLowerCase().trim();
        for (Visite item : visiteList) {
            if (query.isEmpty() || 
                (item.getMotifVisite() != null && item.getMotifVisite().toLowerCase().contains(query)) ||
                (item.getNomPraticien() != null && item.getNomPraticien().toLowerCase().contains(query))) {
                filteredList.add(item);
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void fetchMyVisites() {
        if (swipeRefreshLayout != null && !swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(true);
        }

        ApiService apiService = ApiClient.getService(getContext());
        // L'API utilise le token via l'intercepteur dans ApiClient
        apiService.getVisitesVisiteur().enqueue(new Callback<List<Visite>>() {
            @Override
            public void onResponse(@NonNull Call<List<Visite>> call, @NonNull Response<List<Visite>> response) {
                if (swipeRefreshLayout != null) swipeRefreshLayout.setRefreshing(false);
                
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("MyVisitsFragment", "Visites reçues : " + response.body().size());
                    visiteList.clear();
                    visiteList.addAll(response.body());
                    filter(searchView.getQuery().toString());
                } else {
                    Log.e("MyVisitsFragment", "Erreur : " + response.code() + " " + response.message());
                    Toast.makeText(getContext(), "Erreur de chargement des visites", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Visite>> call, @NonNull Throwable t) {
                if (swipeRefreshLayout != null) swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(getContext(), "Erreur réseau", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteVisite(Visite visite) {
        ApiService apiService = ApiClient.getService(getContext());
        apiService.deleteVisite(visite.getId()).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Visite supprimée", Toast.LENGTH_SHORT).show();
                    fetchMyVisites(); // Recharger la liste
                } else {
                    Toast.makeText(getContext(), "Échec de la suppression", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Erreur réseau", Toast.LENGTH_SHORT).show();
            }
        });
    }
}