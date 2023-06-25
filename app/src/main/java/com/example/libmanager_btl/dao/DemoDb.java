package com.example.libmanager_btl.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.libmanager_btl.database.DbHelper;
import com.example.libmanager_btl.model.Sach;
import com.example.libmanager_btl.model.ThanhVien;
import com.example.libmanager_btl.model.ThuThu;

import java.util.Random;

public class DemoDb {
    private SQLiteDatabase db;
    private ThanhVienDAO thanhVienDAO;
    private SachDAO sachDAO;
    private ThuThuDAO thuThuDAO;
    private static String TAG = "@@@-***";
    public DemoDb(Context context){
        DbHelper dbHelper = new DbHelper(context);
        db = dbHelper.getWritableDatabase();
        thanhVienDAO = new ThanhVienDAO(context);
        sachDAO = new SachDAO(context);
        thuThuDAO = new ThuThuDAO(context);

    }
    public void thanhVien(){
        ThanhVien tv = new ThanhVien(3, "Tuan", "2000");
        Log.d(TAG, "hello thanh vien");
        if(thanhVienDAO.insert(tv) > 0){
            Log.i(TAG, "thành viên ok"  );
        }else{
            Log.i(TAG, "thành viên fail"  );
        }
//        thanhVienDAO.delete("1");
        Log.i(TAG, "Tổng số thành viên: "+thanhVienDAO.getAll().size());
        tv = new ThanhVien(1, "Tuan Loc", "2030");
        thanhVienDAO.update(tv);
        Log.i(TAG, thanhVienDAO.getWithID(tv.getMaTV()+"").toString());
    }
    public int randomInt(){
        Random random = new Random();
        return random.nextInt(100) +2;
    }
    public void sach(){
        Sach sach = new Sach(2, "Book", 25000, 12, 1);
        Log.d(TAG, "hello sach");
        if(sachDAO.insert(sach) > 0){
            Log.i(TAG, "sach ok"  );
        }else{
            Log.i(TAG, "sach fail"  );
        }
        Log.i(TAG, "Tổng số sach: "+sachDAO.getAll().size());
    }
    public void thuThu(){
        Log.d(TAG, "hello thu thu");

        ThuThu thuThu = new ThuThu("admin", "Tran", "123");
        thuThuDAO.insert(thuThu);
        Log.d(TAG, "so thu thu: "+thuThuDAO.getAll().size());
        if(thuThuDAO.checkLogin("admin", "123") == 1){
            Log.d(TAG, "Login success");
        }else{
            Log.d(TAG, "login fail");
        }
    }


}
