package com.example.labolbc_android.api;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import com.example.labolbc_android.R;
import com.example.labolbc_android.entity.Visite;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class VisiteAdapter extends RecyclerView.Adapter<VisiteAdapter.VisiteViewHolder> {

    private List<Visite> visites;
    private boolean isPersonalMode;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    private OnDeleteClickListener deleteClickListener;

    public interface OnDeleteClickListener {
        void onDeleteClick(Visite visite);
    }

    public VisiteAdapter(List<Visite> visites) {
        this(visites, false);
    }

    public VisiteAdapter(List<Visite> visites, boolean isPersonalMode) {
        this.visites = visites;
        this.isPersonalMode = isPersonalMode;
    }

    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        this.deleteClickListener = listener;
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
        holder.tvDate.setText(visite.getDateVisite() != null ? dateFormat.format(visite.getDateVisite()) : "-");
        holder.tvMotif.setText(visite.getMotifVisite());
        holder.tvPraticien.setText(visite.getNomPraticien());
        
        // Affichage du bilan textuel dans la partie dépliée
        holder.tvBilanText.setText(visite.getBilanVisite() != null ? visite.getBilanVisite() : "Aucun bilan saisi.");

        // Afficher ou cacher le bouton de téléchargement selon si compteRendu est présent
        if (visite.getCompteRendu() != null && !visite.getCompteRendu().isEmpty()) {
            holder.btnDownloadPdf.setVisibility(View.VISIBLE);
        } else {
            holder.btnDownloadPdf.setVisibility(View.GONE);
        }

        // Mode personnel : Masquer le visiteur, afficher les boutons modifier/supprimer
        if (isPersonalMode) {
            holder.layoutVisiteurInfo.setVisibility(View.GONE);
            holder.layoutEditDelete.setVisibility(View.VISIBLE);
        } else {
            holder.layoutVisiteurInfo.setVisibility(View.VISIBLE);
            holder.tvVisiteur.setText(visite.getNomVisiteur());
            holder.layoutEditDelete.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(v -> {
            boolean isVisible = holder.layoutActions.getVisibility() == View.VISIBLE;
            holder.layoutActions.setVisibility(isVisible ? View.GONE : View.VISIBLE);
            holder.divider.setVisibility(isVisible ? View.GONE : View.VISIBLE);
            holder.tvClickHint.setText(isVisible ? "Cliquez pour voir les actions" : "Cliquez pour masquer les actions");
        });

        holder.btnDownloadPdf.setOnClickListener(v -> {
            Toast.makeText(v.getContext(), "Téléchargement du bilan...", Toast.LENGTH_SHORT).show();
        });
      
        holder.btnEdit.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putInt("visite_id", visite.getId());
            // On peut aussi passer l'ID sous forme de chaîne ou l'objet complet si on veut de l'instantané
            // Mais pour l'instant restons sur l'ID et optimisons le fragment
            Navigation.findNavController(v).navigate(R.id.navigation_edit_visite, bundle);
        });

        holder.btnDelete.setOnClickListener(v -> {
            if (deleteClickListener != null) {
                deleteClickListener.onDeleteClick(visite);
            }
        });
    }

    /*
    private void processPdf(Context context, Visite visite) {
        String pdfUrl = visite.getCompteRendu();
        if (pdfUrl == null || pdfUrl.isEmpty()) return;

        // Si le lien est interne (assets), on garde la logique de copie pour le test
        if (pdfUrl.equals("feuille1.pdf")) {
            copyAssetPdf(context, visite, pdfUrl);
        } else {
            // Sinon, téléchargement normal via internet
            downloadFromWeb(context, visite, pdfUrl);
        }
    }


    private void copyAssetPdf(Context context, Visite visite, String fileName) {
        File outFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "compte_rendu_" + visite.getId() + ".pdf");
        try {
            InputStream in = context.getAssets().open(fileName);
            OutputStream out = new FileOutputStream(outFile);
            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            out.flush();
            out.close();
            openPdf(context, outFile);
        } catch (Exception e) {
            Toast.makeText(context, "Erreur lecture fichier local", Toast.LENGTH_SHORT).show();
        }
    }

    private void downloadFromWeb(Context context, Visite visite, String url) {
        try {
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
            request.setTitle("Compte-Rendu Visite " + visite.getId());
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "compte_rendu_" + visite.getId() + ".pdf");

            DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            if (manager != null) manager.enqueue(request);
            Toast.makeText(context, "Téléchargement du PDF lancé...", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(context, "URL invalide ou erreur réseau", Toast.LENGTH_SHORT).show();
        }
    }

    private void openPdf(Context context, File file) {
        try {
            Uri uri = FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", file);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(uri, "application/pdf");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(context, "Aucune application PDF trouvée", Toast.LENGTH_SHORT).show();
        }
    }*/

    @Override
    public int getItemCount() {
        return visites != null ? visites.size() : 0;
    }

    public void setVisites(List<Visite> visites) {
        this.visites = visites;
        notifyDataSetChanged();
    }

    static class VisiteViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate, tvMotif, tvVisiteur, tvPraticien, tvClickHint, tvBilanText;
        LinearLayout layoutBilan, layoutActions, layoutEditDelete, layoutVisiteurInfo;
        Button btnDownloadPdf, btnEdit, btnDelete;
        View divider;

        public VisiteViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvMotif = itemView.findViewById(R.id.tv_motif);
            tvVisiteur = itemView.findViewById(R.id.tv_visiteur);
            tvPraticien = itemView.findViewById(R.id.tv_praticien);
            tvClickHint = itemView.findViewById(R.id.tv_click_hint);
            /*
            tvBilanText = itemView.findViewById(R.id.tv_bilan_text);
            layoutBilan = itemView.findViewById(R.id.layout_bilan);
            */
            layoutActions = itemView.findViewById(R.id.layout_actions);
            layoutEditDelete = itemView.findViewById(R.id.layout_edit_delete);
            layoutVisiteurInfo = itemView.findViewById(R.id.layout_visiteur_info);
            btnDownloadPdf = itemView.findViewById(R.id.btn_download_pdf);
            btnEdit = itemView.findViewById(R.id.btn_edit);
            btnDelete = itemView.findViewById(R.id.btn_delete);
            divider = itemView.findViewById(R.id.divider);
        }
    }
}
