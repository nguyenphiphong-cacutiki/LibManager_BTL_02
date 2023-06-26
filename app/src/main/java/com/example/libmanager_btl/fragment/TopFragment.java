package com.example.libmanager_btl.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.libmanager_btl.R;
import com.example.libmanager_btl.adapter.TopAdapter;
import com.example.libmanager_btl.dao.PhieuMuonDAO;
import com.example.libmanager_btl.model.Top;

import java.util.ArrayList;
import java.util.List;

public class TopFragment extends Fragment {
    private RecyclerView rcvTop;
    private TopAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_top,container,false);
        PhieuMuonDAO phieuMuonDb = new PhieuMuonDAO(getContext());
        rcvTop = v.findViewById(R.id.rcvTop);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false   );
        rcvTop.setLayoutManager(layoutManager);
        adapter = new TopAdapter(getContext(), this);
        rcvTop.setAdapter(adapter);
        adapter.setData(phieuMuonDb.getTop());

        return v;
    }
}