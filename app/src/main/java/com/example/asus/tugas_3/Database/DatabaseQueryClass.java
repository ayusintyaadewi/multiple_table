package com.example.asus.tugas_3.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.widget.Toast;

import com.example.asus.tugas_3.Features.AnggotaCRUD.CreateAnggota.Anggota;
import com.example.asus.tugas_3.Features.BukuCRUD.CreateBuku.Buku;
import com.example.asus.tugas_3.Util.Config;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DatabaseQueryClass {

    private Context context;

    public DatabaseQueryClass(Context context){
        this.context = context;
        Logger.addLogAdapter(new AndroidLogAdapter());
    }

    public long insertAnggota(Anggota anggota){

        long id = -1;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Config.COLUMN_ANGGOTA_NAME, anggota.getName());
        contentValues.put(Config.COLUMN_ANGGOTA_REGISTRATION, anggota.getRegistrationNumber());
        contentValues.put(Config.COLUMN_ANGGOTA_PHONE, anggota.getPhoneNumber());
        contentValues.put(Config.COLUMN_ANGGOTA_EMAIL, anggota.getEmail());

        try {
            id = sqLiteDatabase.insertOrThrow(Config.TABLE_ANGGOTA, null, contentValues);
        } catch (SQLiteException e){
            Logger.d("Exception: " + e.getMessage());
            Toast.makeText(context, "Operation failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            sqLiteDatabase.close();
        }

        return id;
    }

    public List<Anggota> getAllAnggota(){

        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();

        Cursor cursor = null;
        try {

            cursor = sqLiteDatabase.query(Config.TABLE_ANGGOTA, null, null, null, null, null, null, null);

            /**
             // If you want to execute raw query then uncomment below 2 lines. And comment out above line.

             String SELECT_QUERY = String.format("SELECT %s, %s, %s, %s, %s FROM %s", Config.COLUMN_STUDENT_ID, Config.COLUMN_STUDENT_NAME, Config.COLUMN_STUDENT_REGISTRATION, Config.COLUMN_STUDENT_EMAIL, Config.COLUMN_STUDENT_PHONE, Config.TABLE_STUDENT);
             cursor = sqLiteDatabase.rawQuery(SELECT_QUERY, null);
             */

            if(cursor!=null)
                if(cursor.moveToFirst()){
                    List<Anggota> anggotaList = new ArrayList<>();
                    do {
                        int id = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_ANGGOTA_ID));
                        String name = cursor.getString(cursor.getColumnIndex(Config.COLUMN_ANGGOTA_NAME));
                        long registrationNumber = cursor.getLong(cursor.getColumnIndex(Config.COLUMN_ANGGOTA_REGISTRATION));
                        String email = cursor.getString(cursor.getColumnIndex(Config.COLUMN_ANGGOTA_EMAIL));
                        String phone = cursor.getString(cursor.getColumnIndex(Config.COLUMN_ANGGOTA_PHONE));

                        anggotaList.add(new Anggota(id, name, registrationNumber, email, phone));
                    }   while (cursor.moveToNext());

                    return anggotaList;
                }
        } catch (Exception e){
            Logger.d("Exception: " + e.getMessage());
            Toast.makeText(context, "Operation failed", Toast.LENGTH_SHORT).show();
        } finally {
            if(cursor!=null)
                cursor.close();
            sqLiteDatabase.close();
        }

        return Collections.emptyList();
    }

    public Anggota getAnggotaByRegNum(long registrationNum){

        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();

        Cursor cursor = null;
        Anggota anggota = null;
        try {

            cursor = sqLiteDatabase.query(Config.TABLE_ANGGOTA, null,
                    Config.COLUMN_ANGGOTA_REGISTRATION + " = ? ", new String[]{String.valueOf(registrationNum)},
                    null, null, null);

            /**
             // If you want to execute raw query then uncomment below 2 lines. And comment out above sqLiteDatabase.query() method.

             String SELECT_QUERY = String.format("SELECT * FROM %s WHERE %s = %s", Config.TABLE_STUDENT, Config.COLUMN_STUDENT_REGISTRATION, String.valueOf(registrationNum));
             cursor = sqLiteDatabase.rawQuery(SELECT_QUERY, null);
             */

            if(cursor.moveToFirst()){
                int id = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_ANGGOTA_ID));
                String name = cursor.getString(cursor.getColumnIndex(Config.COLUMN_ANGGOTA_NAME));
                long registrationNumber = cursor.getLong(cursor.getColumnIndex(Config.COLUMN_ANGGOTA_REGISTRATION));
                String phone = cursor.getString(cursor.getColumnIndex(Config.COLUMN_ANGGOTA_PHONE));
                String email = cursor.getString(cursor.getColumnIndex(Config.COLUMN_ANGGOTA_EMAIL));

                anggota = new Anggota(id, name, registrationNumber, phone, email);
            }
        } catch (Exception e){
            Logger.d("Exception: " + e.getMessage());
            Toast.makeText(context, "Operation failed", Toast.LENGTH_SHORT).show();
        } finally {
            if(cursor!=null)
                cursor.close();
            sqLiteDatabase.close();
        }

        return anggota;
    }

    public long updateAnggotaInfo(Anggota anggota){

        long rowCount = 0;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Config.COLUMN_ANGGOTA_NAME, anggota.getName());
        contentValues.put(Config.COLUMN_ANGGOTA_REGISTRATION, anggota.getRegistrationNumber());
        contentValues.put(Config.COLUMN_ANGGOTA_PHONE, anggota.getPhoneNumber());
        contentValues.put(Config.COLUMN_ANGGOTA_EMAIL, anggota.getEmail());

        try {
            rowCount = sqLiteDatabase.update(Config.TABLE_ANGGOTA, contentValues,
                    Config.COLUMN_ANGGOTA_ID + " = ? ",
                    new String[] {String.valueOf(anggota.getId())});
        } catch (SQLiteException e){
            Logger.d("Exception: " + e.getMessage());
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            sqLiteDatabase.close();
        }

        return rowCount;
    }

    public long deleteAnggotaByRegNum(long registrationNum) {
        long deletedRowCount = -1;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        try {
            deletedRowCount = sqLiteDatabase.delete(Config.TABLE_ANGGOTA,
                    Config.COLUMN_ANGGOTA_REGISTRATION + " = ? ",
                    new String[]{ String.valueOf(registrationNum)});
        } catch (SQLiteException e){
            Logger.d("Exception: " + e.getMessage());
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            sqLiteDatabase.close();
        }

        return deletedRowCount;
    }

    public boolean deleteAllAnggotas(){
        boolean deleteStatus = false;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        try {
            //for "1" delete() method returns number of deleted rows
            //if you don't want row count just use delete(TABLE_NAME, null, null)
            sqLiteDatabase.delete(Config.TABLE_ANGGOTA, null, null);

            long count = DatabaseUtils.queryNumEntries(sqLiteDatabase, Config.TABLE_ANGGOTA);

            if(count==0)
                deleteStatus = true;

        } catch (SQLiteException e){
            Logger.d("Exception: " + e.getMessage());
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            sqLiteDatabase.close();
        }

        return deleteStatus;
    }

    public long getNumberOfAnggota(){
        long count = -1;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        try {
            count = DatabaseUtils.queryNumEntries(sqLiteDatabase, Config.TABLE_ANGGOTA);
        } catch (SQLiteException e){
            Logger.d("Exception: " + e.getMessage());
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            sqLiteDatabase.close();
        }

        return count;
    }

    // buku
    public long insertBuku(Buku buku, long registrationNo){
        long rowId = -1;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Config.COLUMN_BUKU_NAME, buku.getName());
        contentValues.put(Config.COLUMN_BUKU_CODE, buku.getCode());
        contentValues.put(Config.COLUMN_BUKU_HARGA, buku.getCredit());
        contentValues.put(Config.COLUMN_REGISTRATION_NUMBER, registrationNo);

        try {
            rowId = sqLiteDatabase.insertOrThrow(Config.TABLE_BUKU, null, contentValues);
        } catch (SQLiteException e){
            Logger.d(e);
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            sqLiteDatabase.close();
        }

        return rowId;
    }

    public Buku getBukuById(long bukuId){
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();

        Buku buku = null;

        Cursor cursor = null;
        try{
            cursor = sqLiteDatabase.query(Config.TABLE_BUKU, null,
                    Config.COLUMN_BUKU_ID + " = ? ", new String[] {String.valueOf(bukuId)},
                    null, null, null);

            if(cursor!=null && cursor.moveToFirst()){
                String subjectName = cursor.getString(cursor.getColumnIndex(Config.COLUMN_BUKU_NAME));
                int subjectCode = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_BUKU_CODE));
                int subjectCredit = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_BUKU_HARGA));

                buku = new Buku(bukuId, subjectName, subjectCode, subjectCredit);
            }
        } catch (SQLiteException e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            if(cursor!=null)
                cursor.close();
            sqLiteDatabase.close();
        }

        return buku;
    }

    public long updateBukuInfo(Buku buku){

        long rowCount = 0;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Config.COLUMN_BUKU_NAME, buku.getName());
        contentValues.put(Config.COLUMN_BUKU_CODE, buku.getCode());
        contentValues.put(Config.COLUMN_BUKU_HARGA, buku.getCredit());

        try {
            rowCount = sqLiteDatabase.update(Config.TABLE_BUKU, contentValues,
                    Config.COLUMN_BUKU_ID + " = ? ",
                    new String[] {String.valueOf(buku.getId())});
        } catch (SQLiteException e){
            Logger.d("Exception: " + e.getMessage());
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            sqLiteDatabase.close();
        }

        return rowCount;
    }

    public List<Buku> getAllBukusByRegNo(long registrationNo){
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();

        List<Buku> bukuList = new ArrayList<>();
        Cursor cursor = null;
        try{
            cursor = sqLiteDatabase.query(Config.TABLE_BUKU,
                    new String[] {Config.COLUMN_BUKU_ID, Config.COLUMN_BUKU_NAME, Config.COLUMN_BUKU_CODE, Config.COLUMN_BUKU_HARGA},
                    Config.COLUMN_REGISTRATION_NUMBER + " = ? ",
                    new String[] {String.valueOf(registrationNo)},
                    null, null, null);

            if(cursor!=null && cursor.moveToFirst()){
                bukuList = new ArrayList<>();
                do {
                    int id = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_BUKU_ID));
                    String subjectName = cursor.getString(cursor.getColumnIndex(Config.COLUMN_BUKU_NAME));
                    int subjectCode = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_BUKU_CODE));
                    int subjectCredit = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_BUKU_HARGA));

                    bukuList.add(new Buku(id, subjectName, subjectCode, subjectCredit));
                } while (cursor.moveToNext());
            }
        } catch (SQLiteException e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            if(cursor!=null)
                cursor.close();
            sqLiteDatabase.close();
        }

        return bukuList;
    }

    public boolean deleteBukuById(long bukuId) {
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        int row = sqLiteDatabase.delete(Config.TABLE_BUKU,
                Config.COLUMN_BUKU_ID + " = ? ", new String[]{String.valueOf(bukuId)});

        return row > 0;
    }

    public boolean deleteAllBukusByRegNum(long registrationNum) {
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        int row = sqLiteDatabase.delete(Config.TABLE_BUKU,
                Config.COLUMN_REGISTRATION_NUMBER + " = ? ", new String[]{String.valueOf(registrationNum)});

        return row > 0;
    }

    public long getNumberOfBuku(){
        long count = -1;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        try {
            count = DatabaseUtils.queryNumEntries(sqLiteDatabase, Config.TABLE_BUKU);
        } catch (SQLiteException e){
            Logger.d("Exception: " + e.getMessage());
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            sqLiteDatabase.close();
        }

        return count;
    }
}
