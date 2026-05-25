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
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;

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

        // Utilisation de JsonObject pour un contrôle total sur la structure
        JsonObject body = new JsonObject();
        body.addProperty("dateVisite", new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(calendar.getTime()));
        body.addProperty("motifVisite", motif);
        
        if (bilan.isEmpty()) {
            body.add("bilanVisite", JsonNull.INSTANCE);
        } else {
            body.addProperty("bilanVisite", bilan);
        }

        body.add("compteRenduVisite", JsonNull.INSTANCE);

        JsonObject visiteurObj = new JsonObject();
        visiteurObj.addProperty("idVisiteur", currentUser.getId());
        visiteurObj.addProperty("nomVisiteur", currentUser.getName());
        body.add("visiteur", visiteurObj);

        JsonObject praticienObj = new JsonObject();
        praticienObj.addProperty("idPraticien", selectedPraticien.getIdPraticien());
        
        if (selectedPraticien.getSpecialite() != null) {
            JsonObject specObj = new JsonObject();
            specObj.addProperty("numeroSequentiel", selectedPraticien.getSpecialite().getNumeroSequentiel());
            praticienObj.add("specialitePraticien", specObj);
        } else {
            // Sécurité : Si on arrive ici, c'est que la spécialité n'a pas été chargée
            Toast.makeText(getContext(), "Erreur : Spécialité du praticien manquante", Toast.LENGTH_SHORT).show();
            return;
        }
        body.add("praticien", praticienObj);

        ApiService apiService = ApiClient.getService(requireContext());
        apiService.createVisite(body).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String msg = response.body().has("message") ? response.body().get("message").getAsString() : "Visite ajoutée !";
                    Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
                    Navigation.findNavController(requireView()).navigateUp();
                } else {
                    String errorMsg = "Erreur lors de l'ajout";
                    try {
                        if (response.errorBody() != null) {
                            String errorStr = response.errorBody().string();
                            JsonObject errorJson = new com.google.gson.Gson().fromJson(errorStr, JsonObject.class);
                            if (errorJson.has("message")) {
                                errorMsg = errorJson.get("message").getAsString();
                            } else if (errorJson.has("error")) {
                                errorMsg = errorJson.get("error").getAsString();
                            }
                        }
                    } catch (Exception ignored) {}

                    if (response.code() == 409 || response.code() == 400) {
                        Toast.makeText(getContext(), errorMsg, Toast.LENGTH_LONG).show();
                    } else {
                        Log.e("AddVisite", "Erreur " + response.code() + ": " + errorMsg);
                        Toast.makeText(getContext(), "Erreur serveur (" + response.code() + ")", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Erreur de connexion", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
