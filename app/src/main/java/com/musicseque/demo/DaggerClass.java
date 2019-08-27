//package com.musicseque.demo;
//
//import android.app.Activity;
//import android.os.Bundle;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.view.View;
//
//import com.musicseque.R;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//
//
//
//
//public class DaggerClass extends Activity {
//    @BindView(R.id.recycler)
//    RecyclerView recycler;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.dagger_class);
//        ButterKnife.bind(this);
//
//
//
//
//        recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
//        getList();
//
//    }
//
//    private void getList() {
//
//    }
//}
