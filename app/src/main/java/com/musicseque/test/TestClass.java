package com.musicseque.test;

import android.app.Activity;
import android.os.Bundle;

import com.musicseque.R;

public class TestClass extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        hitAPI();
    }

    private void hitAPI() {
    }
}
