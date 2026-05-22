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
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.example.labolbc_android.R;
import com.example.labolbc_android.entity.Praticien;
import com.example.labolbc_android.entity.Specialite;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public class EditVisiteFragment extends Fragment {

    private static final String TAG = "EditVisiteFragment";
    private EditText etVisiteurName, etVisitDate, etMotif, etBilan;
    private Spinner spinnerPraticiens;
    private Button btnSubmit;
    private ImageButton btnBack;

    private int visiteId, visiteurId;
    private int initialIdPraticien = -1;
    private int initialNumeroSequentiel = -1;

    private final List<Praticien> praticiensList = new ArrayList<>();
    private ArrayAdapter<Praticien> praticienAdapter;
    private final SimpleDateFormat displayFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    private final Calendar calendar = Calendar.getInstance();

    private interface InternalService {
        @GET("visiteur/praticiens")
        Call<JsonObject> getPraticiens();
        @GET("visiteur/visites/{id}")
        Call<JsonObject> getVisiteRaw(@Path("id") int id);
        @PUT("visiteur/visites/{id}")
        Call<JsonObject> updateVisiteRaw(@Path("id") int id, @Body JsonObject body);
    }
    private InternalService internalService;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) visiteId = getArguments().getInt("visite_id");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(com.example.labolbc_android.BuildConfig.BASE_URL)
                .client(new okhttp3.OkHttpClient.Builder().addInterceptor(chain -> {
                    String token = new com.example.labolbc_android.SessionManager(getContext()).getToken();
                    return chain.proceed(chain.request().newBuilder().addHeader("Authorization", "Bearer " + token).build());
                }).build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        internalService = retrofit.create(InternalService.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_visite, container, false);
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

        loadData();
        return view;
    }

    private void showDatePicker() {
        new DatePickerDialog(requireContext(), (view, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            etVisitDate.setText(displayFormat.format(calendar.getTime()));
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void loadData() {
        // 1. Charger la visite
        internalService.getVisiteRaw(visiteId).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    JsonObject json = response.body();
                    etMotif.setText(json.has("motifVisite") ? json.get("motifVisite").getAsString() : "");
                    etBilan.setText(json.has("bilanVisite") ? json.get("bilanVisite").getAsString() : "");
                    if (json.has("visiteur")) {
                        JsonObject v = json.getAsJsonObject("visiteur");
                        etVisiteurName.setText(v.has("nomVisiteur") ? v.get("nomVisiteur").getAsString() : "");
                        visiteurId = v.get("idVisiteur").getAsInt();
                    }
                    if (json.has("praticien")) {
                        JsonObject p = json.getAsJsonObject("praticien");
                        initialIdPraticien = p.get("idPraticien").getAsInt();
                        initialNumeroSequentiel = p.get("numeroSequentiel").getAsInt();
                    }
                    if (json.has("dateVisite")) {
                        try {
                            String d = json.get("dateVisite").getAsString();
                            calendar.setTime(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(d.substring(0, 10)));
                            etVisitDate.setText(displayFormat.format(calendar.getTime()));
                        } catch (Exception ignored) {}
                    }
                    updateSelection();
                }
            }
            @Override public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {}
        });

        // 2. Charger les praticiens
        internalService.getPraticiens().enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    praticiensList.clear();
                    JsonArray array = response.body().getAsJsonArray("praticiens");
                    for (JsonElement el : array) {
                        JsonObject obj = el.getAsJsonObject();
                        Praticien p = new Praticien();
                        p.setIdPraticien(obj.get("idPraticien").getAsInt());
                        p.setNomPraticien(obj.get("nom").getAsString());
                        p.setPrenomPraticien(obj.get("prenom").getAsString());
                        if (obj.has("specialite")) {
                            JsonObject s = obj.getAsJsonObject("specialite");
                            p.setSpecialite(new Specialite(s.get("numeroSequentiel").getAsInt(), s.get("libelle").getAsString()));
                        }
                        praticiensList.add(p);
                    }
                    praticienAdapter.notifyDataSetChanged();
                    updateSelection();
                }
            }
            @Override public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {}
        });
    }

    private void updateSelection() {
        if (initialIdPraticien == -1 || praticiensList.isEmpty()) return;
        for (int i = 0; i < praticiensList.size(); i++) {
            Praticien p = praticiensList.get(i);
            if (p.getIdPraticien() == initialIdPraticien && p.getSpecialite().getNumeroSequentiel() == initialNumeroSequentiel) {
                spinnerPraticiens.setSelection(i);
                break;
            }
        }
    }

    private void updateVisite() {
        Praticien p = (Praticien) spinnerPraticiens.getSelectedItem();
        if (p == null) return;

        JsonObject body = new JsonObject();
        body.addProperty("motifVisite", etMotif.getText().toString());
        body.addProperty("dateVisite", new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.getTime()));
        body.addProperty("bilanVisite", etBilan.getText().toString());

        JsonObject praticienObj = new JsonObject();
        praticienObj.addProperty("idPraticien", p.getIdPraticien());
        JsonObject specObj = new JsonObject();
        specObj.addProperty("numeroSequentiel", p.getSpecialite().getNumeroSequentiel());
        praticienObj.add("specialitePraticien", specObj);
        body.add("praticien", praticienObj);

        JsonObject visiteurObj = new JsonObject();
        visiteurObj.addProperty("idVisiteur", visiteurId);
        body.add("visiteur", visiteurObj);

        internalService.updateVisiteRaw(visiteId, body).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Visite mise à jour", Toast.LENGTH_SHORT).show();
                    Navigation.findNavController(requireView()).navigateUp();
                }
            }
            @Override public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {}
        });
    }
}