package com.shujun.bao.imageloaderdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Glide1Activity extends AppCompatActivity {

    @BindView(R.id.imageView)
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glide2);
        ButterKnife.bind(this);
    }


    public void onClicked(View view) {
        switch (view.getId()) {
            case R.id.button2:
                loadImage();
                break;
        }
    }

    public void loadImage() {
        String url = "http://e.hiphotos.baidu.com/image/pic/item/bd3eb13533fa828b8a65d015f11f4134960a5a85.jpg";
        Glide.with(this).load(url).into(imageView);
    }
}
