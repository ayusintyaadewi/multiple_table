package com.example.asus.tugas_3.Util;

public class Config {
    public static final String DATABASE_NAME = "perpustakaan-db";

    //column names of student table
    public static final String TABLE_ANGGOTA = "anggota";
    public static final String COLUMN_ANGGOTA_ID = "_id";
    public static final String COLUMN_ANGGOTA_NAME = "name";
    public static final String COLUMN_ANGGOTA_REGISTRATION = "registration_no";
    public static final String COLUMN_ANGGOTA_PHONE = "phone";
    public static final String COLUMN_ANGGOTA_EMAIL = "email";

    //column names of subject table
    public static final String TABLE_BUKU = "buku";
    public static final String COLUMN_BUKU_ID = "_id";
    public static final String COLUMN_REGISTRATION_NUMBER = "fk_registration_no";
    public static final String COLUMN_BUKU_NAME = "name";
    public static final String COLUMN_BUKU_CODE = "buku_code";
    public static final String COLUMN_BUKU_HARGA = "credit";
    public static final String ANGGOTA_SUB_CONSTRAINT = "anggota_sub_unique";

    //others for general purpose key-value pair data
    public static final String TITLE = "title";
    public static final String CREATE_ANGGOTA = "create_anggota";
    public static final String UPDATE_ANGGOTA = "update_anggota";
    public static final String CREATE_BUKU = "create_buku";
    public static final String UPDATE_BUKU = "update_buku";
    public static final String ANGGOTA_REGISTRATION = "anggota_registration";
}
