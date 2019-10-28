package com.example.asus.tugas_3.Features.BukuCRUD.CreateBuku;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.asus.tugas_3.Database.DatabaseQueryClass;
import com.example.asus.tugas_3.R;

public class BukuCreateDialogFragment extends DialogFragment {

    private static long anggotaRegistrationNumber;
    private static BukuCreateListener bukuCreateListener;

    private EditText bukuNameEditText;
    private EditText bukuCodeEditText;
    private EditText bukuCreditEditText;
    private Button createButton;
    private Button cancelButton;

    public BukuCreateDialogFragment() {
        // Required empty public constructor
    }

    public static BukuCreateDialogFragment newInstance(long anggotaRegNo, BukuCreateListener listener){
        anggotaRegistrationNumber = anggotaRegNo;
        bukuCreateListener = listener;

        BukuCreateDialogFragment bukuCreateDialogFragment = new BukuCreateDialogFragment();

        bukuCreateDialogFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);

        return bukuCreateDialogFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_buku_create_dialog, container, false);
        getDialog().setTitle(getResources().getString(R.string.add_new_subject));

        bukuNameEditText = view.findViewById(R.id.subjectNameEditText);
        bukuCodeEditText = view.findViewById(R.id.subjectCodeEditText);
        bukuCreditEditText = view.findViewById(R.id.subjectCreditEditText);
        createButton = view.findViewById(R.id.createButton);
        cancelButton = view.findViewById(R.id.cancelButton);

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String subjectName = bukuNameEditText.getText().toString();
                int subjectCode = Integer.parseInt(bukuCodeEditText.getText().toString());
                int subjectCredit = Integer.parseInt(bukuCreditEditText.getText().toString());

                Buku buku = new Buku(-1, subjectName, subjectCode, subjectCredit);

                DatabaseQueryClass databaseQueryClass = new DatabaseQueryClass(getContext());

                long id = databaseQueryClass.insertBuku(buku, anggotaRegistrationNumber);

                if(id>0){
                    buku.setId(id);
                    bukuCreateListener.onSubjectCreated(buku);
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
