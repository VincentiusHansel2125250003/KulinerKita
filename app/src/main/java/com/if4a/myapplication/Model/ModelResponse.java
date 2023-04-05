package com.if4a.myapplication.Model;

import java.util.List;

public class ModelResponse {
    private String kode, pesan;
    private List<ModelKuliner> data;

    public String getKode() {
        return kode;
    }

    public String getPesan() {
        return pesan;
    }

    public List<ModelKuliner> getData() {
        return data;
    }
}
