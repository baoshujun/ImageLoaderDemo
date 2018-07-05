package com.shujun.bao.imageloaderdemo.glide;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.shujun.bao.imageloaderdemo.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Glide1Activity extends AppCompatActivity {

    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.mylayout)
    MyLayout mylayout;

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

            case R.id.button3:
                loadImageByListener();
                break;

            case R.id.button4:
                loadImageForMyLayout();
                break;
        }
    }

    public void loadImage() {
        String url = "http://e.hiphotos.baidu.com/image/pic/item/bd3eb13533fa828b8a65d015f11f4134960a5a85.jpg";
        Glide.with(this).load(url).into(imageView);
    }

    SimpleTarget<GlideDrawable> simpleTarget = new SimpleTarget<GlideDrawable>() {
        @Override
        public void onResourceReady(GlideDrawable resource, GlideAnimation glideAnimation) {
            imageView.setImageDrawable(resource);
        }
    };

    public void loadImageByListener() {
        String url = "http://cn.bing.com/az/hprichbg/rb/TOAD_ZH-CN7336795473_1920x1080.jpg";
        Glide.with(this)
                .load(url)
                .into(simpleTarget);
    }


    public void loadImageForMyLayout() {
        String url = "http://cn.bing.com/az/hprichbg/rb/TOAD_ZH-CN7336795473_1920x1080.jpg";
        Glide.with(this)
                .load(url)
                .into(mylayout.getTarget());
    }
}
