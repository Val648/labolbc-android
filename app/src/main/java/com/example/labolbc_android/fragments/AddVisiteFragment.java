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
import java.util.List;
import java.util.Locale;
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

        // Bouton retour
        btnBack.setOnClickListener(v -> Navigation.findNavController(v).navigateUp());

        // Afficher le nom du visiteur
        User user = sessionManager.getUser();
        if (user != null) {
            tvNomVisiteur.setText(user.getName());
        }

        // Date Picker
        etDateVisite.setOnClickListener(v -> showDatePicker());

        // Charger les praticiens
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
                        Toast.makeText(getContext(), "Aucun praticien disponible dans votre région. Accès refusé.", Toast.LENGTH_LONG).show();
                        Navigation.findNavController(requireView()).navigateUp();
                        return;
                    }

                    List<String> names = new ArrayList<>();
                    for (Praticien p : praticienList) {
                        String display = (p.getNomPraticien() != null ? p.getNomPraticien() : "") + 
                                       " " + (p.getPrenomPraticien() != null ? p.getPrenomPraticien() : "");
                        names.add(display.trim().isEmpty() ? "Praticien ": display);
                    }
                    
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, names);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerPraticien.setAdapter(adapter);
                } else {
                    Toast.makeText(getContext(), "Erreur lors de la récupération des praticiens (" + response.code() + "). Page inaccessible.", Toast.LENGTH_LONG).show();
                    Navigation.findNavController(requireView()).navigateUp();
                }
            }

            @Override
            public void onFailure(@NonNull Call<PraticienResponse> call, @NonNull Throwable t) {
                if (!isAdded() || getContext() == null) return;
                Toast.makeText(getContext(), "Erreur réseau : " + t.getMessage() + ". Page inaccessible.", Toast.LENGTH_LONG).show();
                Navigation.findNavController(requireView()).navigateUp();
            }
        });
    }

    private void validerVisite() {
        if (praticienList == null || praticienList.isEmpty()) {
            Toast.makeText(getContext(), "aucun praticien dans votre région", Toast.LENGTH_SHORT).show();
            return;
        }

        String dateStr = etDateVisite.getText().toString().trim();
        String motif = etMotif.getText().toString().trim();
        String bilan = etBilan.getText().toString().trim();

        if (dateStr.isEmpty() || motif.isEmpty() || spinnerPraticien.getSelectedItem() == null) {
            Toast.makeText(getContext(), "Veuillez remplir tous les champs obligatoires (*)", Toast.LENGTH_SHORT).show();
            return;
        }

        Praticien selectedPraticien = praticienList.get(spinnerPraticien.getSelectedItemPosition());

        // Construction du praticien pour l'envoi
        Praticien praticienReq = new Praticien();
        praticienReq.setIdPraticien(selectedPraticien.getIdPraticien());
        
        // On récupère l'ID de la spécialité depuis le praticien sélectionné
        if (selectedPraticien.getSpecialite() != null) {
            Specialite specReq = new Specialite();
            specReq.setId(selectedPraticien.getSpecialite().getId());
            praticienReq.setSpecialite(specReq);
        }

        Visite newVisite = new Visite();
        
        newVisite.setDateVisite(calendar.getTime());
        
        newVisite.setMotifVisite(motif);
        newVisite.setBilanVisite(bilan.isEmpty() ? null : bilan);
        newVisite.setPraticien(praticienReq);

        // On ajoute le visiteur avec idVisiteur
        User currentUser = sessionManager.getUser();
        if (currentUser != null) {
            Visiteur visiteurRequest = new Visiteur();
            visiteurRequest.setIdVisiteur(currentUser.getId());
            newVisite.setVisiteur(visiteurRequest);
        }

        ApiService apiService = ApiClient.getService(requireContext());
        apiService.createVisite(newVisite).enqueue(new Callback<Visite>() {
            @Override
            public void onResponse(@NonNull Call<Visite> call, @NonNull Response<Visite> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Visite ajoutée avec succès", Toast.LENGTH_SHORT).show();
                    Navigation.findNavController(requireView()).navigateUp();
                } else {
                    String errorDetail = "";
                    try {
                        if (response.errorBody() != null) {
                            errorDetail = " : " + response.errorBody().string();
                        }
                    } catch (Exception ignored) {}
                    Toast.makeText(getContext(), "Erreur " + response.code() + errorDetail, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Visite> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Échec de la connexion", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
