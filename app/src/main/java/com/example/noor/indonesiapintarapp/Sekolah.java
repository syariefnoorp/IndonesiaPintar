package com.example.noor.indonesiapintarapp;

public class Sekolah {
    private String namaSekolah;
    private String gambar;
    private String deskripsi;
    private String idSekolah;

    public Sekolah(){

    }

    public Sekolah(String idSekolah, String namaSekolah, String gambar, String deskripsi) {
        this.namaSekolah = namaSekolah;
        this.gambar = gambar;
        this.deskripsi = deskripsi;
        this.idSekolah = idSekolah;
    }

    public String getIdSekolah() {
        return idSekolah;
    }

    public String getNamaSekolah() {
        return namaSekolah;
    }

    public String getGambar() {
        return gambar;
    }

    public String getDeskripsi() {
        return deskripsi;
    }
}
