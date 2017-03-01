package com.liuchad.zhuangbility;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import com.liuchad.zhuangbility.share.QQActivity;
import com.liuchad.zhuangbility.share.SinaActivity;
import com.liuchad.zhuangbility.share.WxActivity;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
    }

    public void testQQ(View view) {
        startActivity(new Intent(TestActivity.this, QQActivity.class));
    }

    public void testSina(View view) {
        startActivity(new Intent(TestActivity.this, SinaActivity.class));
    }

    public void testWx(View view) {
        startActivity(new Intent(TestActivity.this, WxActivity.class));
    }
}
