package com.example.noor.indonesiapintarapp;

public class Relawan {
    private String idRelawan;
    private String namaRelawan;
    private String sekolahTujuan;
    private String emailRelawan;
    private String noTelpRelawan;

    public Relawan(String idRelawan, String namaRelawan, String sekolahTujuan, String emailRelawan, String noTelpRelawan) {
        this.idRelawan = idRelawan;
        this.namaRelawan = namaRelawan;
        this.sekolahTujuan = sekolahTujuan;
        this.emailRelawan = emailRelawan;
        this.noTelpRelawan = noTelpRelawan;
    }

    public Relawan(){

    }

    public String getIdRelawan() {
        return idRelawan;
    }

    public String getNamaRelawan() {
        return namaRelawan;
    }

    public String getSekolahTujuan() {
        return sekolahTujuan;
    }

    public String getEmailRelawan() {
        return emailRelawan;
    }

    public String getNoTelpRelawan() {
        return noTelpRelawan;
    }
}
