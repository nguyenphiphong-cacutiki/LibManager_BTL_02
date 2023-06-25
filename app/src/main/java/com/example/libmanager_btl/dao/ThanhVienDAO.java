package com.example.libmanager_btl.dao;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.libmanager_btl.database.DbHelper;
import com.example.libmanager_btl.model.ThanhVien;

import java.util.ArrayList;
import java.util.List;

public class ThanhVienDAO {
    private SQLiteDatabase db;

    public ThanhVienDAO(Context context) {
        DbHelper dbHelper = new DbHelper(context);
        db = dbHelper.getWritableDatabase();
    }
    public long insert(ThanhVien obj){
        ContentValues contentValues = new ContentValues();
//        contentValues.put("maTV", obj.getMaTV());
        contentValues.put("hoTen", obj.getHoTen());
        contentValues.put("namSinh", obj.getNamSinh());
        return db.insert("ThanhVien", null, contentValues);

    }
    public int update(ThanhVien obj){
        ContentValues values = new ContentValues();
        values.put("maTV", obj.getMaTV());
        values.put("hoTen", obj.getHoTen());
        values.put("namSinh", obj.getNamSinh());
        return db.update("ThanhVien", values, "maTV = ?", new String[]{String.valueOf(obj.getMaTV())});
    }
    public int delete(String id){
        return db.delete("ThanhVien", "maTV=?", new String[]{id});
    }
    @SuppressLint("Range")
    public List<ThanhVien> getData(String sql, String...selectionArgs){
        List<ThanhVien> list = new ArrayList<>();
        Cursor c = db.rawQuery(sql, selectionArgs);
        while(c.moveToNext()){
            ThanhVien obj = new ThanhVien();
//            Log.d("@@@-***", c.getColumnIndex("maTV")+" - columnIndex");
//            Log.d("@@@-***", c.getString(0));

            obj.setMaTV(Integer.parseInt(c.getString(0)));
            obj.setHoTen(c.getString(c.getColumnIndex("hoTen")));
            obj.setNamSinh(c.getString(c.getColumnIndex("namSinh")));

            list.add(obj);
        }
        c.close();
        return list;
    }
    public List<ThanhVien> getAll(){
        String sql = "select * from ThanhVien";
        return getData(sql);
    }
    public ThanhVien getWithID(String id){
        String sql = "select * from ThanhVien where maTV=?";
        List<ThanhVien> list = getData(sql, id);
        if(list.size() > 0) return list.get(0);
        return null;
    }
}
