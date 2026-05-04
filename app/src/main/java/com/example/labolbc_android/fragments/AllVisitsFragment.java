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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.example.labolbc_android.R;
import com.example.labolbc_android.api.ApiClient;
import com.example.labolbc_android.api.ApiService;
import com.example.labolbc_android.api.VisiteAdapter;
import com.example.labolbc_android.entity.Visite;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllVisitsFragment extends Fragment {

    private RecyclerView recyclerView;
    private VisiteAdapter adapter;
    private List<Visite> visiteList = new ArrayList<>();
    private List<Visite> filteredList = new ArrayList<>();
    private SearchView searchView;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_visits, container, false);
      
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_all_visits);
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setOnRefreshListener(this::refreshData);
            // Optionnel : Couleurs du cercle de chargement
            swipeRefreshLayout.setColorSchemeResources(R.color.blue_primary, android.R.color.holo_blue_dark);
        }

        recyclerView = view.findViewById(R.id.rv_visites);
        searchView = view.findViewById(R.id.search_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new VisiteAdapter(filteredList);
        recyclerView.setAdapter(adapter);

        setupSearchView();
        
        // Charger les données initiales
        fetchVisites();

        return view;
    }

    private void setupSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });
    }

    private void filter(String text) {
        filteredList.clear();
        if (text.isEmpty()) {
            filteredList.addAll(visiteList);
        } else {
            String query = text.toLowerCase().trim();
            for (Visite item : visiteList) {
                if ((item.getMotifVisite() != null && item.getMotifVisite().toLowerCase().contains(query)) ||
                    (item.getNomPraticien() != null && item.getNomPraticien().toLowerCase().contains(query)) ||
                    (item.getNomVisiteur() != null && item.getNomVisiteur().toLowerCase().contains(query))) {
                    filteredList.add(item);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void fetchVisites() {
        // Indiquer visuellement le chargement si ce n'est pas initié par un swipe
        if (swipeRefreshLayout != null && !swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(true);
        }

        ApiService apiService = ApiClient.getService(getContext());
        apiService.getVisites().enqueue(new Callback<List<Visite>>() {
            @Override
            public void onResponse(@NonNull Call<List<Visite>> call, @NonNull Response<List<Visite>> response) {
                if (swipeRefreshLayout != null) {
                    swipeRefreshLayout.setRefreshing(false);
                }
                if (response.isSuccessful() && response.body() != null) {
                    visiteList.clear();
                    visiteList.addAll(response.body());
                    filter(searchView.getQuery().toString());
                } else {
                    Toast.makeText(getContext(), "Erreur serveur", Toast.LENGTH_SHORT).show();
                    // Données de secours pour la démo si l'API échoue
                    addMockDataIfEmpty();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Visite>> call, @NonNull Throwable t) {
                if (swipeRefreshLayout != null) {
                    swipeRefreshLayout.setRefreshing(false);
                }
                Log.e("AllVisitsFragment", "Failure: " + t.getMessage());
                Toast.makeText(getContext(), "Impossible de contacter le serveur", Toast.LENGTH_SHORT).show();
                addMockDataIfEmpty();
            }
        });
    }

    private void addMockDataIfEmpty() {
        if (visiteList.isEmpty()) {
            visiteList.add(new Visite(1, new Date(), "Visite de routine", "Bilan.pdf", "Jean Dupont", "Dr. Martin"));
            visiteList.add(new Visite(2, new Date(), "Suivi mensuel", "Suivi.pdf", "Alice Durand", "Dr. Bernard"));
            filter(searchView.getQuery().toString());
        }
    }

    private void refreshData() {
        fetchVisites();
    }
}
