package com.example.libmanager_btl.dao;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.libmanager_btl.database.DbHelper;
import com.example.libmanager_btl.model.LoaiSach;

import java.util.ArrayList;
import java.util.List;

public class LoaiSachDAO {
    private SQLiteDatabase db;

    public LoaiSachDAO(Context context){
        DbHelper dbHelper = new DbHelper(context);
        db = dbHelper.getWritableDatabase();
    }
    public long insert(LoaiSach obj){
        ContentValues values = new ContentValues();
        values.put("tenLoai", obj.getTenLoai());
        return db.insert("LoaiSach", null, values);
    }
    public int update(LoaiSach obj){
        ContentValues contentValues = new ContentValues();
        contentValues.put("tenLoai", obj.getTenLoai());
        return db.update("LoaiSach", contentValues, "maLoai=?", new String[]{String.valueOf(obj.getMaLoai())});
    }
    public int delete(String id){
        return db.delete("LoaiSach", "maLoai=?", new String[]{id});
    }
    //get data nhiều tham số
    @SuppressLint("Range")
    public List<LoaiSach> getData(String sql, String ...selectionArgs){
        List<LoaiSach> list = new ArrayList<>();
        Cursor c = db.rawQuery(sql, selectionArgs);
        while(c.moveToNext()){
            LoaiSach obj = new LoaiSach();
            obj.setMaLoai(Integer.parseInt(c.getString(c.getColumnIndex("maLoai"))));
            obj.setTenLoai((c.getString(c.getColumnIndex("tenLoai"))));
            list.add(obj);
        }
        c.close();
        return list;
    }
    // get tất cả data
    public List<LoaiSach> getAll(){
        String sql = "select * from LoaiSach";
        return getData(sql);
    }
    // get theo id
    public LoaiSach getId(String id){
        String sql = "select * from LoaiSach where maLoai=?";
        List<LoaiSach> list = getData(sql, id);
      //  return list.get(0);
        if(!list.isEmpty())
        {
            return list.get(0);
        }
        else {
            return null;

        }
    }
}
