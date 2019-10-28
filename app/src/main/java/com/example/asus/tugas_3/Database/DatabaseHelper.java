package com.example.asus.tugas_3.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.asus.tugas_3.Util.Config;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static DatabaseHelper databaseHelper;

    // All Static variables
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = Config.DATABASE_NAME;

    // Constructor
    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Logger.addLogAdapter(new AndroidLogAdapter());
    }

    public static DatabaseHelper getInstance(Context context) {
        if(databaseHelper==null){
            synchronized (DatabaseHelper.class) {
                if(databaseHelper==null)
                    databaseHelper = new DatabaseHelper(context);
            }
        }
        return databaseHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Create tables SQL execution
        String CREATE_ANGGOTA_TABLE = "CREATE TABLE " + Config.TABLE_ANGGOTA + "("
                + Config.COLUMN_ANGGOTA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Config.COLUMN_ANGGOTA_NAME + " TEXT NOT NULL, "
                + Config.COLUMN_ANGGOTA_REGISTRATION + " INTEGER NOT NULL UNIQUE, "
                + Config.COLUMN_ANGGOTA_PHONE + " TEXT, " //nullable
                + Config.COLUMN_ANGGOTA_EMAIL + " TEXT " //nullable
                + ")";

        String CREATE_BUKU_TABLE = "CREATE TABLE " + Config.TABLE_BUKU + "("
                + Config.COLUMN_BUKU_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Config.COLUMN_REGISTRATION_NUMBER + " INTEGER NOT NULL, "
                + Config.COLUMN_BUKU_NAME + " TEXT NOT NULL, "
                + Config.COLUMN_BUKU_CODE + " INTEGER NOT NULL, "
                + Config.COLUMN_BUKU_HARGA + " INTEGER NOT NULL, " //nullable
                + "FOREIGN KEY (" + Config.COLUMN_REGISTRATION_NUMBER + ") REFERENCES " + Config.TABLE_ANGGOTA + "(" + Config.COLUMN_ANGGOTA_REGISTRATION + ") ON UPDATE CASCADE ON DELETE CASCADE, "
                + "CONSTRAINT " + Config.ANGGOTA_SUB_CONSTRAINT + " UNIQUE (" + Config.COLUMN_REGISTRATION_NUMBER + "," + Config.COLUMN_BUKU_CODE + ")"
                + ")";

        db.execSQL(CREATE_ANGGOTA_TABLE);
        db.execSQL(CREATE_BUKU_TABLE);

        Logger.d("DB created!");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + Config.TABLE_ANGGOTA);
        db.execSQL("DROP TABLE IF EXISTS " + Config.TABLE_BUKU);

        // Create tables again
        onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);

        //enable foreign key constraints like ON UPDATE CASCADE, ON DELETE CASCADE
        db.execSQL("PRAGMA foreign_keys=ON;");
    }
}
