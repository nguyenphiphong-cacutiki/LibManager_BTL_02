package com.example.libmanager_btl;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.libmanager_btl.dao.DemoDb;
import com.example.libmanager_btl.dao.PhieuMuonDAO;
import com.example.libmanager_btl.dao.ThuThuDAO;
import com.example.libmanager_btl.database.DbHelper;
import com.example.libmanager_btl.fragment.ChangeInfoFragment;
import com.example.libmanager_btl.fragment.ChangePassFragment;
import com.example.libmanager_btl.fragment.DoanhThuFragment;
import com.example.libmanager_btl.fragment.LoaiSachFragment;
import com.example.libmanager_btl.fragment.PhieuMuonFragment;
import com.example.libmanager_btl.fragment.SachFragment;
import com.example.libmanager_btl.fragment.ThanhVienFragment;
import com.example.libmanager_btl.fragment.TopFragment;
import com.example.libmanager_btl.model.PhieuMuon;
import com.example.libmanager_btl.model.ThuThu;
import com.google.android.material.navigation.NavigationView;

import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawer;
    private Toolbar toolbar;
    private View mHeaderView;
    private TextView edUser;

    private FragmentManager manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        createData();
        drawer = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar1);

        // set toolbar instead actionbar
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();

        ab.setHomeAsUpIndicator(R.drawable.iconmenu);
        ab.setDisplayHomeAsUpEnabled(true);
        setTitle("Quản lý phiếu mượn");
        manager = getSupportFragmentManager();
        // show fragment quan ly phieu muon
        PhieuMuonFragment phieuMuonFragment = new PhieuMuonFragment();
        manager.beginTransaction()
                .replace(R.id.flContent, phieuMuonFragment)
                .commit();
        //
        NavigationView nv = findViewById(R.id.nvView);
        //show user in header
        mHeaderView = nv.getHeaderView(0);
        edUser = mHeaderView.findViewById(R.id.txtUser);
        // set welcome!
        Intent i = getIntent();
        String user = i.getStringExtra("user");
        ThuThuDAO thuThuDAO = new ThuThuDAO(MainActivity.this);
        ThuThu thuThu = thuThuDAO.getWithID(user);
        edUser.setText("Xin chào "+thuThu.getHoTen()+" ^^");

        thongBaoQuaHan();

        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.nav_PhieuMuon) {
                    setTitle("Quản lý phiếu mượn");
                    PhieuMuonFragment phieuMuonFragment = new PhieuMuonFragment();
                    manager.beginTransaction()
                            .replace(R.id.flContent, phieuMuonFragment)
                            .commit();
                    //Toast.makeText(MainActivity.this, "Quản lý phiếu mượn", Toast.LENGTH_SHORT).show();
                } else if (item.getItemId() == R.id.nav_LoaiSach) {
                    setTitle("Quản lý loại sách");
                    LoaiSachFragment loaiSachFragment = new LoaiSachFragment();
                    manager.beginTransaction()
                            .replace(R.id.flContent, loaiSachFragment)
                            .commit();
                } else if (item.getItemId() == R.id.nav_Sach) {
                    setTitle("Quản lý sách");
                    SachFragment sachFragment = new SachFragment();
                    manager.beginTransaction()
                            .replace(R.id.flContent, sachFragment)
                            .commit();
                } else if (item.getItemId() == R.id.nav_ThanhVien) {
                    setTitle("Quản lý thành viên");
                    ThanhVienFragment thanhVienFragment = new ThanhVienFragment();
                    manager.beginTransaction()
                            .replace(R.id.flContent, thanhVienFragment)
                            .commit();
                } else if (item.getItemId() == R.id.sub_Top) {
                    setTitle("Top 10 sách cho thuê nhiều nhất");
                    TopFragment topFragment = new TopFragment();
                    manager.beginTransaction()
                            .replace(R.id.flContent, topFragment)
                            .commit();
                } else if (item.getItemId() == R.id.sub_DoanhThu) {
                    setTitle("Thống kê doanh thu");
                    DoanhThuFragment doanhThuFragment = new DoanhThuFragment();
                    manager.beginTransaction()
                            .replace(R.id.flContent, doanhThuFragment)
                            .commit();
                } else if (item.getItemId() == R.id.sub_AddUser) {
                    setTitle("Thêm người dùng");
                } else if (item.getItemId() == R.id.sub_Pass) {
                    setTitle("Thay đổi mật khẩu");
                    ChangePassFragment changePassFragment = new ChangePassFragment();
                    manager.beginTransaction()
                            .replace(R.id.flContent, changePassFragment)
                            .commit();
                }else if(item.getItemId() == R.id.sub_Info){
                     setTitle("Thay đổi thông tin");
                    ChangeInfoFragment changeInfoFragment = new ChangeInfoFragment();
                    manager.beginTransaction()
                            .replace(R.id.flContent, changeInfoFragment)
                            .commit();
                }
                 else if (item.getItemId() == R.id.sub_logout) {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                }else {
                    setTitle("unknown");
                }
                drawer.closeDrawers();
                return false;
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home)
            drawer.openDrawer(GravityCompat.START);
        return super.onOptionsItemSelected(item);
    }
    private void thongBaoQuaHan(){
        PhieuMuonDAO phieuMuonDb = new PhieuMuonDAO(this);
        List<PhieuMuon> list = phieuMuonDb.getAll();
        int sum = 0;
        for(PhieuMuon item: list){
            if(PhieuMuon.isQuaHan(item)) sum++;
        }
        if(sum>0) sendNotification(this, "Đang có "+sum+" người mượn sách quá hạn");
    }
    private void sendNotification(Context context, String mes){
//        Toast.makeText(this, "push notification!", Toast.LENGTH_SHORT).show();
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher_round);
//        Notification notification = new Notification.Builder(this, "1");
        Notification notification = new NotificationCompat.Builder(context, MyApplication.CHANNEL_ID)
                .setContentTitle("Thông báo quá hạn")
                .setContentText(mes)
                .setSmallIcon(R.drawable.heart)
                .setLargeIcon(bitmap)
                .build();
        NotificationManager manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        manager.notify( getNotificationId(), notification);

    }
    private int getNotificationId(){
        return (int) new Date().getTime();
    }
}