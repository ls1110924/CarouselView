package com.yunxian.carousel.app;

import com.yunxian.carousel.app.utils.BaseFragmentActivity;

/**
 * @author A Shuai
 * @email lishuai.ls@alibaba-inc.com
 * @date 2016/9/19 23:37
 */
public class EmptyActivity extends BaseFragmentActivity{

    @Override
    protected void onSetContentView() {
        setContentView(R.layout.activity_empty);
    }

    @Override
    protected void onFindViews() {

    }

    @Override
    protected void onBindContent() {

    }

    @Override
    protected boolean onBackKeyDown() {
        finish();
        return true;
    }
}
