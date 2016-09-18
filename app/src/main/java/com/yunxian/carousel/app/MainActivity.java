package com.yunxian.carousel.app;

import android.content.Intent;
import android.view.View;

import com.yunxian.carousel.app.utils.BaseFragmentActivity;

public class MainActivity extends BaseFragmentActivity {

    @Override
    protected void onSetContentView() {
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onFindViews() {
        findViewById(R.id.btn).setOnClickListener(mBaseCommonListener);
    }

    @Override
    protected void onBindContent() {

    }

    @Override
    protected boolean onBackKeyDown() {
        finish();
        return true;
    }

    @Override
    public void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.btn: {
                Intent mIntent = new Intent(this, TestCarouselActivity.class);
                startActivity(mIntent);
                break;
            }
            default:
                super.onViewClick(v);
                break;
        }
    }
}
