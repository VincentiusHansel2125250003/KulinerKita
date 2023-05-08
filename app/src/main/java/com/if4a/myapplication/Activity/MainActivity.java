package com.if4a.myapplication.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.if4a.myapplication.API.APIRequestData;
import com.if4a.myapplication.API.RetroServer;
import com.if4a.myapplication.Adapter.AdapterKuliner;
import com.if4a.myapplication.Model.ModelKuliner;
import com.if4a.myapplication.Model.ModelResponse;
import com.if4a.myapplication.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private RecyclerView rvKuliner;
    private FloatingActionButton fabTambah;
    private ProgressBar pbKuliner;
    private RecyclerView.Adapter adKuliner;
    private RecyclerView.LayoutManager lmKuliner;
    private List<ModelKuliner> listKuliner = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvKuliner = findViewById(R.id.rv_kuliner);
        fabTambah = findViewById(R.id.fab_tambah);
        pbKuliner = findViewById(R.id.pb_kuliner);

        lmKuliner = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvKuliner.setLayoutManager(lmKuliner);
    }

    @Override
    protected void onResume() {
        super.onResume();
        retrieveKuliner();
    }

    public void retrieveKuliner(){
        pbKuliner.setVisibility(View.VISIBLE);

        APIRequestData ARD = new APIRequestData() {
            @Override
            public Call<ModelResponse> ardRetrieve() {

                APIRequestData ARD = RetroServer.konekRetrofit().create(APIRequestData.class);
                Call<ModelResponse> proses = ARD.ardRetrieve();

                proses.enqueue(new Callback<ModelResponse>() {
                    @Override
                    public void onResponse(Call<ModelResponse> call, Response<ModelResponse> response) {
                        String kode = response.body().getKode();
                        String pesan = response.body().getPesan();
                        listKuliner = response.body().getData();

                        adKuliner = new AdapterKuliner(MainActivity.this, listKuliner);
                        rvKuliner.setAdapter(adKuliner);
                        adKuliner.notifyDataSetChanged();

                        pbKuliner.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(Call<ModelResponse> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "Gagal Menghubungi Server", Toast.LENGTH_SHORT).show();
                        pbKuliner.setVisibility(View.GONE);
                    }
                });
            }

            @Override
            public Call<ModelResponse> ardCreate(String nama, String asal, String deskripsi_singkat) {
                return null;
            }

            @Override
            public Call<ModelResponse> ardUpdate(String id, String nama, String asal, String deskripsi_singkat) {
                return null;
            }

            @Override
            public Call<ModelResponse> ardDelete(String id) {
                return null;
            }
        }
    }

}