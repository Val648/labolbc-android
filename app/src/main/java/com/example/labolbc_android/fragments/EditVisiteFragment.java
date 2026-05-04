package com.example.labolbc_android.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.example.labolbc_android.R;
import com.example.labolbc_android.SessionManager;
import com.example.labolbc_android.api.ApiClient;
import com.example.labolbc_android.api.ApiService;
import com.example.labolbc_android.entity.Praticien;
import com.example.labolbc_android.entity.Visite;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditVisiteFragment extends Fragment {

    private EditText etVisiteurName, etVisitDate, etMotif, etBilan;
    private Spinner spinnerPraticiens;
    private Button btnSubmit;
    private ImageButton btnBack;
    
    private int visiteId;
    private Visite currentVisite;
    private List<Praticien> praticiensList = new ArrayList<>();
    private ArrayAdapter<Praticien> praticienAdapter;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    private Calendar calendar = Calendar.getInstance();
    private SessionManager sessionManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            visiteId = getArguments().getInt("visite_id");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_visite, container, false);

        sessionManager = new SessionManager(requireContext());
        
        etVisiteurName = view.findViewById(R.id.et_visiteur_name);
        etVisitDate = view.findViewById(R.id.et_visit_date);
        etMotif = view.findViewById(R.id.et_motif);
        etBilan = view.findViewById(R.id.et_bilan);
        spinnerPraticiens = view.findViewById(R.id.spinner_praticiens);
        btnSubmit = view.findViewById(R.id.btn_submit_update);
        btnBack = view.findViewById(R.id.btn_back);

        praticienAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, praticiensList);
        praticienAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPraticiens.setAdapter(praticienAdapter);

        etVisitDate.setOnClickListener(v -> showDatePicker());
        btnBack.setOnClickListener(v -> Navigation.findNavController(v).navigateUp());
        btnSubmit.setOnClickListener(v -> updateVisite());

        loadInitialData();

        return view;
    }

    private void showDatePicker() {
        new DatePickerDialog(requireContext(), (view, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            etVisitDate.setText(dateFormat.format(calendar.getTime()));
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void loadInitialData() {
        ApiService apiService = ApiClient.getService(getContext());

        // 1. Charger d'abord les praticiens (nécessaire pour la sélection automatique dans le Spinner)
        apiService.getPraticiensSameRegion().enqueue(new Callback<List<Praticien>>() {
            @Override
            public void onResponse(@NonNull Call<List<Praticien>> call, @NonNull Response<List<Praticien>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    praticiensList.clear();
                    praticiensList.addAll(response.body());
                    praticienAdapter.notifyDataSetChanged();
                    
                    // 2. Charger les détails de la visite
                    fetchVisiteDetails();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Praticien>> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Erreur lors de la récupération des praticiens", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchVisiteDetails() {
        ApiService apiService = ApiClient.getService(getContext());
        apiService.getVisiteVisiteur(visiteId).enqueue(new Callback<Visite>() {
            @Override
            public void onResponse(@NonNull Call<Visite> call, @NonNull Response<Visite> response) {
                if (response.isSuccessful() && response.body() != null) {
                    currentVisite = response.body();
                    prefillForm();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Visite> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Erreur lors du chargement des détails", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void prefillForm() {
        // Pré-remplir le visiteur (lecture seule)
        etVisiteurName.setText(currentVisite.getNomVisiteur());
        
        // Pré-remplir la date
        if (currentVisite.getDateVisite() != null) {
            etVisitDate.setText(dateFormat.format(currentVisite.getDateVisite()));
            calendar.setTime(currentVisite.getDateVisite());
        }
        
        // Pré-remplir Motif et Bilan
        etMotif.setText(currentVisite.getMotifVisite());
        etBilan.setText(currentVisite.getBilanVisite());

        // Sélection automatique du praticien dans le Spinner
        if (currentVisite.getPraticien() != null) {
            for (int i = 0; i < praticiensList.size(); i++) {
                if (praticiensList.get(i).getIdPraticien() == currentVisite.getPraticien().getIdPraticien()) {
                    spinnerPraticiens.setSelection(i);
                    break;
                }
            }
        }
    }

    private void updateVisite() {
        String motif = etMotif.getText().toString().trim();
        String bilan = etBilan.getText().toString().trim();
        Praticien selectedPraticien = (Praticien) spinnerPraticiens.getSelectedItem();

        if (motif.isEmpty() || selectedPraticien == null) {
            Toast.makeText(getContext(), "Le motif et le praticien sont obligatoires", Toast.LENGTH_SHORT).show();
            return;
        }

        currentVisite.setMotifVisite(motif);
        currentVisite.setBilanVisite(bilan);
        currentVisite.setPraticien(selectedPraticien);
        currentVisite.setDateVisite(calendar.getTime());

        ApiService apiService = ApiClient.getService(getContext());
        apiService.updateVisite(visiteId, currentVisite).enqueue(new Callback<Visite>() {
            @Override
            public void onResponse(@NonNull Call<Visite> call, @NonNull Response<Visite> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Visite modifiée avec succès", Toast.LENGTH_SHORT).show();
                    Navigation.findNavController(requireView()).navigateUp();
                } else {
                    Toast.makeText(getContext(), "Erreur lors de la modification", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Visite> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Erreur réseau", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
