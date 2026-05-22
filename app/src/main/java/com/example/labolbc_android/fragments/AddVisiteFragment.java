package com.example.labolbc_android.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
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
import com.example.labolbc_android.entity.Specialite;
import com.example.labolbc_android.entity.User;
import com.example.labolbc_android.entity.Visite;
import com.example.labolbc_android.entity.Visiteur;
import java.text.SimpleDateFormat;
import com.example.labolbc_android.entity.PraticienResponse;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddVisiteFragment extends Fragment {

    private TextView tvNomVisiteur;
    private EditText etDateVisite, etMotif, etBilan;
    private Spinner spinnerPraticien;
    private final Calendar calendar = Calendar.getInstance();
    private List<Praticien> praticienList;
    private SessionManager sessionManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_visite, container, false);

        sessionManager = new SessionManager(requireContext());
        
        tvNomVisiteur = view.findViewById(R.id.tv_nom_visiteur_display);
        etDateVisite = view.findViewById(R.id.et_date_visite);
        etMotif = view.findViewById(R.id.et_motif);
        etBilan = view.findViewById(R.id.et_bilan);
        spinnerPraticien = view.findViewById(R.id.spinner_praticien);
        Button btnValider = view.findViewById(R.id.btn_valider);
        ImageButton btnBack = view.findViewById(R.id.btn_back);

        btnBack.setOnClickListener(v -> Navigation.findNavController(v).navigateUp());

        User user = sessionManager.getUser();
        if (user != null) {
            tvNomVisiteur.setText(user.getName());
        }

        etDateVisite.setOnClickListener(v -> showDatePicker());

        loadPraticiens();

        btnValider.setOnClickListener(v -> validerVisite());

        return view;
    }

    private void showDatePicker() {
        new DatePickerDialog(requireContext(), (view, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDateLabel();
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void updateDateLabel() {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.FRANCE);
        etDateVisite.setText(sdf.format(calendar.getTime()));
    }

    private void loadPraticiens() {
        ApiService apiService = ApiClient.getService(requireContext());
        apiService.getPraticiensSameRegion().enqueue(new Callback<PraticienResponse>() {
            @Override
            public void onResponse(@NonNull Call<PraticienResponse> call, @NonNull Response<PraticienResponse> response) {
                if (!isAdded() || getContext() == null) return;

                if (response.isSuccessful() && response.body() != null) {
                    praticienList = response.body().getPraticiens();
                    
                    if (praticienList == null || praticienList.isEmpty()) {
                        Toast.makeText(getContext(), "Aucun praticien disponible.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    List<String> names = new ArrayList<>();
                    for (Praticien p : praticienList) {
                        String name = (p.getNomPraticien() != null ? p.getNomPraticien() : "") + " " + (p.getPrenomPraticien() != null ? p.getPrenomPraticien() : "");
                        if (p.getSpecialite() != null) {
                            name += " (" + p.getSpecialite().getLibelle() + ")";
                        }
                        names.add(name.trim().isEmpty() ? "Praticien " + p.getIdPraticien() : name);
                    }
                    
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, names);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerPraticien.setAdapter(adapter);
                } else {
                    Toast.makeText(getContext(), "Erreur praticiens: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<PraticienResponse> call, @NonNull Throwable t) {
                if (!isAdded() || getContext() == null) return;
                Toast.makeText(getContext(), "Erreur de connexion (Praticiens)", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void validerVisite() {
        if (praticienList == null || praticienList.isEmpty()) {
            Toast.makeText(getContext(), "Chargement des praticiens en cours...", Toast.LENGTH_SHORT).show();
            return;
        }

        String motif = etMotif.getText().toString().trim();
        String bilan = etBilan.getText().toString().trim();

        if (motif.isEmpty() || etDateVisite.getText().toString().isEmpty() || spinnerPraticien.getSelectedItem() == null) {
            Toast.makeText(getContext(), "Veuillez remplir les champs obligatoires", Toast.LENGTH_SHORT).show();
            return;
        }

        Praticien selectedPraticien = praticienList.get(spinnerPraticien.getSelectedItemPosition());
        User currentUser = sessionManager.getUser();

        if (currentUser == null) return;

        // On récupère le numeroSequentiel depuis la spécialité
        int nSeq = (selectedPraticien.getSpecialite() != null) ? selectedPraticien.getSpecialite().getId() : 0;

        Map<String, Object> body = new HashMap<>();
        body.put("motifVisite", motif);
        body.put("dateVisite", new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(calendar.getTime()));
        body.put("bilanVisite", bilan.isEmpty() ? null : bilan);
        
        // On envoie le numeroSequentiel à la racine car Repertorier en a besoin (Erreur 500)
        body.put("numeroSequentiel", nSeq);
        body.put("numseq", nSeq);

        Map<String, Object> praticienMap = new HashMap<>();
        praticienMap.put("idPraticien", selectedPraticien.getIdPraticien());
        praticienMap.put("numeroSequentiel", nSeq);
        
        if (selectedPraticien.getSpecialite() != null) {
            Map<String, Object> specialiteMap = new HashMap<>();
            specialiteMap.put("id", nSeq);
            praticienMap.put("specialite", specialiteMap);
        }
        body.put("praticien", praticienMap);
        
        Map<String, Object> visiteurMap = new HashMap<>();
        visiteurMap.put("idVisiteur", currentUser.getId());
        body.put("visiteur", visiteurMap);

        ApiService apiService = ApiClient.getService(requireContext());
        apiService.createVisite(body).enqueue(new Callback<Visite>() {
            @Override
            public void onResponse(@NonNull Call<Visite> call, @NonNull Response<Visite> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Visite ajoutée !", Toast.LENGTH_SHORT).show();
                    Navigation.findNavController(requireView()).navigateUp();
                } else {
                    String error = "";
                    try { if (response.errorBody() != null) error = response.errorBody().string(); } catch (Exception ignored) {}
                    Log.e("AddVisite", "Erreur " + response.code() + ": " + error);
                    Toast.makeText(getContext(), "Erreur serveur (" + response.code() + ")", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Visite> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Erreur de connexion", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
