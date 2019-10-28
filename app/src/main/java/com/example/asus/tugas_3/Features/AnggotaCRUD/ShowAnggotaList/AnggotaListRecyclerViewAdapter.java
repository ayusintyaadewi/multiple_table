package com.example.asus.tugas_3.Features.AnggotaCRUD.ShowAnggotaList;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.asus.tugas_3.Database.DatabaseQueryClass;
import com.example.asus.tugas_3.Features.AnggotaCRUD.CreateAnggota.Anggota;
import com.example.asus.tugas_3.Features.AnggotaCRUD.UpdateAnggotaInfo.AnggotaUpdateDialogFragment;
import com.example.asus.tugas_3.Features.AnggotaCRUD.UpdateAnggotaInfo.AnggotaUpdateListener;
import com.example.asus.tugas_3.Features.BukuCRUD.ShowBukuList.BukuListActivity;
import com.example.asus.tugas_3.R;
import com.example.asus.tugas_3.Util.Config;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import java.util.List;

public class AnggotaListRecyclerViewAdapter extends RecyclerView.Adapter<CustomViewHolder> {

    private Context context;
    private List<Anggota> anggotaList;
    private DatabaseQueryClass databaseQueryClass;

    public AnggotaListRecyclerViewAdapter(Context context, List<Anggota> anggotaList) {
        this.context = context;
        this.anggotaList = anggotaList;
        databaseQueryClass = new DatabaseQueryClass(context);
        Logger.addLogAdapter(new AndroidLogAdapter());
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_anggota, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        final int itemPosition = position;
        final Anggota anggota = anggotaList.get(position);

        holder.nameTextView.setText(anggota.getName());
        holder.registrationNumTextView.setText(String.valueOf(anggota.getRegistrationNumber()));
        holder.emailTextView.setText(anggota.getEmail());
        holder.phoneTextView.setText(anggota.getPhoneNumber());

        holder.crossButtonImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setMessage("Yakin menghapus anggota inii?");
                alertDialogBuilder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                deleteAnggota(itemPosition);
                            }
                        });

                alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

        holder.editButtonImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnggotaUpdateDialogFragment anggotaUpdateDialogFragment = AnggotaUpdateDialogFragment.newInstance(anggota.getRegistrationNumber(), itemPosition, new AnggotaUpdateListener() {
                    @Override
                    public void onAnggotaInfoUpdated(Anggota anggota1, int position) {
                        anggotaList.set(position, anggota);
                        notifyDataSetChanged();
                    }
                });
                anggotaUpdateDialogFragment.show(((AnggotaListActivity) context).getSupportFragmentManager(), Config.UPDATE_ANGGOTA);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, BukuListActivity.class);
                intent.putExtra(Config.ANGGOTA_REGISTRATION, anggota.getRegistrationNumber());
                context.startActivity(intent);
            }
        });
    }

    private void deleteAnggota(int position) {
        Anggota anggota = anggotaList.get(position);
        long count = databaseQueryClass.deleteAnggotaByRegNum(anggota.getRegistrationNumber());

        if(count>0){
            anggotaList.remove(position);
            notifyDataSetChanged();
            ((AnggotaListActivity) context).viewVisibility();
            Toast.makeText(context, "Anggota berhasil dihapus", Toast.LENGTH_LONG).show();
        } else
            Toast.makeText(context, "Anggota gagal dihapus", Toast.LENGTH_LONG).show();

    }

    @Override
    public int getItemCount() {
        return anggotaList.size();
    }
}
