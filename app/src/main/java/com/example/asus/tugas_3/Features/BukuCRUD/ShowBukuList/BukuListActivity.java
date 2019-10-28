package com.example.asus.tugas_3.Features.BukuCRUD.ShowBukuList;

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
import com.example.asus.tugas_3.Features.BukuCRUD.CreateBuku.Buku;
import com.example.asus.tugas_3.Features.BukuCRUD.CreateBuku.BukuCreateDialogFragment;
import com.example.asus.tugas_3.Features.BukuCRUD.CreateBuku.BukuCreateListener;
import com.example.asus.tugas_3.R;
import com.example.asus.tugas_3.Util.Config;

import java.util.ArrayList;
import java.util.List;

public class BukuListActivity extends AppCompatActivity implements BukuCreateListener {

    private long anggotaRegNo;

    private DatabaseQueryClass databaseQueryClass = new DatabaseQueryClass(this);

    private List<Buku> bukuList = new ArrayList<>();

    private TextView summaryTextView;
    private TextView bukuListEmptyTextView;
    private RecyclerView recyclerView;
    private BukuListRecyclerViewAdapter bukuListRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buku_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.recyclerView);
        summaryTextView = findViewById(R.id.summaryTextView);
        bukuListEmptyTextView = findViewById(R.id.emptyListTextView);

        anggotaRegNo = getIntent().getLongExtra(Config.ANGGOTA_REGISTRATION, -1);

        bukuList.addAll(databaseQueryClass.getAllBukusByRegNo(anggotaRegNo));

        bukuListRecyclerViewAdapter = new BukuListRecyclerViewAdapter(this, bukuList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(bukuListRecyclerViewAdapter);

        viewVisibility();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openBukuCreateDialog();
            }
        });
    }

    private void printSummary() {
        long anggotaNum = databaseQueryClass.getNumberOfAnggota();
        long bukuNum = databaseQueryClass.getNumberOfBuku();

        summaryTextView.setText(getResources().getString(R.string.database_summary, anggotaNum, bukuNum));
    }

    private void openBukuCreateDialog() {
        BukuCreateDialogFragment bukuCreateDialogFragment = BukuCreateDialogFragment.newInstance(anggotaRegNo, this);
        bukuCreateDialogFragment.show(getSupportFragmentManager(), Config.CREATE_BUKU);
    }

    @Override
    public void onSubjectCreated(Buku buku) {
        bukuList.add(buku);
        bukuListRecyclerViewAdapter.notifyDataSetChanged();
        viewVisibility();
    }

    public void viewVisibility() {
        if(bukuList.isEmpty())
            bukuListEmptyTextView.setVisibility(View.VISIBLE);
        else
            bukuListEmptyTextView.setVisibility(View.GONE);
        printSummary();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_delete:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setMessage("Yakin menghapus semua buku?");
                alertDialogBuilder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                boolean isAllDeleted = databaseQueryClass.deleteAllBukusByRegNum(anggotaRegNo);
                                if(isAllDeleted){
                                    bukuList.clear();
                                    bukuListRecyclerViewAdapter.notifyDataSetChanged();
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
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }

}
