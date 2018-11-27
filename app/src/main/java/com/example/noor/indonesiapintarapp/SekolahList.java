package com.example.noor.indonesiapintarapp;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class SekolahList extends ArrayAdapter<Sekolah> {
    private Activity context;
    private List<Sekolah> sekolahList;

    public SekolahList(Activity context, List<Sekolah> sekolahList){
        super(context,R.layout.list_sekolah,sekolahList);
        this.context = context;
        this.sekolahList = sekolahList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.list_sekolah,null,true);

        TextView namaSekolah = (TextView)listViewItem.findViewById(R.id.namaSekolah);
        ImageView gambarSekolah = (ImageView)listViewItem.findViewById(R.id.imgSekolah);

        Sekolah sekolah = sekolahList.get(position);

        namaSekolah.setText(sekolah.getNamaSekolah());
        Picasso.get().load(sekolah.getGambar()).into(gambarSekolah);

        return listViewItem;
    }
}
