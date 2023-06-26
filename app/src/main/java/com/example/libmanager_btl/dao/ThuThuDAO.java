package com.example.libmanager_btl.dao;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.example.libmanager_btl.database.DbHelper;
import com.example.libmanager_btl.model.ThuThu;

import java.util.ArrayList;
import java.util.List;

public class ThuThuDAO {
    private SQLiteDatabase db;
    public ThuThuDAO(Context context){
        DbHelper dbHelper = new DbHelper(context);
        db = dbHelper.getWritableDatabase();
    }
    public long insert(ThuThu obj){
        ContentValues values = new ContentValues();
        values.put("maTT", obj.getMaTT());
        values.put("hoTen", obj.getHoTen());
        values.put("matKhau", obj.getMatKhau());
        return db.insert("ThuThu", null, values);
    }
    public int updateInfo(ThuThu old, ThuThu mNew){
        ThuThu a = getWithID(mNew.getMaTT());
        if(a != null){
            //  tồn tại mã thủ thư trùng
            // không thực hiện update

            return -1;
        }else {
            insert(mNew);
            delete(old.getMaTT());
            return 1;
        }
    }
    public int updatePass(ThuThu obj){
        ContentValues values = new ContentValues();
        values.put("hoTen", obj.getHoTen());
        values.put("matKhau", obj.getMatKhau());
        return db.update("ThuThu", values, "maTT=?", new String[]{obj.getMaTT()});
    }
    public int delete(String id){
        return db.delete("ThuThu", "maTT=?", new String[]{id});
    }
    @SuppressLint("Range")
    public List<ThuThu> getData(String sql, String...selectionArgs){
        List<ThuThu> list = new ArrayList<>();
        Cursor c = db.rawQuery(sql, selectionArgs);
        while(c.moveToNext()){
            ThuThu obj = new ThuThu();
            obj.setHoTen(c.getString(c.getColumnIndex("hoTen")));
            obj.setMaTT(c.getString(c.getColumnIndex("maTT")));
            obj.setMatKhau(c.getString(c.getColumnIndex("matKhau")));

            list.add(obj);
        }
        return list;
    }
    public List<ThuThu> getAll(){
        String sql = "select * from ThuThu";
        return getData(sql);
    }
    public ThuThu getWithID(String id){
        String sql = "select * from ThuThu where maTT=?";
        List<ThuThu> list = getData(sql, id);
        if(list.size() > 0 ) return list.get(0);
        return null;
    }
    public int checkLogin(String id, String password){
        String sql = "select * from ThuThu where maTT=? and matKhau=?";
        List<ThuThu> list = getData(sql, id, password);
        if(list.size() > 0 ) return 1;
        return -1;
    }
    public Boolean checkInsert(String nameAccount,String name, String pass)
    {
        ContentValues values = new ContentValues();
        values.put("maTT",nameAccount);
        values.put("hoTen", name);
        values.put("matKhau", pass);
        long result = db.insert("ThuThu", null, values);
        if(result==-1)
            return false;
        else
            return true;

    }
}
