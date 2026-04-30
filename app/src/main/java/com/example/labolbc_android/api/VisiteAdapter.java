package com.example.labolbc_android.api;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.labolbc_android.R;
import com.example.labolbc_android.entity.Visite;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class VisiteAdapter extends RecyclerView.Adapter<VisiteAdapter.VisiteViewHolder> {

    private List<Visite> visites;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    public VisiteAdapter(List<Visite> visites) {
        this.visites = visites;
    }

    @NonNull
    @Override
    public VisiteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_visite, parent, false);
        return new VisiteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VisiteViewHolder holder, int position) {
        Visite visite = visites.get(position);
        holder.tvId.setText(String.valueOf(visite.getId()));
        holder.tvDate.setText(visite.getDateVisite() != null ? dateFormat.format(visite.getDateVisite()) : "-");
        holder.tvMotif.setText(visite.getMotifVisite());
        holder.tvBilan.setText(visite.getBilanVisite());
        holder.tvVisiteur.setText(String.valueOf(visite.getIdVisiteur()));
        holder.tvPraticien.setText(String.valueOf(visite.getIdPraticien()));
    }

    @Override
    public int getItemCount() {
        return visites != null ? visites.size() : 0;
    }

    public void setVisites(List<Visite> visites) {
        this.visites = visites;
        notifyDataSetChanged();
    }

    static class VisiteViewHolder extends RecyclerView.ViewHolder {
        TextView tvId, tvDate, tvMotif, tvBilan, tvVisiteur, tvPraticien;

        public VisiteViewHolder(@NonNull View itemView) {
            super(itemView);
            tvId = itemView.findViewById(R.id.tv_id);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvMotif = itemView.findViewById(R.id.tv_motif);
            tvBilan = itemView.findViewById(R.id.tv_bilan);
            tvVisiteur = itemView.findViewById(R.id.tv_visiteur);
            tvPraticien = itemView.findViewById(R.id.tv_praticien);
        }
    }
}
