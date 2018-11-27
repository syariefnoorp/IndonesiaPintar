package com.example.noor.indonesiapintarapp;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SekolahAjuanList extends ArrayAdapter<Sekolah> {
    private Activity context;
    private List<Sekolah> sekolahAjuanList;
    private List<Sekolah> temp = new ArrayList<>();
    private DatabaseReference databaseRef;
    private DatabaseReference ref;

    public SekolahAjuanList(Activity context, List<Sekolah> sekolahAjuanList){
        super(context,R.layout.list_sekolahajuan,sekolahAjuanList);
        this.context = context;
        this.sekolahAjuanList = sekolahAjuanList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.list_sekolahajuan,null,true);

        databaseRef = FirebaseDatabase.getInstance().getReference("sekolah");
        ref = FirebaseDatabase.getInstance().getReference("sekolah_ajuan");

        ImageView gambarSekolah = (ImageView)listViewItem.findViewById(R.id.imgSekolah);

        final TextView namaSekolah = (TextView)listViewItem.findViewById(R.id.namaSekolah);

        final TextView deskripsiSekolah = (TextView)listViewItem.findViewById(R.id.deskripsiSekolah);
        Button btnValidasi = (Button)listViewItem.findViewById(R.id.btnValidasi);
        Button btnHapus = (Button)listViewItem.findViewById(R.id.btnHapus);

        Sekolah sekolah = sekolahAjuanList.get(position);
        final String idSekolah = sekolah.getIdSekolah();
        final String linkSekolah = sekolah.getGambar();
        namaSekolah.setText(sekolah.getNamaSekolah());
        deskripsiSekolah.setText(sekolah.getDeskripsi());
        Picasso.get().load(sekolah.getGambar()).into(gambarSekolah);

        btnValidasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = databaseRef.push().getKey();
                String nama = namaSekolah.getText().toString().trim();
                String deskripsi = deskripsiSekolah.getText().toString().trim();

                Sekolah sekolah = new Sekolah(id,nama,linkSekolah,deskripsi);

                databaseRef.child(id).setValue(sekolah);
                Query toDelete = ref.orderByChild("idSekolah").equalTo(idSekolah);

                toDelete.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot sekolahAjuanSnapshot : dataSnapshot.getChildren()){
                            sekolahAjuanSnapshot.getRef().removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        btnHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Query toDelete = ref.orderByChild("idSekolah").equalTo(idSekolah);

                toDelete.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot sekolahAjuanSnapshot : dataSnapshot.getChildren()){
                            sekolahAjuanSnapshot.getRef().removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        return listViewItem;
    }
}
