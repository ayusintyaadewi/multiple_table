package com.example.asus.tugas_3.Features.BukuCRUD.UpdateBukuList;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.asus.tugas_3.Database.DatabaseQueryClass;
import com.example.asus.tugas_3.Features.BukuCRUD.CreateBuku.Buku;
import com.example.asus.tugas_3.R;
import com.example.asus.tugas_3.Util.Config;

public class BukuUpdateDialogFragment extends DialogFragment {

    private EditText bukuNameEditText;
    private EditText bukuCodeEditText;
    private EditText bukuCreditEditText;
    private Button updateButton;
    private Button cancelButton;

    private static BukuUpdateListener bukuUpdateListener;
    private static long bukuId;
    private static int position;

    private DatabaseQueryClass databaseQueryClass;

    public BukuUpdateDialogFragment() {
        // Required empty public constructor
    }

    public static BukuUpdateDialogFragment newInstance(long subId, int pos, BukuUpdateListener listener){
        bukuId = subId;
        position = pos;
        bukuUpdateListener = listener;

        BukuUpdateDialogFragment subjectUpdateDialogFragment = new BukuUpdateDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", "Update informasi buku");
        subjectUpdateDialogFragment.setArguments(args);

        subjectUpdateDialogFragment.setStyle(android.support.v4.app.DialogFragment.STYLE_NORMAL, R.style.CustomDialog);

        return subjectUpdateDialogFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_buku_update_dialog, container, false);

        bukuNameEditText = view.findViewById(R.id.subjectNameEditText);
        bukuCodeEditText = view.findViewById(R.id.subjectCodeEditText);
        bukuCreditEditText = view.findViewById(R.id.subjectCreditEditText);
        updateButton = view.findViewById(R.id.updateButton);
        cancelButton = view.findViewById(R.id.cancelButton);

        databaseQueryClass = new DatabaseQueryClass(getContext());

        String title = getArguments().getString(Config.TITLE);
        getDialog().setTitle(title);

        Buku buku = databaseQueryClass.getBukuById(bukuId);

        bukuNameEditText.setText(buku.getName());
        bukuCodeEditText.setText(String.valueOf(buku.getCode()));
        bukuCreditEditText.setText(String.valueOf(buku.getCredit()));

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String bukuName = bukuNameEditText.getText().toString();
                int subjectCode = Integer.parseInt(bukuCodeEditText.getText().toString());
                int subjectCredit = Integer.parseInt(bukuCreditEditText.getText().toString());

                Buku buku = new Buku(bukuId, bukuName, subjectCode, subjectCredit);

                long rowCount = databaseQueryClass.updateBukuInfo(buku);

                if(rowCount>0){
                    bukuUpdateListener.onBukuInfoUpdate(buku, position);
                    getDialog().dismiss();
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            //noinspection ConstantConditions
            dialog.getWindow().setLayout(width, height);
        }
    }
}
