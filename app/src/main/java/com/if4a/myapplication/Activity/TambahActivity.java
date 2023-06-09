package com.if4a.myapplication.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.if4a.myapplication.API.APIRequestData;
import com.if4a.myapplication.API.RetroServer;
import com.if4a.myapplication.Model.ModelResponse;
import com.if4a.myapplication.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TambahActivity extends AppCompatActivity {
    private EditText etNama, etAsal, etDeskripsiSingkat;
    private Button btnSimpan;
    private String nama, asal, deskripsiSingkat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah);

        etNama = findViewById(R.id.et_nama);
        etAsal = findViewById(R.id.et_asal);
        etDeskripsiSingkat = findViewById(R.id.et_deskripsi_singkat);
        btnSimpan = findViewById(R.id.btn_simpan);

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nama = etNama.getText().toString();
                asal = etAsal.getText().toString();
                deskripsiSingkat = etDeskripsiSingkat.getText().toString();

                if (nama.trim().isEmpty()) {
                    etNama.setError("Nama Tidak Boleh Kosong");
                } else if (asal.trim().isEmpty()) {
                    etAsal.setError("Asal Tidak Boleh Kosong");
                } else if (deskripsiSingkat.isEmpty()) {
                    etDeskripsiSingkat.setError("Deskripsi Singkat Tidak Boleh Kosong");
                } else {
                    tambahKuliner();
                }
            }
        });
    }

    private void tambahKuliner(){
        APIRequestData ard = RetroServer.konekRetrofit().create(APIRequestData.class);
        Call<ModelResponse> proses = ard.ardCreate(nama, asal, deskripsiSingkat);

        proses.enqueue(new Callback<ModelResponse>() {
            @Override
            public void onResponse(Call<ModelResponse> call, Response<ModelResponse> response) {
                String kode = response.body().getKode();
                String pesan = response.body().getPesan();

                Toast.makeText(TambahActivity.this, "Kode: " + kode + ", Pesan: " + pesan, Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(Call<ModelResponse> call, Throwable t) {
                Toast.makeText(TambahActivity.this, "Gagal Menghubungi Server", Toast.LENGTH_SHORT).show();
            }
        });
    }
}