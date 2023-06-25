package com.example.libmanager_btl.dao;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.libmanager_btl.database.DbHelper;
import com.example.libmanager_btl.model.Sach;

import java.util.ArrayList;
import java.util.List;

public class SachDAO {
    private SQLiteDatabase db;
    public SachDAO(Context context){
        DbHelper dbHelper = new DbHelper(context);
        db = dbHelper.getWritableDatabase();
    }
    public long insert(Sach obj){
        ContentValues values = new ContentValues();
        values.put("tenSach", obj.getTenSach());
        values.put("giaThue", obj.getGiaThue());
        values.put("maLoai", obj.getMaLoai());
        values.put("soLuong", obj.getSoLuong());
        return db.insert("Sach", null, values);
    }
    public int update(Sach obj){
        ContentValues values = new ContentValues();
        values.put("tenSach", obj.getTenSach());
        values.put("giaThue", obj.getGiaThue());
        values.put("maLoai", obj.getMaLoai());
        values.put("soLuong", obj.getSoLuong());
        return db.update("Sach", values, "maSach=?", new String[]{String.valueOf(obj.getMaSach())});
    }


    public int delete(String id){
        return db.delete("Sach", "maSach=?", new String[]{id});
    }
    //get data nhiều tham số
    @SuppressLint("Range")
    public List<Sach> getData(String sql, String ...selectionArgs){
        List<Sach> list = new ArrayList<>();
        Cursor c = db.rawQuery(sql, selectionArgs);
        while(c.moveToNext()){
            Sach obj = new Sach();
            obj.setMaSach(Integer.parseInt(c.getString(c.getColumnIndex("maSach"))));
            obj.setTenSach(c.getString(c.getColumnIndex("tenSach")));
            obj.setGiaThue(Integer.parseInt(c.getString(c.getColumnIndex("giaThue"))));
            obj.setMaLoai(Integer.parseInt(c.getString(c.getColumnIndex("maLoai"))));
            obj.setSoLuong(Integer.parseInt(c.getString(c.getColumnIndex("soLuong"))));
            list.add(obj);
        }
        c.close();
        return list;
    }
    // get tất cả data
    public List<Sach> getAll(){
        String sql = "select * from Sach";
        return getData(sql);
    }
    // get theo id
    public Sach getId(String id){
        String sql = "select *from Sach where maSach=?";
        List<Sach> list = getData(sql,id);
        return list.get(0);
    }



}
