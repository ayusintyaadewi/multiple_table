package com.example.asus.tugas_3.Features.AnggotaCRUD.ShowAnggotaList;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.asus.tugas_3.Database.DatabaseQueryClass;
import com.example.asus.tugas_3.Features.AnggotaCRUD.CreateAnggota.Anggota;
import com.example.asus.tugas_3.Features.AnggotaCRUD.CreateAnggota.AnggotaCreateDialogFragment;
import com.example.asus.tugas_3.Features.AnggotaCRUD.CreateAnggota.AnggotaCreateListener;
import com.example.asus.tugas_3.R;
import com.example.asus.tugas_3.Util.Config;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

public class AnggotaListActivity extends AppCompatActivity implements AnggotaCreateListener {

    private DatabaseQueryClass databaseQueryClass = new DatabaseQueryClass(this);

    private List<Anggota> anggotaList = new ArrayList<>();

    private TextView summaryTextView;
    private TextView anggotaListEmptyTextView;
    private RecyclerView recyclerView;
    private AnggotaListRecyclerViewAdapter anggotaListRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anggota_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Logger.addLogAdapter(new AndroidLogAdapter());

        recyclerView = findViewById(R.id.recyclerView);
        summaryTextView = findViewById(R.id.summaryTextView);
        anggotaListEmptyTextView = findViewById(R.id.emptyListTextView);

        anggotaList.addAll(databaseQueryClass.getAllAnggota());

        anggotaListRecyclerViewAdapter = new AnggotaListRecyclerViewAdapter(this, anggotaList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(anggotaListRecyclerViewAdapter);

        viewVisibility();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAnggotaCreateDialog();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        printSummary();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==R.id.action_delete){

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("Yakin menghapus semua anggota?");
            alertDialogBuilder.setPositiveButton("Yes",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            boolean isAllDeleted = databaseQueryClass.deleteAllAnggotas();
                            if(isAllDeleted){
                                anggotaList.clear();
                                anggotaListRecyclerViewAdapter.notifyDataSetChanged();
                                viewVisibility();
                            }
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

        return super.onOptionsItemSelected(item);
    }

    public void viewVisibility() {
        if(anggotaList.isEmpty())
            anggotaListEmptyTextView.setVisibility(View.VISIBLE);
        else
            anggotaListEmptyTextView.setVisibility(View.GONE);
        printSummary();
    }

    private void openAnggotaCreateDialog() {
        AnggotaCreateDialogFragment anggotaCreateDialogFragment = AnggotaCreateDialogFragment.newInstance("Create Anggota", this);
        anggotaCreateDialogFragment.show(getSupportFragmentManager(), Config.CREATE_ANGGOTA);
    }

    private void printSummary() {
        long anggotaNum = databaseQueryClass.getNumberOfAnggota();
        long bukuNum = databaseQueryClass.getNumberOfBuku();

        summaryTextView.setText(getResources().getString(R.string.database_summary, anggotaNum, bukuNum));
    }

    @Override
    public void onAnggotaCreated(Anggota anggota) {
        anggotaList.add(anggota);
        anggotaListRecyclerViewAdapter.notifyDataSetChanged();
        viewVisibility();
        Logger.d(anggota.getName());
    }
}
