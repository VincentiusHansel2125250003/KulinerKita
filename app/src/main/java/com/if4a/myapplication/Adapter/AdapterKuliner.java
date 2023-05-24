package com.if4a.myapplication.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.if4a.myapplication.API.APIRequestData;
import com.if4a.myapplication.API.RetroServer;
import com.if4a.myapplication.Activity.MainActivity;
import com.if4a.myapplication.Activity.UbahActivity;
import com.if4a.myapplication.Model.ModelKuliner;
import com.if4a.myapplication.Model.ModelResponse;
import com.if4a.myapplication.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterKuliner extends RecyclerView.Adapter<AdapterKuliner.VHKuliner> {

    private Context ctx;
    private List<ModelKuliner> listKuliner;

    public AdapterKuliner(Context ctx, List<ModelKuliner> listKuliner) {
        this.ctx = ctx;
        this.listKuliner = listKuliner;
    }

    @NonNull
    @Override
    public VHKuliner onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View varView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_kuliner, parent, false);
        return new VHKuliner(varView);
    }

    @Override
    public void onBindViewHolder(@NonNull VHKuliner holder, int position) {
        ModelKuliner MK = listKuliner.get(position);
        holder.tvID.setText(MK.getId());
        holder.tvNama.setText(MK.getNama());
        holder.tvAsal.setText(MK.getAsal());
        holder.tvDeskripsiSingkat.setText(MK.getDeskripsi_singkat());
    }

    @Override
    public int getItemCount() {
        return listKuliner.size();
    }

    public class VHKuliner extends RecyclerView.ViewHolder {

        TextView tvID, tvNama, tvAsal, tvDeskripsiSingkat;

        public VHKuliner(@NonNull View itemView) {
            super(itemView);

            tvID = itemView.findViewById(R.id.tv_id);
            tvNama = itemView.findViewById(R.id.tv_nama);
            tvAsal = itemView.findViewById(R.id.tv_asal);
            tvDeskripsiSingkat = itemView.findViewById(R.id.tv_deskripsi);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    AlertDialog.Builder pesan = new AlertDialog.Builder(ctx);
                    pesan.setTitle("Perhatian");
                    pesan.setMessage("Operasi apa yang akan dilakukan?");
                    pesan.setCancelable(true);

                    pesan.setNegativeButton("Hapus", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            hapusKuliner(tvID.getText().toString());
                            dialog.dismiss();
                        }
                    });

                    pesan.setPositiveButton("Ubah", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent pindah = new Intent(ctx, UbahActivity.class);
                            pindah.putExtra("xId", tvID.getText().toString());
                            pindah.putExtra("xNama", tvNama.getText().toString());
                            pindah.putExtra("xAsal", tvAsal.getText().toString());
                            pindah.putExtra("xDeskripsiSingkat", tvDeskripsiSingkat.getText().toString());
                            ctx.startActivity(pindah);
                        }
                    });

                    pesan.show();
                    return false;
                }
            });
        }

        private void hapusKuliner(String idKuliner) {
            APIRequestData ard = RetroServer.konekRetrofit().create(APIRequestData.class);
            Call<ModelResponse> proses = ard.ardDelete(idKuliner);

            proses.enqueue(new Callback<ModelResponse>() {
                @Override
                public void onResponse(Call<ModelResponse> call, Response<ModelResponse> response) {
                    String kode = response.body().getKode();
                    String pesan = response.body().getPesan();

                    Toast.makeText(ctx, "Kode: " + kode + ", Pesan: " + pesan, Toast.LENGTH_SHORT).show();
                    ((MainActivity) ctx).retrieveKuliner();
                }

                @Override
                public void onFailure(Call<ModelResponse> call, Throwable t) {
                    Toast.makeText(ctx, "Gagal Menghubungi Server!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
