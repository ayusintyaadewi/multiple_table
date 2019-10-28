package com.example.asus.tugas_3.Features.BukuCRUD.ShowBukuList;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.asus.tugas_3.R;

public class CustomViewHolder extends RecyclerView.ViewHolder {

    TextView bukuNameTextView;
    TextView bukuCodeTextView;
    TextView bukuCreditTextView;
    ImageView crossButtonImageView;
    ImageView editButtonImageView;

    public CustomViewHolder(View itemView) {
        super(itemView);

        bukuNameTextView = itemView.findViewById(R.id.subjectNameTextView);
        bukuCodeTextView = itemView.findViewById(R.id.subjectCodeTextView);
        bukuCreditTextView = itemView.findViewById(R.id.subjectCreditTextView);
        crossButtonImageView = itemView.findViewById(R.id.crossImageView);
        editButtonImageView = itemView.findViewById(R.id.editImageView);
    }
}
