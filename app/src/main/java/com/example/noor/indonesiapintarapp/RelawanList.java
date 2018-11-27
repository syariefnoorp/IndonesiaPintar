package com.example.noor.indonesiapintarapp;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class RelawanList extends ArrayAdapter<Relawan> {
    private Activity context;
    private List<Relawan> relawanList;

    public RelawanList(Activity context, List<Relawan> relawanList){
        super(context,R.layout.list_relawan,relawanList);
        this.context = context;
        this.relawanList = relawanList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.list_relawan,null,true);

        TextView sekolahTujuan = (TextView)listViewItem.findViewById(R.id.sekolahTujuan);
        TextView namaRelawan = (TextView)listViewItem.findViewById(R.id.namaRelawan);
        TextView emailRelawan = (TextView)listViewItem.findViewById(R.id.emailRelawan);
        TextView nomorTelp = (TextView)listViewItem.findViewById(R.id.nomorTelp);

        Relawan relawan = relawanList.get(position);

        sekolahTujuan.setText(relawan.getSekolahTujuan());
        namaRelawan.setText(relawan.getNamaRelawan());
        emailRelawan.setText(relawan.getEmailRelawan());
        nomorTelp.setText(relawan.getNoTelpRelawan());

        return listViewItem;
    }
}
