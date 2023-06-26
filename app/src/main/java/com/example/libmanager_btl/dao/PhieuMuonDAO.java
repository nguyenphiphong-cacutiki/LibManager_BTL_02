package com.example.libmanager_btl.dao;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.example.libmanager_btl.database.DbHelper;
import com.example.libmanager_btl.model.PhieuMuon;
import com.example.libmanager_btl.model.Sach;
import com.example.libmanager_btl.model.Top;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class PhieuMuonDAO {
    private static SQLiteDatabase db;
    private Context context;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public PhieuMuonDAO(Context context){
        this.context = context;
        DbHelper dbHelper = new DbHelper(context);
        db = dbHelper.getWritableDatabase();
    }
    public long insert(PhieuMuon obj){
        ContentValues values = new ContentValues();
        values.put("maTT", obj.getMaTT());
        values.put("maTV", obj.getMaTV());
        values.put("maSach", obj.getMaSach());
        values.put("ngay", sdf.format(obj.getNgay()));
        values.put("tienThue", obj.getTienThue());
        values.put("traSach", obj.getTraSach());
        values.put("soLuong", obj.getSoLuong());
        return db.insert("PhieuMuon", null, values);
    }
    public int update(PhieuMuon obj){
        ContentValues values = new ContentValues();
        values.put("maTT", obj.getMaTT());
        values.put("maTV", obj.getMaTV());
        values.put("maSach", obj.getMaSach());
        values.put("ngay", sdf.format(obj.getNgay()));
        values.put("tienThue", obj.getTienThue());
        values.put("traSach", obj.getTraSach());
        values.put("soLuong", obj.getSoLuong());
        return db.update("PhieuMuon", values, "maPM=?", new String[]{String.valueOf(obj.getMaPM())});
    }
    public int delete(String id){
        return db.delete("PhieuMuon", "maPM=?", new String[]{id});
    }
    public int deleteWithMaTV(String maTV){
        return db.delete("PhieuMuon", "maTV=?", new String[]{maTV});
    }
    @SuppressLint("Range")
    public List<PhieuMuon> getData(String sql, String ...selectionArgs){
        List<PhieuMuon> list = new ArrayList<>();
        Cursor c = db.rawQuery(sql, selectionArgs);
        while(c.moveToNext()){
            PhieuMuon obj = new PhieuMuon();
            obj.setMaPM(Integer.parseInt(c.getString(c.getColumnIndex("maPM"))));
            obj.setMaTT(c.getString(c.getColumnIndex("maTT")));
            obj.setMaTV(Integer.parseInt(c.getString(c.getColumnIndex("maTV"))));
            obj.setSoLuong(Integer.parseInt(c.getString(c.getColumnIndex("soLuong"))));
            obj.setMaSach(Integer.parseInt(c.getString(c.getColumnIndex("maSach"))));
            obj.setTienThue(Integer.parseInt(c.getString(c.getColumnIndex("tienThue"))));
            try{
                obj.setNgay(sdf.parse(c.getString(c.getColumnIndex("ngay"))));
            }catch(ParseException e){
                e.printStackTrace();
            }
            obj.setTraSach(Integer.parseInt(c.getString(c.getColumnIndex("traSach"))));
            list.add(obj);
        }
        c.close();
        return list;
    }
    public List<PhieuMuon> getAll(){
        String sql = "select * from PhieuMuon";
        return getData(sql);
    }
    public PhieuMuon getId(String id){
        String sql = "select * from PhieuMuon where maPM=?";
        return getData(sql, id).get(0);
    }
    public List<PhieuMuon> getWithMaSach(String id){
        String sql = "select * from PhieuMuon where maSach=?";
        return getData(sql, id);
    }
    public List<PhieuMuon> getWithMaTVChuaTraSach(String maTV){
        String sql = "select * from PhieuMuon where maTV=? and traSach='"+PhieuMuon.CHUA_TRA+"'";
        return getData(sql, maTV);
    }
    // thống kê top 10
    @SuppressLint("Range")
    public List<Top> getTop(){
        String sqlTop = "select maSach, sum(soLuong) as tongSoLuong from PhieuMuon group by maSach order by tongSoLuong DESC LIMIT 10";
        List<Top> list = new ArrayList<>();
        SachDAO sachDAO = new SachDAO(context);
        Cursor c = db.rawQuery(sqlTop, null);
        while(c.moveToNext()){
            Top top = new Top();
            Sach sach = sachDAO.getId(c.getString(c.getColumnIndex("maSach")));
            top.setTenSach(sach.getTenSach());
            top.setSoLuong(Integer.parseInt(c.getString(c.getColumnIndex("tongSoLuong"))));
            list.add(top);
        }
        c.close();
        return list;
    }
    @SuppressLint("Range")
    public Integer getDoanhThu(String tuNgay, String denNgay){
        String sqlDoanhThu = "select sum(tienThue) as doanhThu from PhieuMuon where ngay between ? and ?";
        List<Integer> list = new ArrayList<>();
        Cursor c = db.rawQuery(sqlDoanhThu, new String[]{tuNgay, denNgay});
        while(c.moveToNext()){
            try{
                list.add(Integer.parseInt(c.getString(c.getColumnIndex("doanhThu"))));
            }catch (Exception e){
                list.add(0);
            }
        }
        c.close();
        return list.get(0);
    }
}
