package com.example.asus.tugas_3.Features.AnggotaCRUD.UpdateAnggotaInfo;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.asus.tugas_3.Database.DatabaseQueryClass;
import com.example.asus.tugas_3.Features.AnggotaCRUD.CreateAnggota.Anggota;
import com.example.asus.tugas_3.R;
import com.example.asus.tugas_3.Util.Config;

public class AnggotaUpdateDialogFragment extends DialogFragment {

    private static long anggotaRegNo;
    private static int anggotaItemPosition;
    private static AnggotaUpdateListener anggotaUpdateListener;

    private Anggota mAnggota;

    private EditText nameEditText;
    private EditText registrationEditText;
    private EditText phoneEditText;
    private EditText emailEditText;
    private Button updateButton;
    private Button cancelButton;

    private String nameString = "";
    private long registrationNumber = -1;
    private String phoneString = "";
    private String emailString = "";

    private DatabaseQueryClass databaseQueryClass;

    public AnggotaUpdateDialogFragment() {
        // Required empty public constructor
    }

    public static AnggotaUpdateDialogFragment newInstance(long registrationNumber, int position, AnggotaUpdateListener listener){
        anggotaRegNo = registrationNumber;
        anggotaItemPosition = position;
        anggotaUpdateListener = listener;
        AnggotaUpdateDialogFragment anggotaUpdateDialogFragment = new AnggotaUpdateDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", "Update informasi anggota");
        anggotaUpdateDialogFragment.setArguments(args);

        anggotaUpdateDialogFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);

        return anggotaUpdateDialogFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_anggota_update_dialog, container, false);

        databaseQueryClass = new DatabaseQueryClass(getContext());

        nameEditText = view.findViewById(R.id.studentNameEditText);
        registrationEditText = view.findViewById(R.id.registrationEditText);
        phoneEditText = view.findViewById(R.id.phoneEditText);
        emailEditText = view.findViewById(R.id.emailEditText);
        updateButton = view.findViewById(R.id.updateStudentInfoButton);
        cancelButton = view.findViewById(R.id.cancelButton);

        String title = getArguments().getString(Config.TITLE);
        getDialog().setTitle(title);

        mAnggota = databaseQueryClass.getAnggotaByRegNum(anggotaRegNo);

        if(mAnggota!=null){
            nameEditText.setText(mAnggota.getName());
            registrationEditText.setText(String.valueOf(mAnggota.getRegistrationNumber()));
            phoneEditText.setText(mAnggota.getPhoneNumber());
            emailEditText.setText(mAnggota.getEmail());

            updateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    nameString = nameEditText.getText().toString();
                    registrationNumber = Integer.parseInt(registrationEditText.getText().toString());
                    phoneString = phoneEditText.getText().toString();
                    emailString = emailEditText.getText().toString();

                    mAnggota.setName(nameString);
                    mAnggota.setRegistrationNumber(registrationNumber);
                    mAnggota.setPhoneNumber(phoneString);
                    mAnggota.setEmail(emailString);

                    long id = databaseQueryClass.updateAnggotaInfo(mAnggota);

                    if(id>0){
                        anggotaUpdateListener.onAnggotaInfoUpdated(mAnggota, anggotaItemPosition);
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

        }

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
