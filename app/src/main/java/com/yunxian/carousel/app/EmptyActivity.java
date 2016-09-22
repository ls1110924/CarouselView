package com.yunxian.carousel.app;

import com.yunxian.carousel.app.utils.BaseFragmentActivity;

/**
 * @author A Shuai
 * @email ls1110924@163.com
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
