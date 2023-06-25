package com.example.libmanager_btl.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "LIB_MANAGER";
    private static final int DB_VERSION = 1;

    private final String CREATE_TABLE_THU_THU = "CREATE TABLE ThuThu (" +
            " maTT TEXT PRIMARY KEY," +
            " hoTen   TEXT NOT NULL," +
            " matKhau TEXT NOT NULL" +
            ")";
    private final String CREATE_TABLE_THANH_VIEN = "CREATE TABLE ThanhVien (" +
            "    maTV    Integer PRIMARY KEY AUTOINCREMENT," +
            "    hoTen   TEXT NOT NULL," +
            "    namSinh TEXT NOT NULL" +
            ")";
    private final String CREATE_TABLE_SACH = "CREATE TABLE Sach (" +
            "    maSach  INTEGER PRIMARY KEY AUTOINCREMENT," +
            "    tenSach TEXT    NOT NULL," +
            "    giaThue INTEGER NOT NULL," +
            "    maLoai  INTEGER REFERENCES LoaiSach (maLoai), " +
            "    soLuong  INTEGER" +
            ")";



    private final String CREATE_TABLE_LOAI_SACH = "CREATE TABLE LoaiSach (" +
            "    maLoai  INTEGER PRIMARY KEY AUTOINCREMENT," +
            "    tenLoai TEXT    NOT NULL" +
            ")";
    private final String CREATE_TABLE_PHIEU_MUON = "CREATE TABLE PhieuMuon (" +
            "    maPM     INTEGER PRIMARY KEY AUTOINCREMENT," +
            "    maTT     TEXT    REFERENCES ThuThu (maTT)," +
            "    maTV     TEXT    REFERENCES ThanhVien (maTV)," +
            "    maSach   INTEGER REFERENCES Sach (maSach)," +
            "    tienThue INTEGER NOT NULL," +
            "    soLuong INTEGER NOT NULL," +
            "    ngay     DATE    NOT NULL," +
            "    traSach  INTEGER NOT NULL" +
            ")";


    public DbHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // create table ThuThu
             db.execSQL(CREATE_TABLE_THU_THU);
        // create table ThanhVien
             db.execSQL(CREATE_TABLE_THANH_VIEN);
        // create table Sach
             db.execSQL(CREATE_TABLE_SACH);
        // create table LoaiSach
             db.execSQL(CREATE_TABLE_LOAI_SACH);
        // create table PhieuMuon
             db.execSQL(CREATE_TABLE_PHIEU_MUON);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String dropTableThuThu = "drop table if exists ThuThu";
        db.execSQL(dropTableThuThu);


        String dropTableThanhVien = "drop table if exists ThanhVien";
        db.execSQL(dropTableThanhVien);

        String dropTableSach = "drop table if exists Sach";
        db.execSQL(dropTableSach);

        String dropTableLoaiSach = "drop table if exists LoaiSach";
        db.execSQL(dropTableLoaiSach);

        String dropTablePhieuMuon = "drop table if exists PhieuMuon";
        db.execSQL(dropTablePhieuMuon);

    }
}
