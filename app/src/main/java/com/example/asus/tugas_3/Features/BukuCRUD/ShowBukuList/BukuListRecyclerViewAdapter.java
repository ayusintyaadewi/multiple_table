package com.example.asus.tugas_3.Features.BukuCRUD.ShowBukuList;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.asus.tugas_3.Database.DatabaseQueryClass;
import com.example.asus.tugas_3.Features.BukuCRUD.CreateBuku.Buku;
import com.example.asus.tugas_3.Features.BukuCRUD.UpdateBukuList.BukuUpdateDialogFragment;
import com.example.asus.tugas_3.Features.BukuCRUD.UpdateBukuList.BukuUpdateListener;
import com.example.asus.tugas_3.R;
import com.example.asus.tugas_3.Util.Config;

import java.util.List;

public class BukuListRecyclerViewAdapter extends RecyclerView.Adapter<CustomViewHolder> {

    private Context context;
    private List<Buku> bukuList;

    public BukuListRecyclerViewAdapter(Context context, List<Buku> bukuList) {
        this.context = context;
        this.bukuList = bukuList;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_buku, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        final int listPosition = position;
        final Buku buku = bukuList.get(position);

        holder.bukuNameTextView.setText(buku.getName());
        holder.bukuCodeTextView.setText(String.valueOf(buku.getCode()));
        holder.bukuCreditTextView.setText(String.valueOf(buku.getCredit()));

        holder.crossButtonImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setMessage("Yakin menghapus buku ini?");
                alertDialogBuilder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                deleteSubject(buku);
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
                editBuku(buku.getId(), listPosition);
            }
        });
    }

    private void editBuku(long subjectId, int listPosition){
        BukuUpdateDialogFragment bukuUpdateDialogFragment = BukuUpdateDialogFragment.newInstance(subjectId, listPosition, new BukuUpdateListener() {
            @Override
            public void onBukuInfoUpdate(Buku buku, int position) {
                bukuList.set(position, buku);
                notifyDataSetChanged();
            }
        });
       bukuUpdateDialogFragment.show(((BukuListActivity) context).getSupportFragmentManager(), Config.UPDATE_BUKU);
    }

    private void deleteSubject(Buku buku) {
        DatabaseQueryClass databaseQueryClass = new DatabaseQueryClass(context);
        boolean isDeleted = databaseQueryClass.deleteBukuById(buku.getId());

        if(isDeleted) {
            bukuList.remove(buku);
            notifyDataSetChanged();
            ((BukuListActivity) context).viewVisibility();
        } else
            Toast.makeText(context, "Cannot delete!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getItemCount() {
        return bukuList.size();
    }
}
