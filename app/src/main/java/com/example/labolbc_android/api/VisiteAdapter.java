package com.example.labolbc_android.api;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;
import com.example.labolbc_android.R;
import com.example.labolbc_android.entity.Visite;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
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
        holder.tvDate.setText(visite.getDateVisite() != null ? dateFormat.format(visite.getDateVisite()) : "-");
        holder.tvMotif.setText(visite.getMotifVisite());
        holder.tvVisiteur.setText(visite.getNomVisiteur());
        holder.tvPraticien.setText(visite.getNomPraticien());

        holder.itemView.setOnClickListener(v -> {
            boolean isVisible = holder.layoutBilan.getVisibility() == View.VISIBLE;
            holder.layoutBilan.setVisibility(isVisible ? View.GONE : View.VISIBLE);
            holder.divider.setVisibility(isVisible ? View.GONE : View.VISIBLE);
            holder.tvClickHint.setText(isVisible ? "Cliquez pour voir le bilan" : "Cliquez pour masquer le bilan");
        });

        holder.btnDownloadPdf.setOnClickListener(v -> {
            processPdf(v.getContext(), visite);
        });
    }

    private void processPdf(Context context, Visite visite) {
        String assetFileName = "feuille1.pdf";
        File outFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "bilan_visite_" + visite.getId() + ".pdf");

        try {
            // 1. Copy file from assets to Downloads
            InputStream in = context.getAssets().open(assetFileName);
            OutputStream out = new FileOutputStream(outFile);
            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            out.flush();
            out.close();

            // 2. Open the file
            openPdf(context, outFile);

        } catch (Exception e) {
            Log.e("VisiteAdapter", "Erreur: " + e.getMessage());
            fallbackToWebDownload(context, visite);
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
            Toast.makeText(context, "Aucune application trouvée pour ouvrir le PDF", Toast.LENGTH_LONG).show();
        }
    }

    private void fallbackToWebDownload(Context context, Visite visite) {
        String dummyUrl = "https://www.w3.org/WAI/ER/tests/xhtml/testfiles/resources/pdf/dummy.pdf";
        try {
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(dummyUrl));
            request.setTitle("Bilan Visite " + visite.getId());
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "bilan_web_" + visite.getId() + ".pdf");

            DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            if (manager != null) manager.enqueue(request);
            Toast.makeText(context, "Téléchargement web lancé...", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(context, "Impossible de récupérer le PDF", Toast.LENGTH_SHORT).show();
        }
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
        TextView tvDate, tvMotif, tvVisiteur, tvPraticien, tvClickHint;
        LinearLayout layoutBilan;
        Button btnDownloadPdf;
        View divider;

        public VisiteViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvMotif = itemView.findViewById(R.id.tv_motif);
            tvVisiteur = itemView.findViewById(R.id.tv_visiteur);
            tvPraticien = itemView.findViewById(R.id.tv_praticien);
            tvClickHint = itemView.findViewById(R.id.tv_click_hint);
            layoutBilan = itemView.findViewById(R.id.layout_bilan);
            btnDownloadPdf = itemView.findViewById(R.id.btn_download_pdf);
            divider = itemView.findViewById(R.id.divider);
        }
    }
}
