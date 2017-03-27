package com.liuchad.zhuangbility;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.lchad.gifflen.Gifflen;

import java.io.File;
import java.io.IOException;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

public class TestActivity extends AppCompatActivity {

    private GifImageView mGifImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        mGifImageView = (GifImageView) findViewById(R.id.gif);

        findViewById(R.id.fab_edit_task).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gifflen gifflen = new Gifflen.Builder()
                        .color(256)
                        .delay(500)
                        .quality(10)
                        .width(320)
                        .height(320)
                        .listener(new Gifflen.OnEncodeFinishListener() {
                            @Override
                            public void onEncodeFinish(String path) {
                                Toast.makeText(TestActivity.this, "已保存gif到" + path, Toast.LENGTH_LONG).show();
                                try {
                                    GifDrawable gifFromPath = new GifDrawable(path);
                                    mGifImageView.setImageDrawable(gifFromPath);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        })
                        .build();
                gifflen.encode(TestActivity.this, Environment.getExternalStorageDirectory().getAbsolutePath() +
                        File.separator + "gifflen-sample.gif", new int[]{
                        R.drawable.target1,
                        R.drawable.target2,
                        R.drawable.target3,
                        R.drawable.target4,
                        R.drawable.target5
                });
            }
        });
    }
}